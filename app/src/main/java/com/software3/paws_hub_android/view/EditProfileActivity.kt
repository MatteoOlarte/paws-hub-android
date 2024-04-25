package com.software3.paws_hub_android.view

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.databinding.ActivityEditProfileBinding
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.viewmodel.CityViewModel
import com.software3.paws_hub_android.viewmodel.EditProfileViewModel
import com.software3.paws_hub_android.viewmodel.UserViewModel
import com.squareup.picasso.Picasso


class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val profileViewModel: EditProfileViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val cityViewModel: CityViewModel by viewModels()
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        profileViewModel.setProfilePhotoURI(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(this.layoutInflater)
        initUI()
        initObservers()
        initListeners()
        userViewModel.fetchUserData()
        cityViewModel.fetchCityData()
    }

    private fun initUI() {
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initObservers() {
        userViewModel.userdata.observe(this) { userdata ->
            userdata?.let(::updateTextFields)
        }
        profileViewModel.photoURI.observe(this) {
            binding.profileImage.setImageURI(it)
        }
        profileViewModel.state.observe(this) {
            when (it) {
                TransactionState.PENDING -> onPendingState()
                TransactionState.SUCCESS -> onSuccessState()
                TransactionState.FAILED -> onFailureState()
                else -> onFailureState()
            }
        }
        cityViewModel.cites.observe(this) {
            with(binding) {
                if (userCityInput is MaterialAutoCompleteTextView) {
                    userCityInput.setSimpleItems(it.toTypedArray())
                }
            }
        }
    }

    private fun initListeners() {
        binding.editPhotoButton.setOnClickListener { imageResult.launch("image/*") }
        binding.confirmButton.setOnClickListener { updateProfileFromData() }
    }

    private fun updateProfileFromData() {
        with(binding) {
            profileViewModel.updateProfile(
                fName = userFirstNameInput.text.toString(),
                lName = userLastNameInput.text.toString(),
                uName = userNameInput.text.toString(),
                email = userEmailInput.text.toString(),
                city = userCityInput.text.toString(),
                phone = userPhoneNumberInput.text.toString()
            )
        }
    }

    private fun updateTextFields(data: UserData) {
        profileViewModel.setProfilePhotoURI(data.photo)
        Picasso.get().load(data.photo).into(binding.profileImage)
        binding.userFirstNameInput.setText(data.fName)
        binding.userLastNameInput.setText(data.lName)
        binding.userEmailInput.setText(data.email)
        binding.userPhoneNumberInput.setText(data.phoneNumber)
        binding.userNameInput.setText(data.uName)
        binding.userCityInput.setText(data.city)
    }

    private fun enableFieldsAndButtons(isEnabled: Boolean) {
        with(binding) {
            userFirstNameInput.isEnabled = isEnabled
            userLastNameInput.isEnabled = isEnabled
            userEmailInput.isEnabled = isEnabled
            userPhoneNumberInput.isEnabled = isEnabled
            userNameInput.isEnabled = isEnabled
            userCityInput.isEnabled = isEnabled
            confirmButton.isEnabled = isEnabled
            editPhotoButton.isEnabled = isEnabled
        }
    }

    private fun onPendingState() {
        binding.toolbarProgressIndicator.visibility = View.VISIBLE
        enableFieldsAndButtons(false)
    }

    private fun onSuccessState() {
        val msg = getString(R.string.profile_updated_success)
        binding.toolbarProgressIndicator.visibility = View.GONE
        enableFieldsAndButtons(true)
        Snackbar.make(this, binding.coordinatorLayout, msg, Snackbar.LENGTH_SHORT).also {
            it.setAction("OK") { _ -> it.dismiss() }
            it.show()
        }
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
}