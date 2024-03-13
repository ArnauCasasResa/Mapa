package com.example.mymapa

sealed class Routes(val route:String) {
    object MapScreen:Routes("mapa")
    object ListaMarcadores:Routes("listaMarcas")
    object DetallMarcador:Routes("detallMarcador")
    object CameraScreen:Routes("cameraScreen")
}