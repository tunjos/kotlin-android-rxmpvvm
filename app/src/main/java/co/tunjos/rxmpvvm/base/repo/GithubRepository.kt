package co.tunjos.rxmpvvm.base.repo

import co.tunjos.rxmpvvm.api.GithubApi
import co.tunjos.rxmpvvm.model.Repo
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(private val githubApi: GithubApi) {

    fun getRepos(username: String): Single<Response<List<Repo>>> =
        githubApi
            .getRepos(username)

    fun getRepo(owner: String, repo: String): Single<Response<Repo>> =
        githubApi
            .getRepo(owner, repo)
}
