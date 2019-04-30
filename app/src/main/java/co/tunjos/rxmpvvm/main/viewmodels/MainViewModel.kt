package co.tunjos.rxmpvvm.main.viewmodels

import androidx.annotation.StringRes
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import co.tunjos.rxmpvvm.base.di.scopes.ActivityScope
import javax.inject.Inject

@ActivityScope
class MainViewModel @Inject constructor() {
    val repos = ObservableArrayList<RepoViewModel>()

    val progressBarVisibility = ObservableBoolean()
    val reposRecyclerViewVisibility = ObservableBoolean()
    val messageViewVisibility = ObservableBoolean()

    val messageViewTextId = ObservableInt()
    val messageViewText = ObservableField<String>()


    fun showMessage(msg: String? = null) {
        messageViewText.set(msg)
    }

    fun showMessageWithId(@StringRes resId: Int = 0) {
        messageViewTextId.set(resId)
    }

    fun updateRepos(repos: List<RepoViewModel>) {
        this.repos.clear()
        this.repos.addAll(repos)
    }
}
