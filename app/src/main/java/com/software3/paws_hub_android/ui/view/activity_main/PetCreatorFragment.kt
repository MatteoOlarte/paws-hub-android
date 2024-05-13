package com.software3.paws_hub_android.ui.view.activity_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.core.ex.getStringResource
import com.software3.paws_hub_android.databinding.FragmentPetPublishBinding
import com.software3.paws_hub_android.model.PetBreed
import com.software3.paws_hub_android.model.PetPublish
import com.software3.paws_hub_android.model.PetType
import com.software3.paws_hub_android.viewmodel.MainActivityViewModel
import com.software3.paws_hub_android.viewmodel.PetCreatorViewModel
import com.software3.paws_hub_android.ui.viewstate.PetCreatorViewState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


class PetCreatorFragment : Fragment() {
    private var _biding: FragmentPetPublishBinding? = null
    private val binding get() = _biding!!
    private val activityViewmodel: MainActivityViewModel by activityViewModels()
    private val viewmodel: PetCreatorViewModel by viewModels()
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
        viewmodel.fetchPetTypes()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _biding = null
    }

    private fun initUI() {
        binding.tfLayoutPetType.isEnabled = false
        binding.tfLayoutPetBreed.isEnabled = false
        binding.btnPublish.isEnabled = false
    }

    private fun initObservers() {
        viewmodel.birthdate.observe(viewLifecycleOwner) {
            binding.tfPetAge.setText(SimpleDateFormat.getDateInstance().format(it))
        }
        viewmodel.petTypes.observe(viewLifecycleOwner) {
            binding.tfLayoutPetType.isEnabled = true
            setTfPetTypeItems(it)
        }
        viewmodel.petBreeds.observe(viewLifecycleOwner) {
            binding.tfLayoutPetBreed.isEnabled = true
            setTfPetBreedItems(it)
        }
        viewmodel.allowOtherTypes.observe(viewLifecycleOwner) {
            binding.tfLayoutAlternativePetType.visibility = if (it) View.VISIBLE else View.GONE
            binding.tfLayoutPetBreed.isVisible = !it
        }
        viewmodel.transactionState.observe(viewLifecycleOwner) {
            when (it) {
                TransactionState.PENDING -> onPendingState()
                TransactionState.SUCCESS -> onSuccessState()
                TransactionState.FAILED -> onFailureState()
                else -> onFailureState()
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.viewState.collect { updateUI(it) }
            }
        }
    }

    private fun initListeners() {
        binding.tfLayoutPetAge.setEndIconOnClickListener {
            datePicker.show(parentFragmentManager, "tag")
        }
        datePicker.addOnPositiveButtonClickListener {
            viewmodel.setBirthDate(Date(it))
        }
        binding.tfPetType.setOnItemClickListener { _, _, position, _ ->
            viewmodel.setPetType(position)
            binding.tfPetBreed.text = null
            viewmodel.onPetTypeFieldChanged()
        }
        binding.tfPetBreed.setOnItemClickListener { _, _, position, _ ->
            viewmodel.setPetBreed(position)
            viewmodel.onPetBreedFieldChanged()
        }
        binding.tfPetName.doOnTextChanged { _, _, _, _ ->
            viewmodel.onNameFieldChanged(binding.tfPetName.text.toString())
        }
        binding.tfPetAge.doOnTextChanged { _, _, _, _ ->
            viewmodel.onDateFieldChanged(binding.tfPetAge.text.toString())
        }
        binding.tfPetWeight.doOnTextChanged { _, _, _, _ ->
            viewmodel.onWeightFieldChanged(binding.tfPetWeight.text.toString())
        }
        binding.tfPetAlternativePetType.doOnTextChanged { _, _, _, _ ->
            viewmodel.onPetAlternativeBreedFieldChanged(binding.tfPetAlternativePetType.text.toString())
        }
        binding.btnPublish.setOnClickListener {
            onPublish()
        }
    }

    private fun updateUI(viewState: PetCreatorViewState) {
        with(binding) {
            binding.btnPublish.isEnabled = viewState.isFormValid
            tfLayoutPetName.error =
                if (viewState.isValidName == true || viewState.isValidName == null) null else "error!"
            tfLayoutPetAge.error =
                if (viewState.isValidBirthDate == true || viewState.isValidBirthDate == null) null else "error!"
            tfLayoutPetWeight.error =
                if (viewState.isValidWeight == true || viewState.isValidWeight == null) null else "error!"
            tfLayoutPetType.error =
                if (viewState.isValidPetType == true || viewState.isValidPetType == null) null else "error!"
            tfLayoutPetBreed.error =
                if (viewState.isValidPetBreed == true || viewState.isValidPetBreed == null) null else "error!"
        }
    }

    private fun setTfPetTypeItems(items: List<PetType>) {
        val array = items.map { it.typeID }.map { it.getStringResource(requireContext()) }.toTypedArray()
        val adapter = ArrayAdapter(requireContext(), R.layout.layout_list_adapter, array)
        with(binding) {
            tfPetType.setAdapter(adapter)
        }
    }

    private fun setTfPetBreedItems(items: List<PetBreed>) {
        val array = items.map { it.breedName }.toTypedArray()
        val adapter = ArrayAdapter(requireContext(), R.layout.layout_list_adapter, array)
        with(binding) {
            tfPetBreed.setAdapter(adapter)
        }
    }

    private fun onPublish() {
        val petPublish = PetPublish(
            name = binding.tfPetName.text.toString(),
            weight = binding.tfPetWeight.text.toString().toFloatOrNull() ?: 0f,
            breed = binding.tfPetBreed.text.toString(),
            notes = binding.tfPetNotes.text.toString(),
            alternativeBreed = binding.tfPetAlternativePetType.text.toString()
        )
        viewmodel.createPet(petPublish)
    }

    private fun onSuccessState() {
        val msg = getString(R.string.pet_upload_success_message)
        activityViewmodel.stopProgressIndicator()
        activityViewmodel.createSimpleSnackbarMessage(msg)
        activityViewmodel.fetchUserdata()
        findNavController().navigateUp()
    }

    private fun onPendingState() {
        activityViewmodel.startProgressIndicator()
    }

    private fun onFailureState() {
        val msg = getString(R.string.pet_upload_failure_message)
        activityViewmodel.stopProgressIndicator()
        activityViewmodel.createSimpleSnackbarMessage(msg)
    }
}