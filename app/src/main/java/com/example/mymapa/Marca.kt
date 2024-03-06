package com.example.mymapa

import com.google.android.gms.maps.model.LatLng

class Marca {
    var nombre:String
    var ubicacion:LatLng

    constructor(nom:String,ubi:LatLng){
        this.nombre=nom
        this.ubicacion=ubi
    }
}