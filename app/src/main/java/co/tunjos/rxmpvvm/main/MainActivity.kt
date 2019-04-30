package co.tunjos.rxmpvvm.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import co.tunjos.rxmpvvm.BuildConfig
import co.tunjos.rxmpvvm.R
import co.tunjos.rxmpvvm.api.GithubApi
import co.tunjos.rxmpvvm.base.activities.BaseActivity
import co.tunjos.rxmpvvm.base.mvvm.presenteractions.PresenterAction
import co.tunjos.rxmpvvm.base.presenters.register
import co.tunjos.rxmpvvm.databinding.ActivityMainBinding
import co.tunjos.rxmpvvm.extensions.rx.subscribe
import co.tunjos.rxmpvvm.extensions.rx.view.click
import co.tunjos.rxmpvvm.main.actions.ShowUserNameDialogAction
import co.tunjos.rxmpvvm.main.adapters.ReposAdapter
import co.tunjos.rxmpvvm.main.presenters.MainPresenter
import co.tunjos.rxmpvvm.main.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
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
        when (it) {
            is ShowUserNameDialogAction -> showUserNameDialog()
        }
    }

    //TODO Encapsulate this
    private fun showUserNameDialog() {
        @SuppressLint("InflateParams")
        val usernameView = layoutInflater.inflate(R.layout.dialog_username, null)

        val edtxUsername = usernameView.findViewById<EditText>(R.id.edtx_username)

        val builder = AlertDialog.Builder(this, R.style.GithubDialogStyle)
            .setTitle(R.string.github)
            .setView(usernameView)
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(R.string.github, null)
        val alertDialog = builder.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { v ->
            val username = edtxUsername.text.toString()

            if (username.isEmpty()) {
                edtxUsername.error = getString(R.string.username_empty)
                return@setOnClickListener
            }

            presenter.getRepos(username)
//            presenter.setLastUsername(username)

            supportActionBar?.subtitle = username

            alertDialog.dismiss()
        }

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener { v ->
            edtxUsername.setText("")
            presenter.getRepos(GithubApi.USERNAME_GITHUB)
//            presenter.setLastUsername(GithubApi.USERNAME_GITHUB)

            supportActionBar?.subtitle = GithubApi.USERNAME_GITHUB
            alertDialog.dismiss()
        }
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
            R.id.action_about -> {
                showAboutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //TODO Encapsulate this
    private fun showAboutDialog() {
        @SuppressLint("InflateParams")
        val aboutView = layoutInflater.inflate(R.layout.dialog_about, null)

        val tvVersionName = aboutView.findViewById<TextView>(R.id.tv_version_name)
        val tvVersionNo = aboutView.findViewById<TextView>(R.id.tv_version_no)
        val tvBuildTime = aboutView.findViewById<TextView>(R.id.tv_build_time)

        tvVersionName.text = BuildConfig.VERSION_NAME
        tvVersionNo.text = String.format(Locale.getDefault(), "%d", BuildConfig.VERSION_CODE)
        tvBuildTime.text = BuildConfig.latestBuildTime

        val builder = AlertDialog.Builder(this, R.style.GithubDialogStyle)
            .setTitle(R.string.action_about)
            .setView(aboutView)
            .setPositiveButton(android.R.string.ok, null)
        val alertDialog = builder.show()

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).isEnabled = false
    }
}
