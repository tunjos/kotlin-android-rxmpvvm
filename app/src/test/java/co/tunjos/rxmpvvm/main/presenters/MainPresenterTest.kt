package co.tunjos.rxmpvvm.main.presenters

import co.tunjos.rxmpvvm.R
import co.tunjos.rxmpvvm.api.GithubApi
import co.tunjos.rxmpvvm.base.ResponseBodyConverter
import co.tunjos.rxmpvvm.base.mvvm.uievents.UiEvent
import co.tunjos.rxmpvvm.base.mvvm.uievents.ViewClickEvent
import co.tunjos.rxmpvvm.base.repo.GithubRepository
import co.tunjos.rxmpvvm.main.actions.ShowUserNameDialogAction
import co.tunjos.rxmpvvm.main.viewmodels.MainViewModel
import co.tunjos.rxmpvvm.main.viewmodels.mappers.RepoViewModelMapper
import co.tunjos.rxmpvvm.model.Repo
import co.tunjos.rxmpvvm.model.Repo.Owner
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import okhttp3.ResponseBody
import org.junit.jupiter.api.Test
import retrofit2.Response

class MainPresenterTest {
    private val githubRepository: GithubRepository = mock {
        whenever(it.getRepos(any())).thenReturn(Single.just(dummySuccessResponse))
    }

    private val responseBodyConverter: ResponseBodyConverter = mock()

    private val mainViewModel: MainViewModel = spy()

    private val uiEvents = PublishSubject.create<UiEvent>()

    private var underTest = createPresenter()

    private val presenterActions = underTest.presenterActions.test()

    @Test
    fun getReposSucceeds() {
        underTest.createSubscriptions()

        val username = "github"
        underTest.getRepos(username)

        verify(mainViewModel, times(2)).updateRepos(dummyReposViewModel)
        verify(githubRepository, times(2)).getRepos(username)
    }

    @Test
    fun getReposFailsApiError() {
        val githubRepository: GithubRepository = mock {
            whenever(it.getRepos(any())).thenReturn(Single.just(dummyErrorResponse))
        }

        underTest = createPresenter(githubRepository)

        val username = "github"
        underTest.getRepos(username)

        verify(mainViewModel).showMessage()
    }

    @Test
    fun getReposFailsThrowable() {
        val githubRepository: GithubRepository = mock {
            whenever(it.getRepos(any())).thenReturn(Single.error(Exception()))
        }

        underTest = createPresenter(githubRepository)

        val username = "github"
        underTest.getRepos(username)

        verify(mainViewModel).showMessage(anyOrNull())
    }

    @Test
    fun clickFabUsernameEmitsShowUserNameDialogAction() {
        underTest.createSubscriptions()

        uiEvents.onNext(ViewClickEvent(R.id.fab_username))

        presenterActions.assertValue { it is ShowUserNameDialogAction }
    }

    private fun createPresenter(customGithubRepository: GithubRepository = githubRepository) =
        MainPresenter(
            compositeDisposable = compositeDisposable,
            uiEvents = uiEvents,
            ioScheduler = Schedulers.trampoline(),
            uiScheduler = Schedulers.trampoline(),
            githubRepository = customGithubRepository,
            responseBodyConverter = responseBodyConverter,
            mainViewModel = mainViewModel,
            repoViewModelMapper = repoViewModelMapper
        )

    private companion object {
        private val compositeDisposable = CompositeDisposable()

        private val dummyOwner = Owner(login = "login")
        private val dummyRepo = Repo(
            id = 1,
            name = "name",
            owner = dummyOwner,
            full_name = "name",
            url = "https://url.com",
            forksUrl = "https://forksUrl.com",
            stargazersUrl = "https://stargazersUrl.com",
            description = "description",
            stargazersCount = 1,
            language = "language",
            forks = 1,
            subscribersCount = 1
        )
        private val dummyRepos = listOf(dummyRepo)

        private val repoViewModelMapper: RepoViewModelMapper = RepoViewModelMapper()
        private val dummyReposViewModel =
            dummyRepos.map { repo -> repoViewModelMapper.toRepoViewModel(repo) }

        private val dummySuccessResponse = Response.success(dummyRepos)
        private val dummyErrorResponse =
            Response.error<List<Repo>>(GithubApi.HTTP_CODE_404, ResponseBody.create(null, "null"))
    }
}
