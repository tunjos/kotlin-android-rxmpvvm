package co.tunjos.rxmpvvm.api

import co.tunjos.rxmpvvm.model.Repo
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {

    @GET("users/{username}/repos")
    fun getRepos(@Path("username") username: String): Single<Response<List<Repo>>>

    @GET("repos/{owner}/{repo}")
    fun getRepo(@Path("owner") owner: String, @Path("repo") repo: String): Single<Response<Repo>>

    companion object {
        val USERNAME_GITHUB = "github"
        val ACCEPT_HEADER_JSON = "application/vnd.github.v3+json"
        val GITHUB_API_BASE_URL = "https://api.github.com"
    }
}
