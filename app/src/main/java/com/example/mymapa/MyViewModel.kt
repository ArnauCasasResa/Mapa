package com.example.mymapa

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymapa.FireBase.Repository
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel:ViewModel() {

    private val _marcaActual=MutableLiveData(Marca("ITB",LatLng(41.4534265,2.1837151),"Inst Tecnologic de Bcn"))
    var marcaActual=_marcaActual
    private val _imagenActual=MutableLiveData(Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888))
    var imagenActual=_imagenActual
    private var _listaMarcas= MutableLiveData(mutableListOf<Marca>())
    var listaMarcas=_listaMarcas
    private var _showImage=MutableLiveData(false)
    var showImage=_showImage
    private var _showOptions=MutableLiveData(false)
    var showOptions=_showOptions
    private var _inicioPantall=MutableLiveData(true)
    var inicioPantall=_inicioPantall
    private val _loading = MutableLiveData(true)
    val loading = _loading
    private val repository = Repository()

    fun addTOList(marca:Marca){
        repository.addMarker(marca)
    }
    fun deleteTOList(marca: Marca){
        val currentList=_listaMarcas.value.orEmpty().toMutableList()
        currentList.remove(marca)
        _listaMarcas.value=currentList
        repository.removeMarker(marca)
    }
    fun changeActual(marca: Marca){
        marcaActual.value=marca
    }
    fun changeImagenActual(imagen: Bitmap){
        imagenActual.value=imagen
    }
    fun turnFalseImage(){
        _showImage.value=false
    }
    fun turnFalseOptions(){
        _showOptions.value=false
    }
    fun turnTrueImage(){
        _showImage.value=true
    }
    fun turnTrueOptions(){
        _showOptions.value=true
    }
    fun saveChanges(nomMarca:String,descripcioMarca:String){
        this._marcaActual.value?.nombre=nomMarca
        this._marcaActual.value?.descripcion=descripcioMarca
    }
    fun addImage(imagen: Bitmap){
        _marcaActual.value?.imagenes?.apply {
            add(imagen)
        }
    }
    fun editMarker(){
        repository.editMarker(_marcaActual.value!!)
    }
    fun inicio(){
        _inicioPantall.value=false
    }
    fun getMarkers() {
        repository.getMarkersFromDataBase().addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error != null){
                    Log.e("Firestore error", error.message.toString())
                    return
                }
                val tempList = mutableListOf<Marca>()
                for(dc: DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        val document = dc.document
                        val nombre = document.getString("nombre") ?: ""
                        val descripcion = document.getString("descripcion") ?: ""
                        val ubicacionMap = document.get("ubicacion") as? Map<String, Double>
                        val ubicacion = LatLng(ubicacionMap?.get("latitude") ?: 0.0, ubicacionMap?.get("longitude") ?: 0.0)
                        val tipo = document.getString("tipo") ?: ""
                        val imagenes = document.get("imagenes") as? MutableList<Bitmap> ?: mutableListOf<Bitmap>()

                        val newMark = Marca(nombre, ubicacion, descripcion)
                        newMark.tipo=tipo
                        newMark.imagenes=imagenes
                        tempList.add(newMark)
                    }
                }
                _listaMarcas.value = tempList
            }
        })
    }
}