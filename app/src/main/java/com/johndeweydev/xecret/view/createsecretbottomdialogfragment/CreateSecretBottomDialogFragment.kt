package com.johndeweydev.xecret.view.createsecretbottomdialogfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.johndeweydev.xecret.R
import com.johndeweydev.xecret.databinding.FragmentCreateSecretBottomDialogBinding
import com.johndeweydev.xecret.model.data.SecretData
import com.johndeweydev.xecret.viewmodel.SecretsViewModel
import java.util.Date

open class CreateSecretBottomDialogFragment : BottomSheetDialogFragment() {

  private var binding: FragmentCreateSecretBottomDialogBinding? = null
  lateinit var secretsViewModel: SecretsViewModel
  lateinit var modeFlag: String

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    modeFlag = "CREATE"
    binding = FragmentCreateSecretBottomDialogBinding.inflate(inflater, container, false)
    secretsViewModel = ViewModelProvider(requireActivity())[SecretsViewModel::class.java]
    return binding?.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding?.materialCardViewSetNameAndDescriptionCreateSecretBottomDialog?.setOnClickListener {
      showNameAndDescriptionInputDialog()
    }
    binding?.materialCardViewSetNoteCreateSecretBottomDialog?.setOnClickListener {
      showNoteInputDialog()
    }
    binding?.materialCardViewSetUsernameAndPasswordCreateSecretBottomDialog?.setOnClickListener {
      showUsernameAndPasswordInputDialog()
    }
    binding?.materialCardViewAddSecurityCodesCreateSecretBottomDialog?.setOnClickListener {
      showAddedSecurityCodesInputDialog()
    }
    binding?.materialCardViewAddEmailCreateSecretBottomDialog?.setOnClickListener {
      showAddedEmailAccountsInputDialog()
    }
    binding?.materialCardViewAddPhoneNumberCreateSecretBottomDialog?.setOnClickListener {
      showAddedPhoneNumbersInputDialog()
    }
    binding?.materialCardViewAddExtraInformationCreateSecretBottomDialog?.setOnClickListener {
      showExtraInformationInputDialog()
    }
    binding?.buttonCreateSecretBottomDialog?.setOnClickListener {
      val response = secretsViewModel.addNewSecret()
      if (response != "None") {
        Toast.makeText(requireActivity(), response, Toast.LENGTH_LONG).show()
      } else {
        dismiss()
      }
    }

    secretsViewModel.preUploadingSecret = SecretData(
      uid = 0, name = "", description = "", notes = "", userName = "", password = "",
      extraPasswordsOrSecurityCodes = ArrayList(), associatedEmails = ArrayList(),
      usingEmailForTwoFA = false, associatedPhoneNumbers = ArrayList(),
      usingPhoneNumberForTwoFA = false, extras = "", createdAt = Date(), updatedAt = Date(),
      deletedAt = null)
    secretsViewModel.preUploadingSecretCopy = secretsViewModel.preUploadingSecret?.clone()
    setupObservers()
  }

  private fun setupObservers() {
    val totalSecurityCodesObserver = Observer<Int?> {
      if (it == null) { return@Observer }
      val securityCodeDescription = "$it security codes have been added"
      binding?.textViewAddSecurityCodesDescriptionCreateSecretBottomDialog
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

  private fun setDescriptionForEmail(numberOfAssociatedEmail: Int?) {
    val twoFaNotEnabled = "$numberOfAssociatedEmail associations, 2FA is not enabled"
    val twoFaIsEnabled = "$numberOfAssociatedEmail associations, 2FA is enabled"
    if (secretsViewModel.selectedSecret?.usingEmailForTwoFA == true) {
      binding?.textViewAddEmailDescriptionCreateSecretBottomDialog?.text = twoFaIsEnabled
    } else {
      binding?.textViewAddEmailDescriptionCreateSecretBottomDialog?.text = twoFaNotEnabled
    }
  }

  private fun setDescriptionForPhoneNumber(numberOfAssociatedPhoneNumber: Int?) {
    val twoFaPhoneNotEnabled = "$numberOfAssociatedPhoneNumber associations, 2FA is not enabled"
    val twoFaPhoneIsEnabled = "$numberOfAssociatedPhoneNumber associations, 2FA is enabled"
    if (secretsViewModel.selectedSecret?.usingPhoneNumberForTwoFA == true) {
      binding?.textViewAddPhoneNumberCreateSecretBottomDialog?.text = twoFaPhoneIsEnabled
    } else {
      binding?.textViewAddPhoneNumberCreateSecretBottomDialog?.text = twoFaPhoneNotEnabled
    }
  }

  @SuppressLint("InflateParams")
  fun showNameAndDescriptionInputDialog() {
    val nameAndDescriptionInputDialog = LayoutInflater.from(requireActivity()).inflate(
      R.layout.dialog_name_and_description_input, null)
    val textInputName = nameAndDescriptionInputDialog.findViewById<TextInputEditText>(
      R.id.textInputEditTextDialogNameInput)
    val textInputDescription = nameAndDescriptionInputDialog.findViewById<TextInputEditText>(
      R.id.textInputEditTextDialogDescriptionInput)

    lateinit var currentName: String
    lateinit var currentDescription: String

    if (modeFlag == "CREATE") {
      currentName = secretsViewModel.preUploadingSecret?.name.toString()
      currentDescription = secretsViewModel.preUploadingSecret?.description.toString()
    } else if (modeFlag == "UPDATE") {
      currentName = secretsViewModel.selectedSecret?.name.toString()
      currentDescription = secretsViewModel.selectedSecret?.description.toString()
    }

    textInputName.setText(currentName)
    textInputDescription.setText(currentDescription)

    MaterialAlertDialogBuilder(requireActivity())
      .setTitle("Name and Description")
      .setMessage("Set the name and description of the secret")
      .setView(nameAndDescriptionInputDialog)
      .setPositiveButton("SET") { _, _ ->
        val name = textInputName.text.toString()
        val description = textInputDescription.text.toString()

        if (modeFlag == "CREATE") {
          secretsViewModel.preUploadingSecret?.name = name
          secretsViewModel.preUploadingSecret?.description = description
        } else if (modeFlag == "UPDATE") {
          secretsViewModel.selectedSecret?.name = name
          secretsViewModel.selectedSecret?.description = description
        }

      }
      .setNegativeButton("CANCEL") { dialog, _ -> dialog.dismiss() }
      .create().show()
  }

  @SuppressLint("InflateParams")
  fun showNoteInputDialog() {
    val noteInputDialog = LayoutInflater.from(requireActivity()).inflate(
      R.layout.dialog_note_input, null)
    val textInputNote = noteInputDialog.findViewById<TextInputEditText>(
      R.id.textInputEditTextDialogNoteInput)

    lateinit var currentNote: String
    if (modeFlag == "CREATE") {
      currentNote = secretsViewModel.preUploadingSecret?.notes.toString()
    } else if (modeFlag == "UPDATE") {
      currentNote = secretsViewModel.selectedSecret?.notes.toString()
    }

    textInputNote.setText(currentNote)

    MaterialAlertDialogBuilder(requireActivity())
      .setTitle("Note")
      .setMessage("Set a short note for this secret")
      .setView(noteInputDialog)
      .setPositiveButton("SET") { _, _ ->
        val note = textInputNote.text.toString()

        if (modeFlag == "CREATE") {
          secretsViewModel.preUploadingSecret?.notes = note
        } else if (modeFlag == "UPDATE") {
          secretsViewModel.selectedSecret?.notes = note
        }
      }
      .setNegativeButton("CANCEL") { dialog, _ -> dialog.dismiss()}
      .create().show()
  }

  @SuppressLint("InflateParams")
  fun showUsernameAndPasswordInputDialog() {
    val usernameAndPasswordInputDialog = LayoutInflater.from(requireActivity()).inflate(
      R.layout.dialog_username_and_password_input, null)
    val textInputUsername = usernameAndPasswordInputDialog.findViewById<TextInputEditText>(
      R.id.textInputEditTextDialogUsernameInput)
    val textInputPassword = usernameAndPasswordInputDialog.findViewById<TextInputEditText>(
      R.id.textInputEditTextDialogPasswordInput)

    lateinit var currentUsername: String
    lateinit var currentPassword: String

    if (modeFlag == "CREATE") {
      currentUsername = secretsViewModel.preUploadingSecret?.userName.toString()
      currentPassword = secretsViewModel.preUploadingSecret?.password.toString()
    } else if (modeFlag == "UPDATE") {
      currentUsername = secretsViewModel.selectedSecret?.userName.toString()
      currentPassword = secretsViewModel.selectedSecret?.password.toString()
    }

    textInputUsername.setText(currentUsername)
    textInputPassword.setText(currentPassword)

    MaterialAlertDialogBuilder(requireActivity())
      .setTitle("Username and Password")
      .setMessage("Set the username and password of the secret")
      .setView(usernameAndPasswordInputDialog)
      .setPositiveButton("SET") { _, _ ->
        val userName = textInputUsername.text.toString()
        val password = textInputPassword.text.toString()

        if (modeFlag == "CREATE") {
          secretsViewModel.preUploadingSecret?.userName = userName
          secretsViewModel.preUploadingSecret?.password = password
        } else if (modeFlag == "UPDATE") {
          secretsViewModel.selectedSecret?.userName = userName
          secretsViewModel.selectedSecret?.password = password
        }

      }
      .setNegativeButton("CANCEL") { dialog, _ -> dialog.dismiss() }
      .create().show()
  }

  @SuppressLint("InflateParams")
  fun showAddedSecurityCodesInputDialog() {
    val securityCodeInputDialog = LayoutInflater.from(requireActivity()).inflate(
      R.layout.dialog_security_code_input, null)
    val textInputSecurityCode = securityCodeInputDialog.findViewById<TextInputEditText>(
      R.id.textInputEditTextDialogSecurityCodeInput)

    lateinit var addedSecurityCodes: ArrayList<String>

    if (modeFlag == "CREATE") {
      addedSecurityCodes = secretsViewModel.preUploadingSecret?.extraPasswordsOrSecurityCodes!!
    } else if (modeFlag == "UPDATE") {
      addedSecurityCodes = secretsViewModel.selectedSecret?.extraPasswordsOrSecurityCodes!!
    }

    MaterialAlertDialogBuilder(requireActivity())
      .setTitle("Security Codes")
      .setView(securityCodeInputDialog)
      .setPositiveButton("ADD") { _, _ ->
        val securityCode = textInputSecurityCode.text.toString()
        if (modeFlag == "CREATE") {
          secretsViewModel.preUploadingSecret?.extraPasswordsOrSecurityCodes?.add(securityCode)
          val totalCodes = secretsViewModel.preUploadingSecret?.extraPasswordsOrSecurityCodes?.size
          secretsViewModel.getTotalSecurityCodes().value = totalCodes
        } else if (modeFlag == "UPDATE") {
          secretsViewModel.selectedSecret?.extraPasswordsOrSecurityCodes?.add(securityCode)
          val totalCodes = secretsViewModel.selectedSecret?.extraPasswordsOrSecurityCodes?.size
          secretsViewModel.getTotalSecurityCodes().value = totalCodes
        }

      }
      .setNegativeButton("CLEAR ALL") { dialog, _ ->
        if (modeFlag == "CREATE") {
          secretsViewModel.preUploadingSecret?.extraPasswordsOrSecurityCodes?.clear()
        } else if (modeFlag == "UPDATE") {
          secretsViewModel.selectedSecret?.extraPasswordsOrSecurityCodes?.clear()
        }
        secretsViewModel.getTotalSecurityCodes().value = 0
        dialog.dismiss()
      }
      .setItems(addedSecurityCodes.toTypedArray()) { _, _ -> }
      .create().show()
  }

  @SuppressLint("InflateParams")
  fun showAddedEmailAccountsInputDialog() {
    val associatedEmailInputDialog = LayoutInflater.from(requireActivity()).inflate(
      R.layout.dialog_associated_email_input, null)
    val textInputEmail = associatedEmailInputDialog.findViewById<TextInputEditText>(
      R.id.textInputEditTextDialogEmailInput)

    lateinit var addedEmailAccounts: ArrayList<String>

    if (modeFlag == "CREATE") {
      addedEmailAccounts = secretsViewModel.preUploadingSecret?.associatedEmails!!
    } else if (modeFlag == "UPDATE") {
      addedEmailAccounts = secretsViewModel.selectedSecret?.associatedEmails!!
    }

    MaterialAlertDialogBuilder(requireActivity())
      .setTitle("Associated Emails")
      .setView(associatedEmailInputDialog)
      .setPositiveButton("ADD") { _, _ ->
        val email = textInputEmail.text.toString()
        if (modeFlag == "CREATE") {
          secretsViewModel.preUploadingSecret?.associatedEmails?.add(email)
          val totalEmails = secretsViewModel.preUploadingSecret?.associatedEmails?.size
          secretsViewModel.getTotalAssociatedEmails().value = totalEmails
        } else if (modeFlag == "UPDATE") {
          secretsViewModel.selectedSecret?.associatedEmails?.add(email)
          val totalEmails = secretsViewModel.selectedSecret?.associatedEmails?.size
          secretsViewModel.getTotalAssociatedEmails().value = totalEmails
        }
      }
      .setNegativeButton("CLEAR ALL") { dialog, _ ->
        if (modeFlag == "CREATE") {
          secretsViewModel.preUploadingSecret?.associatedEmails?.clear()
        } else if (modeFlag == "UPDATE") {
          secretsViewModel.selectedSecret?.associatedEmails?.clear()
        }
        secretsViewModel.getTotalAssociatedEmails().value = 0
        dialog.dismiss()
      }
      .setItems(addedEmailAccounts.toTypedArray()) { _, _ -> }
      .create().show()
  }

  @SuppressLint("InflateParams")
  fun showAddedPhoneNumbersInputDialog() {
    val associatedPhoneNumberInputDialog = LayoutInflater.from(requireActivity()).inflate(
      R.layout.dialog_associated_phone_number_input, null)
    val textInputPhoneNumber = associatedPhoneNumberInputDialog.findViewById<TextInputEditText>(
      R.id.textInputEditTextDialogPhoneNumberInput)

    lateinit var addedPhoneNumbers: ArrayList<String>

    if (modeFlag == "CREATE") {
      addedPhoneNumbers = secretsViewModel.preUploadingSecret?.associatedPhoneNumbers!!
    } else if (modeFlag == "UPDATE") {
      addedPhoneNumbers = secretsViewModel.selectedSecret?.associatedPhoneNumbers!!
    }

    MaterialAlertDialogBuilder(requireActivity())
      .setTitle("Associated Phone Numbers")
      .setView(associatedPhoneNumberInputDialog)
      .setPositiveButton("ADD") { _, _ ->
        val phoneNumber = textInputPhoneNumber.text.toString()
        if (modeFlag == "CREATE") {
          secretsViewModel.preUploadingSecret?.associatedPhoneNumbers?.add(phoneNumber)
          val totalPhoneNumbers = secretsViewModel.preUploadingSecret?.associatedPhoneNumbers?.size
          secretsViewModel.getTotalAssociatedPhoneNumbers().value = totalPhoneNumbers
        } else if (modeFlag == "UPDATE") {
          secretsViewModel.selectedSecret?.associatedPhoneNumbers?.add(phoneNumber)
          val totalPhoneNumbers = secretsViewModel.selectedSecret?.associatedPhoneNumbers?.size
          secretsViewModel.getTotalAssociatedPhoneNumbers().value = totalPhoneNumbers
        }
      }
      .setNegativeButton("CLEAR ALL") { dialog, _ ->
        if (modeFlag == "CREATE") {
          secretsViewModel.preUploadingSecret?.associatedPhoneNumbers?.clear()
        } else if (modeFlag == "UPDATE") {
          secretsViewModel.selectedSecret?.associatedPhoneNumbers?.clear()
        }
        secretsViewModel.getTotalAssociatedPhoneNumbers().value = 0
        Toast.makeText(requireActivity(), "Deleted all associated phone numbers",
          Toast.LENGTH_LONG).show()
        dialog.dismiss()
      }
      .setItems(addedPhoneNumbers.toTypedArray()) { _, _ -> }
      .create().show()
  }

  @SuppressLint("InflateParams")
  fun showExtraInformationInputDialog() {
    val extraInfoInputDialog = LayoutInflater.from(requireActivity()).inflate(
      R.layout.dialog_extra_info_input, null)
    val textInputExtraInfo = extraInfoInputDialog.findViewById<TextInputEditText>(
      R.id.textInputEditTextDialogExtraInfoInput)

    lateinit var currentExtraInfo: String

    if (modeFlag == "CREATE") {
      currentExtraInfo = secretsViewModel.preUploadingSecret?.extras.toString()
    } else if (modeFlag == "UPDATE") {
      currentExtraInfo = secretsViewModel.selectedSecret?.extras.toString()
    }

    textInputExtraInfo.setText(currentExtraInfo)

    MaterialAlertDialogBuilder(requireActivity())
      .setTitle("Set Extra Information")
      .setView(extraInfoInputDialog)
      .setPositiveButton("SET") { _, _ ->
        val extraInfo = textInputExtraInfo.text.toString()
        if (modeFlag == "CREATE") {
          secretsViewModel.preUploadingSecret?.extras = extraInfo
        } else if (modeFlag == "UPDATE") {
          secretsViewModel.selectedSecret?.extras = extraInfo
        }
      }
      .setNegativeButton("CANCEL") { dialog, _ -> dialog.dismiss() }
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