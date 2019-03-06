package co.tunjos.rxmpvvm.model

/**
 * The model for a github repository.
 */
data class Repo(
    val id: Int = 0,
    val name: String? = null,
    val owner: Owner? = null,
    val full_name: String? = null,
    val url: String? = null,
    val forksUrl: String? = null,
    val stargazersUrl: String? = null,
    val description: String? = null,
    val stargazersCount: Int = 0,
    val language: String? = null,
    val forks: Int = 0,
    val subscribersCount: Int = 0
) {
    data class Owner(val login: String? = null)
}


