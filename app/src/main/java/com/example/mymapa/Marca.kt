package com.example.mymapa

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

class Marca {
    var nombre:String
    var ubicacion:LatLng
    var descripcion:String
    var imagenes:MutableList<Bitmap> = mutableListOf()
    constructor(nom:String,ubi:LatLng,des:String){
        this.nombre=nom
        this.ubicacion=ubi
        this.descripcion=des
    }
    fun addImage(img:Bitmap){
        imagenes.add(img)
    }
}