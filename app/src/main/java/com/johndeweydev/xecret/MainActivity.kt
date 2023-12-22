package com.johndeweydev.xecret

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.johndeweydev.xecret.api.secretsdb.SecretsSingleton
import com.johndeweydev.xecret.databinding.ActivityMainBinding
import com.johndeweydev.xecret.viewmodel.SecretsViewModel

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

  private var binding: ActivityMainBinding? = null
  private lateinit var secretsViewModel: SecretsViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val secretsSingleton = SecretsSingleton.getInstance()
    secretsSingleton.setSecretsDatabase(applicationContext)
    secretsViewModel = ViewModelProvider(this)[SecretsViewModel::class.java]

    binding = ActivityMainBinding.inflate(layoutInflater)
    setSupportActionBar(binding?.toolBarMain)
    setContentView(binding?.root)

    binding?.toolBarMain?.setNavigationOnClickListener {
      binding?.drawerLayoutMain?.open()
    }
    binding?.navigationViewMain?.setNavigationItemSelectedListener(
      this::navigationDrawerItemSelected)
  }

  private fun navigationDrawerItemSelected(item: MenuItem): Boolean {
    binding?.drawerLayoutMain?.close()
    when (item.itemId) {
      R.id.allSecretsToolBarDrawerMenu -> secretsViewModel.getAllNonTemporarilyDeletedSecrets()
      R.id.starredToolBarDrawerMenu -> {
        TODO("Implementation")
      }
      R.id.trashToolBarDrawerMenu -> secretsViewModel.getAllTemporarilyDeletedSecret()
    }
    return false
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.toolbar_menu, menu)
    val search = menu?.findItem(R.id.searchToolBarMenu)
    val searchView = search?.actionView as? SearchView
    searchView?.isSubmitButtonEnabled = true
    searchView?.setOnQueryTextListener(this)
    return true
  }

  override fun onQueryTextSubmit(query: String?): Boolean {
    return true
  }

  override fun onQueryTextChange(query: String?): Boolean {
    if (query != null) {
      val searchQuery = "%$query%"
      secretsViewModel.searchDatabase(searchQuery)
    }
    return true
  }
}