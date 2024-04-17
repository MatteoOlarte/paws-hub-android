package com.software3.paws_hub_android.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.FragmentProfileBinding
import com.software3.paws_hub_android.viewmodel.UserViewModel


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var param1: String? = null
    private var param2: String? = null
    private val userViewModel: UserViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        userViewModel.userData.observe(viewLifecycleOwner) {
            it?.let { user ->
                binding.profileFragmentFullName.text = "${user.fName} ${user.lName}"
                binding.profileFragmentUserEmail.text = user.uName
                binding.profileFragmentUserEmail.text = user.email
            }
        }
        userViewModel.fetchUserData()
        return binding.root
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}