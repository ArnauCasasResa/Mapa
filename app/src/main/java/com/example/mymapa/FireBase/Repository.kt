package com.example.mymapa.FireBase

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mymapa.Clases.Marca
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Repository {
    private val database = FirebaseFirestore.getInstance()

    fun addMarker(marker: Marca) {
        database.collection("markers")
            .add(
                hashMapOf(
                    "id" to marker.id,
                    "nombre" to marker.nombre,
                    "ubicacion" to hashMapOf(
                        "latitude" to marker.ubicacion.latitude,
                        "longitude" to marker.ubicacion.longitude
                    ),
                    "descripcion" to marker.descripcion,
                    "tipo" to marker.tipo,
                    "imagenes" to marker.imagenes,
                    "usuario" to marker.usuario
                )
            )
    }

    fun getMarkersFromDataBase(): CollectionReference {
        return database.collection("markers")
    }

    fun removeMarker(marker: Marca) {
        database.collection("markers")
            .whereEqualTo("id", marker.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    database.collection("markers").document(document.id)
                        .delete()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }
    }

    fun editMarker(marker: Marca) {
        database.collection("markers")
            .whereEqualTo("id", marker.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    database.collection("markers").document(document.id)
                        .update(
                            hashMapOf(
                                "id" to marker.id,
                                "nombre" to marker.nombre,
                                "ubicacion" to hashMapOf(
                                    "latitude" to marker.ubicacion.latitude,
                                    "longitude" to marker.ubicacion.longitude
                                ),
                                "descripcion" to marker.descripcion,
                                "tipo" to marker.tipo,
                                "imagenes" to marker.imagenes
                            )
                        )
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }
    }

    fun deleteImage(image: String, marca: Marca) {
        val storage = FirebaseStorage.getInstance().getReferenceFromUrl(image)
        storage.delete()
            .addOnSuccessListener {
                marca.imagenes.remove(image)
                editMarker(marca)
            }
            .addOnFailureListener { Log.e("Image delete", "Image delete failed") }
    }

    fun uploadImage(imageUri: Uri, marca: Marca) {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("images/$fileName")
        storage.putFile(imageUri)
            .addOnSuccessListener {
                Log.i("Image upload", "Image uploaded correctly")
                storage.downloadUrl.addOnSuccessListener {
                    marca.imagenes.add(it.toString())
                    editMarker(marca)
                    Log.i("Image upload", it.toString())
                }
                    .addOnFailureListener { Log.e("Image upload", "Image upload failed") }
            }
    }
}