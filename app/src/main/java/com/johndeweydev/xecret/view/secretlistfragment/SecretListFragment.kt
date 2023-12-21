package com.johndeweydev.xecret.view.secretlistfragment

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.johndeweydev.xecret.R
import com.johndeweydev.xecret.databinding.FragmentSecretListBinding
import com.johndeweydev.xecret.model.data.SecretData
import com.johndeweydev.xecret.viewmodel.SecretsViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class SecretListFragment : Fragment() {

  private var binding: FragmentSecretListBinding? = null
  private lateinit var secretsViewModel: SecretsViewModel
  private var secretListRVAdapter: SecretListRVAdapter? = null

  private var indexOfCurrentlySelectedSecret = -1
  private var swipedSecretData: SecretData? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentSecretListBinding.inflate(inflater, container, false)
    secretsViewModel = ViewModelProvider(requireActivity())[SecretsViewModel::class.java]
    return binding?.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding?.floatingActionButtonSecretList?.setOnClickListener {
      Navigation.findNavController(it).navigate(
        R.id.action_secretListFragment_to_createSecretFragment)
    }

    setupRecyclerView()
    setupObservers()
    secretsViewModel.getAllSecrets()
  }

  private fun setupRecyclerView() {
    val recyclerViewEvent: SecretListRVAdapter.Event = object : SecretListRVAdapter.Event {
      override fun onClickSecret(secretData: SecretData, pos: Int) {
        indexOfCurrentlySelectedSecret = pos

        secretsViewModel.selectedSecret = secretData
        secretsViewModel.selectedSecretCopy = secretsViewModel.selectedSecret?.clone()

        val action = R.id.action_secretListFragment_to_secretInfoBottomDialogFragment
        binding?.root?.let { Navigation.findNavController(it).navigate(action) }
      }
    }

    secretListRVAdapter = SecretListRVAdapter(recyclerViewEvent)
    binding?.recyclerViewSecretList?.adapter = secretListRVAdapter
    binding?.recyclerViewSecretList?.layoutManager = LinearLayoutManager(requireActivity())

    val itemTouchHelper = ItemTouchHelper(recyclerViewSwipe())
    itemTouchHelper.attachToRecyclerView(binding?.recyclerViewSecretList)
  }

  private fun recyclerViewSwipe(): ItemTouchHelper.SimpleCallback {
    return object : ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.LEFT) {
      override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
      ): Boolean {
        return false
      }

      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // The current position of the swiped recycler view item
        val position = viewHolder.bindingAdapterPosition
        if (direction == ItemTouchHelper.LEFT) {
          swipedLeft(position)
        }
      }

      private fun swipedLeft(position: Int) {
        // User swiped left on the recycler view item, this will partially delete the item in the
        // view but the information is still in the database.
        swipedSecretData = secretListRVAdapter?.removeSecret(position)

        val snackbarDelete = binding?.recyclerViewSecretList?.let {
          Snackbar.make(it, "Deleted ${swipedSecretData?.name}", Snackbar.LENGTH_LONG)
        }
        snackbarDelete?.setAction("Undo") { _ ->
          swipedSecretData?.let { secretListRVAdapter?.insertSecret(position, it) }
          swipedSecretData = null
        }

        val callbackDelete = createSnackbarCallbackForDeleting()
        snackbarDelete?.addCallback(callbackDelete)
        snackbarDelete?.show()
      }

      private fun createSnackbarCallbackForDeleting(): Snackbar.Callback {
        return object : Snackbar.Callback() {
          override fun onShown(sb: Snackbar?) {
            super.onShown(sb)
          }

          override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            if (swipedSecretData == null) {
              Log.e("dev-log", "SecretListFragment.createSnackbarCallbackForDeleting: " +
                      "Swiped secret data is null")
              return
            }

            // Deletes the secret data in the database
            Log.d("dev-log", "SecretListFragment.createSnackbarCallbackForDeleting: " +
                    "Temporarily deleting ${swipedSecretData?.name}")
            secretsViewModel.temporaryDeleteSecret(swipedSecretData)
            swipedSecretData = null
          }
        }
      }

      override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
      ) {
        RecyclerViewSwipeDecorator.Builder(
          c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
        ).addSwipeLeftBackgroundColor(ContextCompat.getColor(requireActivity(),
            R.color.md_theme_light_error))
          .addSwipeLeftActionIcon(R.drawable.ic_delete_24)
          .create()
          .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
      }
    }
  }

  private fun setupObservers() {
    val newlyAddedSecretObserver = Observer<SecretData?> {
      if (it == null) {
        return@Observer
      }
      secretListRVAdapter?.addSecret(it)
      secretsViewModel.getNewlyAddedSecret().value = null
    }
    secretsViewModel.getNewlyAddedSecret().observe(viewLifecycleOwner, newlyAddedSecretObserver)
    val newlyUpdatedSecretObserver = Observer<SecretData?> {
      if (it == null) {
        return@Observer
      }
      if (indexOfCurrentlySelectedSecret != -1) {
        secretListRVAdapter?.removeSecret(indexOfCurrentlySelectedSecret)
        secretListRVAdapter?.insertSecret(indexOfCurrentlySelectedSecret, it)
        indexOfCurrentlySelectedSecret = -1
        secretsViewModel.getNewlyUpdatedSecret().value = null
      }
    }
    secretsViewModel.getNewlyUpdatedSecret().observe(viewLifecycleOwner, newlyUpdatedSecretObserver)
    val secretsObserver = Observer<ArrayList<SecretData>?> {
      if (it == null) {
        return@Observer
      }
      secretListRVAdapter?.addListOfSecrets(it)
      secretsViewModel.getSecrets().value = null
    }
    secretsViewModel.getSecrets().observe(viewLifecycleOwner, secretsObserver)
    val showNoSecretsFoundIndicatorObserver = Observer<Boolean> {
      if (it) {
        binding?.imageViewSecretList?.visibility = View.VISIBLE
        binding?.textViewNoSecretsFoundSecretList?.visibility = View.VISIBLE
      } else {
        binding?.imageViewSecretList?.visibility = View.GONE
        binding?.textViewNoSecretsFoundSecretList?.visibility = View.GONE
      }
    }
    secretsViewModel.getShowNoSecretsFoundIndicator().observe(viewLifecycleOwner,
      showNoSecretsFoundIndicatorObserver)
  }

  override fun onDestroyView() {
    binding = null
    secretListRVAdapter = null
    super.onDestroyView()
  }
}