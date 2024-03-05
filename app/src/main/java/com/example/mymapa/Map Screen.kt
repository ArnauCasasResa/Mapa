package com.example.mymapa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(navController: NavController){
    var marcadores= mutableListOf<LatLng>()
   Column(modifier = Modifier
       .fillMaxSize()
       .padding(16.dp)) {
       val itb= LatLng(41.4534265,2.1837151)
       marcadores.add(itb)
       val cameraPositionState= rememberCameraPositionState{
           position = CameraPosition.fromLatLngZoom(itb,10f)
       }
       GoogleMap(modifier = Modifier.fillMaxSize(),cameraPositionState=cameraPositionState,
           onMapClick = {
               val marcador=LatLng(0.0,0.0)
               marcadores.add(marcador)
           }){
           for(i in marcadores){
               Marker(state = MarkerState(position = i),
                   title = "ITB",
                   snippet = "Marker at ITB")
           }


       }
   }
}