package com.software3.paws_hub_android.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.software3.paws_hub_android.databinding.ActivityEditProfileBinding
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.viewmodel.EditProfileViewModel
import com.software3.paws_hub_android.viewmodel.UserViewModel


class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val profileViewModel: EditProfileViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(this.layoutInflater)
        initUI()
        initObservers()
        userViewModel.fetchUserData()
    }

    private fun initUI() {
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.confirmButton.setOnClickListener {
            with(binding) {
                profileViewModel.updateUser(UserData(
                    fName = userFirstNameInput.text.toString(),
                    lName = userLastNameInput.text.toString(),
                    uName = userNameInput.text.toString(),
                    email = userEmailInput.text.toString(),
                    phoneNumber = userPhoneNumberInput.text.toString(),
                    city = userCityInput.text.toString())
                )
            }
        }
    }

    private fun initObservers() {
        userViewModel.userdata.observe(this) { userdata ->
            userdata?.let(::updateTextFields)
        }
        profileViewModel.transactionState.observe(this) {
            Toast.makeText(this, "Update Status ${it.name}", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateTextFields(data: UserData) {
        //falta cargar foto de perfil
        binding.userFirstNameInput.setText(data.fName)
        binding.userLastNameInput.setText(data.lName)
        binding.userEmailInput.setText(data.email)
        binding.userPhoneNumberInput.setText(data.phoneNumber)
        binding.userNameInput.setText(data.uName)
        binding.userCityInput.setText(data.city)
    }
}