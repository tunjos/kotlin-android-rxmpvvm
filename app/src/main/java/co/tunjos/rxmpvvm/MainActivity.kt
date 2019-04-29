package co.tunjos.rxmpvvm

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import co.tunjos.rxmpvvm.base.activities.BaseActivity
import co.tunjos.rxmpvvm.base.mvvm.presenteractions.PresenterAction
import co.tunjos.rxmpvvm.base.presenters.register
import co.tunjos.rxmpvvm.databinding.ActivityMainBinding
import co.tunjos.rxmpvvm.extensions.rx.subscribe
import co.tunjos.rxmpvvm.extensions.rx.view.click
import co.tunjos.rxmpvvm.main.adapters.ReposAdapter
import co.tunjos.rxmpvvm.main.presenters.MainPresenter
import co.tunjos.rxmpvvm.main.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var presenter: MainPresenter

    @Inject
    lateinit var reposAdapter: ReposAdapter

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    override fun getContentView() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        bind()
        registerActions()
    }

    private fun bind() {
        binding = DataBindingUtil.bind(findViewById(R.id.main_container))!!
        with(binding) {
            rvRepos.adapter = reposAdapter
            model = mainViewModel
        }
    }

    private fun registerActions() {
        with(presenter) {
            presenterActions.subscribe(
                lifecycle
            ) { handlePresenterAction(it) }
            register(lifecycle)
        }
    }

    private fun handlePresenterAction(it: PresenterAction) {

    }

    override fun compileViewUiEventGenerators() = arrayOf(
        binding.fabUsername.click(),
        reposAdapter.uiEvents
    )

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
