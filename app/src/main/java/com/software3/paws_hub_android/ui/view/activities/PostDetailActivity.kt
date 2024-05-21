package com.software3.paws_hub_android.ui.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.Menu
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.core.ex.getStringResource
import com.software3.paws_hub_android.core.ex.yearsSince
import com.software3.paws_hub_android.core.util.PhoneFormat
import com.software3.paws_hub_android.databinding.ActivityPostDetailBinding
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.Post
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.ui.viewstate.TransactionViewState
import com.software3.paws_hub_android.viewmodel.PostDetailsViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding
    private lateinit var picasso: Picasso
    private val viewModel: PostDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val postID = intent.getStringExtra("postID")

        if (postID != null) {
            binding = ActivityPostDetailBinding.inflate(layoutInflater)
            picasso = Picasso.get()
            initUI()
            initObservers()
            initListeners()
            viewModel.fetchPostByID(postID)
        } else {
            navigateUpTo(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_post_detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initUI() {
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<MarginLayoutParams> { bottomMargin = systemBars.bottom }
            insets
        }
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initObservers() {
        viewModel.post.observe(this) { post ->
            val petID = post.pet["_id"]
            //Toast.makeText(this, petID, Toast.LENGTH_SHORT).show()
            onPostLoaded(post)
            petID?.let { viewModel.fetchPetByID(it) }
        }
        viewModel.pet.observe(this) { pet ->
            onPetLoaded(pet)
            viewModel.fetchOwner(pet)
        }
        viewModel.ownerProfile.observe(this) {profile ->
            profile?.let { onOwnerLoaded(it) }
        }
    }

    private fun initListeners() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { updateUI(it) }
            }
        }
    }

    private fun onPostLoaded(post: Post) {
        with(binding.layoutBody) {
            tvPostBody.text = post.body
            picasso.load(post.image).into(imgPostPhoto)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onPetLoaded(pet: Pet) {
        val format = SimpleDateFormat.getDateInstance()
        val years = "${pet.birthDate.yearsSince()} ${getString(R.string.years_old)}"
        val type: String = if (pet.typeID == "type_other") {
            "${pet.breed?.get("name")}"
        } else {
            "${pet.typeID?.getStringResource(this@PostDetailActivity)}, ${pet.breed?.get("name")}"
        }

        this.supportActionBar?.title = pet.name
        this.supportActionBar?.subtitle = type
        with(binding.layoutBody) {
            tvPetBirthDate.text = "${format.format(pet.birthDate)} ($years)"
            tvPetWeight.text = "${pet.weight} KG"
            tvPetType.text = type
            if (pet.notes.isNullOrEmpty()) {
                tvPetNotes.visibility = View.GONE
                tvNotesLabel.visibility = View.GONE
            } else {
                tvPetNotes.text = pet.notes
            }
        }
    }

    private fun onOwnerLoaded(profile: Profile) {
        val phoneNumber = if (profile.phoneNumber != null) {
            profile.phoneNumber?.let { PhoneFormat().format(it) }
        } else {
            PhoneFormat().format("0000000000")
        }
        with(binding.layoutBody) {
            tvUserFullName.text = profile.fullName
            tvUserPhoneNumber.text = phoneNumber
        }
    }

    private fun updateUI(viewState: TransactionViewState) {
        if (viewState.isSuccess) {
            onSuccess()
            return
        }
        if (viewState.isPending) {
            onPending()
            return
        }
        if (viewState.isFailure) {
            onFailure(viewState.error!!)
            return
        }
    }

    private fun onSuccess() {
        binding.toolbarProgressIndicator.visibility = View.GONE
    }

    private fun onPending() {
        binding.toolbarProgressIndicator.visibility = View.VISIBLE
    }

    private fun onFailure(error: String) {
        binding.toolbarProgressIndicator.visibility = View.GONE

    }
}