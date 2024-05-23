package com.software3.paws_hub_android.core.ex

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.PetBreed
import com.software3.paws_hub_android.model.PetType
import com.software3.paws_hub_android.model.Post
import com.software3.paws_hub_android.model.Profile


fun DocumentSnapshot.toPost(): Post {
    val author = this.get("author").toMapOrEmpty<String>()
    val pet = this.get("pet").toMapOrEmpty<String>()
    val post = Post(
        postID = this.get("_id") as String,
        image = (this.get("image_url") as String).toURI(),
        body = this.get("post_body") as String,
        created = (this.get("pub_date") as Timestamp).toDate(),
        author = author,
        pet = pet,
        type = this.get("type") as String?,
        location = this.get("last_location") as String?
    )
    return post
}

fun DocumentSnapshot.toProfile(): Profile = Profile(
    profileID = this.id,
    fName = this.get("first_name") as String,
    lName = this.get("last_name") as String,
    uName = this.get("user_name") as String,
    email = this.get("email") as String?,
    city = this.get("city") as String?,
    photo = (this.get("profile_photo") as String?)?.toURI(),
    phoneNumber = this.get("phone_number") as String?,
    preferredPet = this.get("preferred_pet") as String?,
    pets = this.get("pets").toMutableListOrEmpty<String>()
)

fun DocumentSnapshot.toPet(): Pet {
    val breed = this.get("breed").toMapOrEmpty<String>()
    val pet = Pet(
        petID = this.get("_id") as String,
        name = this.get("name") as String,
        birthDate = (this.get("birth_date") as Timestamp).toDate(),
        weight = (this.get("weight") as Double).toFloat(),
        typeID = this.get("type") as String?,
        breed = breed,
        notes = this.get("notes") as String?,
        ownerID = this.get("owner_id") as String
    )
    return pet
}

fun DocumentSnapshot.toPetType(): PetType = PetType(
    typeID = this.get("_id") as String
)

fun DocumentSnapshot.toPetBreed(): PetBreed = PetBreed(
    breedID = this.get("_id") as String,
    breedName = this.get("breed_name") as String,
    typeID = this.get("type_id") as String
)