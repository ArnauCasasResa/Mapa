package com.example.mymapa.Clases

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import java.net.IDN
import java.util.UUID

class Marca {
    var nombre:String=""
    var ubicacion:LatLng=LatLng(0.0,0.0)
    var descripcion:String=""
    var imagenes:MutableList<String> = mutableListOf()
    var tipo:String=""
    var id:String=""

    constructor()
    constructor(nom:String,ubi:LatLng,des:String,tipo:String,id:String){
        this.nombre=nom
        this.ubicacion=ubi
        this.descripcion=des
        this.tipo=tipo
        this.id=id
    }
}