package com.example.mymapa

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MapScreen(navController: NavController){
    var marcadores by remember { mutableStateOf(mutableListOf<LatLng>())}
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
               marcadores.add(it)
           }){
           for(i in marcadores){
               Marker(state = MarkerState(position = i),
                   title = "ITB",
                   snippet = "Marker at ITB")
           }


       }
   }
}