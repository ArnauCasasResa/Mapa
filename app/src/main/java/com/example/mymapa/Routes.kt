package com.example.mymapa

sealed class Routes(val route:String) {
    object MapScreen:Routes("mapa")
    object ListaMarcadores:Routes("listaMarcas")
    object DetallMarcador:Routes("detallMarcador")
    object CameraScreen:Routes("cameraScreen")
    object MapAllMarkersScreen:Routes("allMarkersMapScreen")
    object EditarMarcador:Routes("editarMarcador")
    object GalleryScreen:Routes("galleryScreen")
}