package co.tunjos.rxmpvvm.main.viewmodels.mappers

import co.tunjos.rxmpvvm.main.viewmodels.RepoViewModel
import co.tunjos.rxmpvvm.model.Repo
import javax.inject.Inject

class RepoViewModelMapper @Inject constructor() {

    fun toRepoViewModel(repo: Repo) =
        RepoViewModel(
            name = repo.name,
            owner = repo.owner,
            full_name = repo.full_name,
            url = repo.url,
            forksUrl = repo.forksUrl,
            stargazersUrl = repo.stargazersUrl,
            description = repo.description,
            stargazersCount = Integer.toString(repo.stargazersCount),
            stargazersCountVisibility = repo.stargazersCount > 0,
            language = repo.language,
            forks = Integer.toString(repo.forks),
            forksVisibility = repo.forks > 0,
            subscribersCount = Integer.toString(repo.subscribersCount)
        )
}
