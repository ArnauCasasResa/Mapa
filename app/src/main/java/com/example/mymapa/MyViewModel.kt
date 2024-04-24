package com.example.mymapa

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymapa.Clases.Marca
import com.example.mymapa.FireBase.Repository
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot

class MyViewModel:ViewModel() {

    private val _marcaActual=MutableLiveData(Marca("ITB",LatLng(41.4534265,2.1837151),"Inst Tecnologic de Bcn","Escuela",""))
    var marcaActual=_marcaActual
    private val _imagenActual=MutableLiveData("")
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

    val _loggedIn = MutableLiveData(false)

    var primeraVez = true

    fun addTOList(marca: Marca){
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
    fun changeImagenActual(imagen: String){
        imagenActual.value=imagen
    }
    fun turnFalseImage(){
        _showImage.value=false
    }
    fun turnTrueImage(){
        _showImage.value=true
    }
    fun saveChanges(nomMarca:String,descripcioMarca:String,tipo:String){
        this._marcaActual.value?.nombre=nomMarca
        this._marcaActual.value?.descripcion=descripcioMarca
        this._marcaActual.value?.tipo=tipo
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
                        val imagenes = document.get("imagenes") as? MutableList<String> ?: mutableListOf<String>()
                        val id = document.getString("id") ?: ""
                        val usuario =document.getString("usuario") ?: ""

                        val newMark = Marca(nombre, ubicacion, descripcion,tipo,id)
                        newMark.imagenes=imagenes
                        newMark.usuario=usuario
                        tempList.add(newMark)
                    }
                }
                val filtredList = mutableListOf<Marca>()
                for (marca in tempList){
                    if(marca.usuario == _userId.value){
                        filtredList.add(marca)
                    }
                }
                _listaMarcas.value = filtredList
            }
        })
    }
    fun uploadImage(image: Uri){
        repository.uploadImage(image,_marcaActual.value!!)
    }


    private val auth = FirebaseAuth.getInstance()
    var _goToNext= MutableLiveData<Boolean>()
    var _userId= MutableLiveData<String>()
    var _loggedUser= MutableLiveData<String>()
    fun register(username:String,passwrd:String){
        auth.createUserWithEmailAndPassword(username,passwrd)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    _goToNext.value=true
                }else{
                    _goToNext.value=false
                }
            }
    }

    fun login(username:String?,passwrd:String?){
        auth.signInWithEmailAndPassword(username!!,passwrd!!)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    _userId.value= task.result.user?.uid
                    _loggedUser.value = task.result.user?.email?.split("@")?.get(0)
                    _goToNext.value=true
                    _loggedIn.value=true
                }else{
                    _goToNext.value=false
                    _loggedIn.value=false
                }
            }
    }
    fun deleteImage(image: String){
        repository.deleteImage(image,_marcaActual.value!!)
    }
    fun logOut(){
        auth.signOut()
    }
    fun log(p:Boolean){
        _loggedIn.value=p
    }
}