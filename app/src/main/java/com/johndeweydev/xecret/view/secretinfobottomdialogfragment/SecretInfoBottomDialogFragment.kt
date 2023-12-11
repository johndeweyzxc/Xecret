package com.johndeweydev.xecret.view.secretinfobottomdialogfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.johndeweydev.xecret.R
import com.johndeweydev.xecret.databinding.FragmentSecretInfoBottomDialogBinding
import com.johndeweydev.xecret.view.createsecretbottomdialogfragment.CreateSecretBottomDialogFragment
import com.johndeweydev.xecret.viewmodel.SecretsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class SecretInfoBottomDialogFragment : CreateSecretBottomDialogFragment() {

  private var binding: FragmentSecretInfoBottomDialogBinding? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    modeFlag = "UPDATE"
    binding = FragmentSecretInfoBottomDialogBinding.inflate(inflater, container, false)
    secretsViewModel = ViewModelProvider(requireActivity())[SecretsViewModel::class.java]
    return binding?.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding?.materialCardViewNameAndDescriptionSecretInfoBottomDialog?.setOnClickListener {
      showNameAndDescriptionInputDialog()
    }
    binding?.materialCardViewNoteSecretInfoBottomDialog?.setOnClickListener {
      showNoteInputDialog()
    }
    binding?.materialCardViewUsernameAndPasswordSecretInfoBottomDialog?.setOnClickListener {
      showUsernameAndPasswordInputDialog()
    }
    binding?.materialCardViewSecurityCodesSecretInfoBottomDialog?.setOnClickListener {
      showAddedSecurityCodesInputDialog()
    }
    binding?.materialCardViewAssociatedEmailsSecretInfoBottomDialog?.setOnClickListener {
      showAddedEmailAccountsInputDialog()
    }
    binding?.materialCardViewAssociatedPhoneNumbersSecretInfoBottomDialog?.setOnClickListener {
      showAddedPhoneNumbersInputDialog()
    }
    binding?.materialCardViewExtraInformationSecretInfoBottomDialog?.setOnClickListener {
      showExtraInformationInputDialog()
    }
    binding?.materialCardViewMetadataSecretInfoBottomDialog?.setOnClickListener {
      showMetadataDialog()
    }
    binding?.buttonUpdateSecretInfoBottomDialog?.setOnClickListener {
      val response = secretsViewModel.updateSecret()
      if (response != "None") {
        Toast.makeText(requireActivity(), response, Toast.LENGTH_LONG).show()
      } else {
        dismiss()
      }
    }
    setData()
    setupObservers()
  }

  private fun setupObservers() {
    val totalSecurityCodesObserver = Observer<Int?> {
      if (it == null) { return@Observer }
      val securityCodeDescription = "$it security codes have been added"
      Log.d("dev-log", "SecretInfoBottomDialogFragment.setupObservers: " +
              securityCodeDescription)
      binding?.textViewSecurityCodesDescriptionSecretInfoBottomDialog
        ?.text = securityCodeDescription
    }
    secretsViewModel.getTotalSecurityCodes().observe(viewLifecycleOwner, totalSecurityCodesObserver)
    val totalAssociatedEmailsObserver = Observer<Int?> {
      if (it == null) { return@Observer }
      setDescriptionForEmail(it)
    }
    secretsViewModel.getTotalAssociatedEmails().observe(viewLifecycleOwner,
      totalAssociatedEmailsObserver)
    val totalAssociatedPhoneNumbersObserver = Observer<Int?> {
      if (it == null) { return@Observer }
      setDescriptionForPhoneNumber(it)
    }
    secretsViewModel.getTotalAssociatedPhoneNumbers().observe(viewLifecycleOwner,
      totalAssociatedPhoneNumbersObserver)
  }

  private fun setData() {
    binding?.textViewNameAndDescriptionCreateSecret?.text = secretsViewModel.selectedSecret?.name
    binding?.textViewNameAndDescriptionDescCreateSecret?.text = secretsViewModel
      .selectedSecret?.description

    val numberOfSecurityCodes = secretsViewModel.selectedSecret?.extraPasswordsOrSecurityCodes?.size
    val textCodes = "$numberOfSecurityCodes security codes have been added"
    binding?.textViewSecurityCodesDescriptionSecretInfoBottomDialog?.text = textCodes

    val numberOfAssociatedEmail = secretsViewModel.selectedSecret?.associatedEmails?.size
    setDescriptionForEmail(numberOfAssociatedEmail)

    val numberOfAssociatedPhoneNumber = secretsViewModel.selectedSecret
      ?.associatedPhoneNumbers?.size
    setDescriptionForPhoneNumber(numberOfAssociatedPhoneNumber)
  }

  private fun setDescriptionForEmail(numberOfAssociatedEmail: Int?) {
    val twoFaNotEnabled = "$numberOfAssociatedEmail associations, 2FA is not enabled"
    val twoFaIsEnabled = "$numberOfAssociatedEmail associations, 2FA is enabled"
    if (secretsViewModel.selectedSecret?.usingEmailForTwoFA == true) {
      binding?.textViewEmailTwoFactorIndicatorSecretInfoBottomDialog?.text = twoFaIsEnabled
    } else {
      binding?.textViewEmailTwoFactorIndicatorSecretInfoBottomDialog?.text = twoFaNotEnabled
    }
  }

  private fun setDescriptionForPhoneNumber(numberOfAssociatedPhoneNumber: Int?) {
    val twoFaPhoneNotEnabled = "$numberOfAssociatedPhoneNumber associations, 2FA is not enabled"
    val twoFaPhoneIsEnabled = "$numberOfAssociatedPhoneNumber associations, 2FA is enabled"
    if (secretsViewModel.selectedSecret?.usingPhoneNumberForTwoFA == true) {
      binding?.textViewPhoneTwoFactorIndicatorSecretInfoBottomDialog?.text = twoFaPhoneIsEnabled
    } else {
      binding?.textViewPhoneTwoFactorIndicatorSecretInfoBottomDialog?.text = twoFaPhoneNotEnabled
    }
  }

  @SuppressLint("InflateParams")
  fun showMetadataDialog() {
    val metadataDialog = LayoutInflater.from(requireActivity()).inflate(
      R.layout.dialog_metadata, null)
    val textViewCreated = metadataDialog.findViewById<TextView>(R.id.textViewCreatedValueMetadata)
    val textViewUpdated = metadataDialog.findViewById<TextView>(R.id.textViewUpdatedValueMetadata)

    val dateFormat = SimpleDateFormat("MMMM. d, yyyy - hh:mm:ss", Locale.getDefault())
    textViewCreated.text = secretsViewModel.selectedSecret?.createdAt?.let { dateFormat.format(it) }
    textViewUpdated.text = secretsViewModel.selectedSecret?.updatedAt?.let { dateFormat.format(it) }

    MaterialAlertDialogBuilder(requireActivity())
      .setTitle("Metadata")
      .setView(metadataDialog)
      .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
      .create().show()
  }

  override fun onDestroyView() {
    secretsViewModel.getTotalSecurityCodes().value = null
    secretsViewModel.getTotalAssociatedEmails().value = null
    secretsViewModel.getTotalAssociatedPhoneNumbers().value = null
    binding = null
    super.onDestroyView()
  }
}