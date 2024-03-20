package com.example.mymapa.FireBase

import android.util.Log
import com.example.mymapa.Marca
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.util.EventListener

class Repository {
    val database=FirebaseFirestore.getInstance()

    fun addMarker(marker: Marca){
        database.collection("markers")
            .add(hashMapOf(
                "nombre" to marker.nombre,
                "ubicacion" to hashMapOf(
                    "latitude" to marker.ubicacion.latitude,
                    "longitude" to marker.ubicacion.longitude
                ),
                "descripcion" to marker.descripcion,
                "tipo" to marker.tipo,
                "imagenes" to marker.imagenes
            ))
    }
    fun getMarkersFromDataBase(): CollectionReference {
        return database.collection("markers")
    }
    fun editMarker(marker: Marca){
        database.collection("markers")
            .whereEqualTo("nombre",marker.nombre)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    database.collection("markers").document(document.id)
                        .update(hashMapOf(
                            "nombre" to marker.nombre,
                            "ubicacion" to hashMapOf(
                                "latitude" to marker.ubicacion.latitude,
                                "longitude" to marker.ubicacion.longitude
                            ),
                            "descripcion" to marker.descripcion,
                            "tipo" to marker.tipo,
                            "imagenes" to marker.imagenes
                        ))
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }
    }
    fun removeMarker(marker: Marca){
        database.collection("markers")
            .whereEqualTo("nombre",marker.nombre)
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

}