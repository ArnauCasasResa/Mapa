package com.example.mymapa

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MyViewModel:ViewModel() {
    var listaMarcas= MutableLiveData<MutableList<Marca>>()

    fun addTOList(marca:Marca){
        listaMarcas.value?.add(marca)
    }
}