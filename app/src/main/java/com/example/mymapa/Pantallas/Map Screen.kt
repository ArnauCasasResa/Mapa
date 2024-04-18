package com.example.mymapa

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mymapa.Clases.Marca
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@SuppressLint("MutableCollectionMutableState", "MissingPermission")
@Composable
fun MapScreen(navController: NavController,myViewModel: MyViewModel){
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var nuevaMarca by remember { mutableStateOf(LatLng(0.0,0.0)) }
    val marcaActual: Marca by myViewModel.marcaActual.observeAsState(Marca("ITB",LatLng(41.4534265,2.1837151),"Inst Tecnologic de Bcn","Escuela",""))
    var cameraPositionState= rememberCameraPositionState{position = CameraPosition.fromLatLngZoom(marcaActual.ubicacion,10f) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val context= LocalContext.current
    val fusedLocationProviderClient=remember{LocationServices.getFusedLocationProviderClient(context)}
    var lastKnownLocation by remember{ mutableStateOf<Location?>(null)}
    var deviceLatIng by remember { mutableStateOf(LatLng(0.0,0.0)) }
    val locationResult=fusedLocationProviderClient.getCurrentLocation(100,null)
    val inicioPantalla:Boolean by myViewModel.inicioPantall.observeAsState(true)
    if (inicioPantalla){
        locationResult.addOnCompleteListener(context as MainActivity){task->
            if(task.isSuccessful){
                lastKnownLocation=task.result
                deviceLatIng= LatLng(lastKnownLocation!!.latitude,lastKnownLocation!!.longitude)
                cameraPositionState.position= CameraPosition.fromLatLngZoom(deviceLatIng,15f)
                myViewModel.inicio()
            }
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
       GoogleMap(modifier = Modifier.fillMaxSize(),cameraPositionState=cameraPositionState,
           onMapClick = {showBottomSheet=true;nuevaMarca=it},
           properties = MapProperties(isBuildingEnabled = true, isMyLocationEnabled = true)
       ){
           Marker(state = MarkerState(position = marcaActual.ubicacion),
               title = marcaActual.nombre,
               snippet = "Marker at ${marcaActual.nombre}")
       }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                var nombre by remember{ mutableStateOf("")}
                var descripcion by remember{ mutableStateOf("")}
                var tipo by remember { mutableStateOf("Marcador Comun") }
                var id by remember { mutableStateOf("") }
                Column(Modifier.padding(10.dp)) {
                    TextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Name") },
                        singleLine = true,
                        placeholder = { Text(text = "New Ubication")}
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    TextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Description") },
                        singleLine = true,
                        placeholder = { Text(text = "New Ubication")}
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    var listaTipos= listOf(Pair("Hospital",R.drawable.hospital),Pair("Hotel",R.drawable.hotel),Pair("Restaurante",
                        R.drawable.restaurant),Pair("Escuela",R.drawable.university),Pair("Veterinario",R.drawable.veterinary))
                    Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceEvenly){
                        for (i in listaTipos){
                            Image(painter = painterResource(i.second), contentDescription =i.first ,Modifier.size(50.dp)
                                .clickable { tipo=i.first })
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(onClick = {
                        id=UUID.randomUUID().toString()
                        val marca= Marca(nombre,nuevaMarca,descripcion,tipo,id)
                        marca.usuario=myViewModel._userId.value!!
                        myViewModel.addTOList(marca)
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                        myViewModel.changeActual(Marca(nombre,nuevaMarca,descripcion,tipo,id))
                    }) {
                        Text("Add")
                    }
                }

            }
        }
    }
}

