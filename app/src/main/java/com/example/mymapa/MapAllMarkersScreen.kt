package com.example.mymapa

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.mymapa.Clases.Marca
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("MutableCollectionMutableState", "MissingPermission")
@Composable
fun MapAllMarkersScreen(navController: NavController,myViewModel: MyViewModel){
    myViewModel.getMarkers()
    val marcaActual: Marca by myViewModel.marcaActual.observeAsState(Marca("ITB",LatLng(41.4534265,2.1837151),"Inst Tecnologic de Bcn","Escuela",""))
    val cameraPositionState= rememberCameraPositionState{position = CameraPosition.fromLatLngZoom(marcaActual.ubicacion,10f) }
    val context= LocalContext.current
    val fusedLocationProviderClient=remember{LocationServices.getFusedLocationProviderClient(context)}
    var lastKnownLocation by remember{ mutableStateOf<Location?>(null)}
    var deviceLatIng by remember { mutableStateOf(LatLng(0.0,0.0)) }
    val locationResult=fusedLocationProviderClient.getCurrentLocation(100,null)
    locationResult.addOnCompleteListener(context as MainActivity){task->
        if(task.isSuccessful){
            lastKnownLocation=task.result
            deviceLatIng= LatLng(lastKnownLocation!!.latitude,lastKnownLocation!!.longitude)
            cameraPositionState.position= CameraPosition.fromLatLngZoom(deviceLatIng,15f)
        }
    }

        Column(modifier = Modifier.fillMaxSize()) {
            GoogleMap(modifier = Modifier.fillMaxSize(),cameraPositionState=cameraPositionState,
                properties = MapProperties(isBuildingEnabled = true, isMyLocationEnabled = true)
            ){
                for (marca in myViewModel.listaMarcas.value!!){
                    Marker(state = MarkerState(position = marca.ubicacion),
                        title = marca.nombre,
                        snippet = "Marker at ${marca.nombre}")
                }
            }
        }

}

