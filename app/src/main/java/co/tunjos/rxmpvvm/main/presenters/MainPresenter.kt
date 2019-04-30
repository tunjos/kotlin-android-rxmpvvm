package co.tunjos.rxmpvvm.main.presenters

import co.tunjos.rxmpvvm.R
import co.tunjos.rxmpvvm.api.GithubApi
import co.tunjos.rxmpvvm.base.ResponseBodyConverter
import co.tunjos.rxmpvvm.base.di.qualifiers.ActivityUiEvents
import co.tunjos.rxmpvvm.base.di.qualifiers.IoScheduler
import co.tunjos.rxmpvvm.base.di.qualifiers.UiScheduler
import co.tunjos.rxmpvvm.base.mvvm.presenteractions.PresenterAction
import co.tunjos.rxmpvvm.base.mvvm.uievents.UiEvent
import co.tunjos.rxmpvvm.base.mvvm.uievents.ViewClickEvent
import co.tunjos.rxmpvvm.base.presenters.BaseDisposablePresenter
import co.tunjos.rxmpvvm.base.repo.GithubRepository
import co.tunjos.rxmpvvm.extensions.rx.disposeWith
import co.tunjos.rxmpvvm.main.actions.ShowUserNameDialogAction
import co.tunjos.rxmpvvm.main.viewmodels.MainViewModel
import co.tunjos.rxmpvvm.main.viewmodels.mappers.RepoViewModelMapper
import co.tunjos.rxmpvvm.model.Repo
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import okhttp3.ResponseBody
import timber.log.Timber
import javax.inject.Inject

class MainPresenter @Inject constructor(
    override val compositeDisposable: CompositeDisposable,
    @ActivityUiEvents private val uiEvents: Observable<UiEvent>,
    @IoScheduler private val ioScheduler: Scheduler,
    @UiScheduler private val uiScheduler: Scheduler,
    private val githubRepository: GithubRepository,
    private val responseBodyConverter: ResponseBodyConverter,
    private val mainViewModel: MainViewModel,
    private val repoViewModelMapper: RepoViewModelMapper
) : BaseDisposablePresenter(compositeDisposable) {

    override val presenterActions: Observable<PresenterAction> get() = emitter.hide()
    private val emitter = PublishSubject.create<PresenterAction>()

    override fun createSubscriptions() {
        getRepos(GithubApi.USERNAME_GITHUB)

        uiEvents
            .ofType(ViewClickEvent::class.java)
            .filter { it.id == R.id.fab_username }
            .subscribe { emitter.onNext(ShowUserNameDialogAction) }
            .disposeWith(compositeDisposable)
    }

    fun getRepos(username: String) {
        githubRepository
            .getRepos(username = username)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .doOnSubscribe {
                mainViewModel.reposRecyclerViewVisibility.set(false)
                mainViewModel.repos.clear()
                updateProgressBarVisibility(true)
            }
            .subscribe({
                updateProgressBarVisibility(false)
                mainViewModel.reposRecyclerViewVisibility.set(true)

                if (it.isSuccessful && it.code() == GithubApi.HTTP_CODE_200) {
                    val repos: List<Repo>? = it.body()
                    if (!repos.isNullOrEmpty()) {
                        mainViewModel.reposRecyclerViewVisibility.set(true)
                        mainViewModel.updateRepos(repos.map { repo -> repoViewModelMapper.toRepoViewModel(repo) })
                    } else {
                        mainViewModel.reposRecyclerViewVisibility.set(false)
                        mainViewModel.messageViewVisibility.set(true)
                        mainViewModel.showMessageWithId(R.string.empty_repositories)
                    }
                } else if (it.code() == GithubApi.HTTP_CODE_404) {
                    val errorBody: ResponseBody? = it.errorBody()
                    errorBody?.let { responseBody ->
                        val apiError = responseBodyConverter.convertToApiError(responseBody)
                        mainViewModel.messageViewVisibility.set(true)
                        mainViewModel.showMessage(apiError?.message)
                    }
                }
            }, {
                Timber.e(it)
                updateProgressBarVisibility(false)
                mainViewModel.showMessage()
            })
            .disposeWith(compositeDisposable)
    }

    private fun updateProgressBarVisibility(visible: Boolean) {
        mainViewModel.progressBarVisibility.set(visible)
    }
}
