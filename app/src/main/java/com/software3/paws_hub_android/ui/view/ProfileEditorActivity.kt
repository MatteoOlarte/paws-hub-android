package com.software3.paws_hub_android.ui.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.databinding.ActivityEditProfileBinding
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.viewmodel.ProfileEditorViewModel
import com.software3.paws_hub_android.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso


class ProfileEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val profileViewModel: ProfileEditorViewModel by viewModels()
    private val userViewModel: ProfileViewModel by viewModels()
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        profileViewModel.setPhotoURL(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(this.layoutInflater)
        initUI()
        initObservers()
        initListeners()
        userViewModel.fetchProfileData()
        profileViewModel.fetchCityData()
    }

    private fun initUI() {
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initObservers() {
        userViewModel.profile.observe(this) {
            it?.let(::updateUI)
            profileViewModel.setUserdata(it)
        }
        profileViewModel.userPhotoURI.observe(this) {
            Picasso.get().load(it).into(binding.profileImage)
        }
        profileViewModel.isNameAvailable.observe(this) {
            if (it) {
                binding.tfLayoutUsername.error = null
                return@observe
            }
            val msg = getString(R.string.username_taken_error)
            binding.tfLayoutUsername.error = msg
        }
        profileViewModel.updateState.observe(this) {
            when (it) {
                TransactionState.PENDING -> onPendingState()
                TransactionState.SUCCESS -> onSuccessState()
                TransactionState.FAILED -> onFailureState()
                else -> onFailureState()
            }
        }
        profileViewModel.citiesList.observe(this) {
            val adapter = ArrayAdapter(this, R.layout.layout_list_adapter, it)
            with(binding) {
                if (tfCity is MaterialAutoCompleteTextView) {
                    tfCity.setAdapter(adapter)
                }
            }
        }
    }

    private fun initListeners() {
        binding.editPhotoButton.setOnClickListener { imageResult.launch("image/*") }
        binding.confirmButton.setOnClickListener { onConfirmButtonClick() }
        binding.tfUsername.doOnTextChanged { text, _, _, _ ->
            profileViewModel.checkUsername(text.toString())
        }
        binding.tfCity.setOnFocusChangeListener { _, _ -> binding.tfCity.text = null }
    }

    private fun updateUI(data: Profile) {
        profileViewModel.setPhotoURL(data.photo)
        binding.tfFirstName.setText(data.fName)
        binding.tfLastName.setText(data.lName)
        binding.tfEmail.setText(data.email)
        binding.tfPhoneNumber.setText(data.phoneNumber)
        binding.tfUsername.setText(data.uName)
        binding.tfPreferredPet.setText(data.preferredPet)
        binding.tfCity.setText(data.city)
    }

    private fun onSuccessState() {
        val msg = getString(R.string.profile_updated_success)
        binding.toolbarProgressIndicator.visibility = View.GONE
        enableFieldsAndButtons(true)
        Snackbar.make(this, binding.coordinatorLayout, msg, Snackbar.LENGTH_SHORT).also {
            it.setAction("OK") { _ ->

                it.dismiss()
            }
            it.show()
        }
    }

    private fun onPendingState() {
        binding.toolbarProgressIndicator.visibility = View.VISIBLE
        enableFieldsAndButtons(false)
    }

    private fun onFailureState() {
        val msg = getString(R.string.profile_update_error)
        binding.toolbarProgressIndicator.visibility = View.GONE
        enableFieldsAndButtons(true)
        Snackbar.make(this, binding.coordinatorLayout, msg, Snackbar.LENGTH_SHORT).also {
            it.setAction("OK") { _ -> it.dismiss() }
            it.show()
        }
    }

    private fun onConfirmButtonClick() {
        with(binding) {
            profileViewModel.saveProfile(
                fName = tfFirstName.text.toString(),
                lName = tfLastName.text.toString(),
                uName = tfUsername.text.toString(),
                email = tfEmail.text.toString(),
                city = tfCity.text.toString(),
                phone = tfPhoneNumber.text.toString(),
                preferredPet = tfPreferredPet.text.toString().lowercase()
            )
        }
    }

    private fun enableFieldsAndButtons(isEnabled: Boolean) {
        with(binding) {
            tfFirstName.isEnabled = isEnabled
            tfLastName.isEnabled = isEnabled
            //userEmailInput.isEnabled = isEnabled
            tfPhoneNumber.isEnabled = isEnabled
            tfUsername.isEnabled = isEnabled
            tfCity.isEnabled = isEnabled
            confirmButton.isEnabled = isEnabled
            editPhotoButton.isEnabled = isEnabled
        }
    }
}