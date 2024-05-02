package com.software3.paws_hub_android.view.main_activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.databinding.FragmentPetPublishBinding
import com.software3.paws_hub_android.model.PetPublish
import com.software3.paws_hub_android.viewmodel.PetUploadViewModel
import java.text.SimpleDateFormat
import java.util.Date


class PetPublishFragment : Fragment() {
    private var _biding: FragmentPetPublishBinding? = null
    private val binding get() = _biding!!
    private val viewmodel: PetUploadViewModel by viewModels()
    private val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(R.string.question_pet_age)
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _biding = FragmentPetPublishBinding.inflate(inflater, container, false)
        initUI()
        initObservers()
        initListeners()
        viewmodel.fetchTypes()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _biding = null
    }

    private fun initUI() {
        binding.tfPetBreed.isEnabled = false
    }

    private fun initObservers() {
        viewmodel.breeds.observe(viewLifecycleOwner) {
            setTfPetBreedItems(it.breeds)
            binding.tfPetBreed.isEnabled = true
        }
        viewmodel.birthdate.observe(viewLifecycleOwner) {
            binding.tfPetAge.setText(SimpleDateFormat.getDateInstance().format(it))
        }
        viewmodel.types.observe(viewLifecycleOwner) {
            setTfPetTypeItems(R.array.pets_types)
        }
        viewmodel.state.observe(viewLifecycleOwner) {
            when (it) {
                TransactionState.PENDING -> onPendingState()
                TransactionState.SUCCESS -> onSuccessState()
                TransactionState.FAILED -> onFailureState()
                else -> onFailureState()
            }
        }
    }

    private fun initListeners() {
        datePicker.addOnPositiveButtonClickListener { viewmodel.setBirthDate(Date(it)) }
        binding.tfLayoutPetAge.setEndIconOnClickListener {
            datePicker.show(parentFragmentManager, "tag")
        }
        binding.tfPetType.setOnItemClickListener { _, _, position, _ ->
            viewmodel.setPetType(position)
            binding.tfPetBreed.text = null
        }
        binding.btnPublish.setOnClickListener { onPetPublish() }
    }

    private fun onPetPublish() {
        val petPublish = PetPublish(
            name =  binding.tfPetName.text.toString(),
            weight = binding.tfPetWeight.text.toString().toFloat(),
            breed = binding.tfPetBreed.text.toString(),
            notes = binding.tfPetNotes.text.toString()
        )
        viewmodel.publishPet(petPublish)
    }

    private fun setTfPetBreedItems(items: List<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.layout_list_adapter, items)
        with(binding) {
            if (tfPetBreed is MaterialAutoCompleteTextView) tfPetBreed.setAdapter(adapter)
        }
    }

    private fun setTfPetTypeItems(resource: Int) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            resource,
            R.layout.layout_list_adapter
        )
        with(binding) {
            if (tfPetType is MaterialAutoCompleteTextView) tfPetType.setAdapter(adapter)
        }
    }

    private fun onSuccessState() {
        Toast.makeText(this.context, "SAVED", Toast.LENGTH_LONG).show()
    }

    private fun onPendingState() {
        Toast.makeText(this.context, "PENDING", Toast.LENGTH_LONG).show()
    }

    private fun onFailureState() {
        Toast.makeText(this.context, "FAILED", Toast.LENGTH_LONG).show()
    }
}