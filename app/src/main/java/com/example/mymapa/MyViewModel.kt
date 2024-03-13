package com.example.mymapa

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MyViewModel:ViewModel() {

    private val _marcaActual=MutableLiveData(Marca("ITB",LatLng(41.4534265,2.1837151),"Inst Tecnologic de Bcn"))
    var marcaActual=_marcaActual
    var _listaMarcas= MutableLiveData(mutableListOf<Marca>(Marca("ITB",LatLng(41.4534265,2.1837151),"Inst Tecnologic de Bcn")))
    var listaMarcas=_listaMarcas
    fun addTOList(marca:Marca){
        if (marca !in _listaMarcas.value!!){
            _listaMarcas.value?.add(marca)
        }
    }
    fun deleteTOList(marca: Marca){
        val currentList=_listaMarcas.value.orEmpty().toMutableList()
        currentList.remove(marca)
        _listaMarcas.value=currentList
    }
    fun changeActual(marca: Marca){
        marcaActual.value=marca
    }
}