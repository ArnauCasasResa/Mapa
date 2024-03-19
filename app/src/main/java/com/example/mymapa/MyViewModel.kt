package com.example.mymapa

import android.graphics.Bitmap
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MyViewModel:ViewModel() {

    private val _marcaActual=MutableLiveData(Marca("ITB",LatLng(41.4534265,2.1837151),"Inst Tecnologic de Bcn"))
    var marcaActual=_marcaActual
    private val _imagenActual=MutableLiveData(Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888))
    var imagenActual=_imagenActual
    private var _listaMarcas= MutableLiveData(mutableListOf<Marca>(Marca("ITB",LatLng(41.4534265,2.1837151),"Inst Tecnologic de Bcn")))
    var listaMarcas=_listaMarcas
    private var _show=MutableLiveData(false)
    var show=_show
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
    fun changeImagenActual(imagen: Bitmap){
        imagenActual.value=imagen
    }
    fun turnFalse(){
        _show.value=false
    }
    fun turnTrue(){
        _show.value=true
    }
    fun saveChanges(nomMarca:String,descripcioMarca:String){
        this._marcaActual.value?.nombre=nomMarca
        this._marcaActual.value?.descripcion=descripcioMarca
    }
}