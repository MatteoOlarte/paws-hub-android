package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.software3.paws_hub_android.model.dal.entity.CityObject

class CityViewModel: ViewModel() {
    val cites = MutableLiveData<List<String>>()


    fun fetchCityData() {
        val cityObject = CityObject()
        cites.value = cityObject.getAll()
    }
}