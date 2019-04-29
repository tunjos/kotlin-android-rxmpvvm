package co.tunjos.rxmpvvm.main.adapters

import co.tunjos.rxmpvvm.BR
import co.tunjos.rxmpvvm.R
import co.tunjos.rxmpvvm.base.adapters.SimpleDataBindingListAdapter
import co.tunjos.rxmpvvm.main.viewmodels.RepoViewModel
import javax.inject.Inject

class ReposAdapter @Inject constructor() : SimpleDataBindingListAdapter<RepoViewModel>(
    layoutResId = R.layout.repo_list_item,
    modelVariable = BR.model
)
