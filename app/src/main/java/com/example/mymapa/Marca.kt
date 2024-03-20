package com.example.mymapa

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

class Marca {
    var nombre:String=""
    var ubicacion:LatLng=LatLng(0.0,0.0)
    var descripcion:String=""
    var imagenes:MutableList<Bitmap> = mutableListOf()
    var tipo:String=""

    constructor()
    constructor(nom:String,ubi:LatLng,des:String){
        this.nombre=nom
        this.ubicacion=ubi
        this.descripcion=des
    }


    fun addImage(img:Bitmap){
        this.imagenes.add(img)
    }
}