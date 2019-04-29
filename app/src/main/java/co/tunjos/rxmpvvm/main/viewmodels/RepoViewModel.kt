package co.tunjos.rxmpvvm.main.viewmodels

import co.tunjos.rxmpvvm.model.Repo.Owner

data class RepoViewModel(
    val name: String? = null,
    val owner: Owner? = null,
    val full_name: String? = null,
    val url: String? = null,
    val forksUrl: String? = null,
    val stargazersUrl: String? = null,
    val description: String? = null,
    val stargazersCount: String? = null,
    val stargazersCountVisibility: Boolean = false,
    val language: String? = null,
    val forks: String? = null,
    val forksVisibility: Boolean? = false,
    val subscribersCount: String? = null
)
