package com.example.mymapa

import android.Manifest
import android.annotation.SuppressLint

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun MapScreen(navController: NavController,myViewModel: MyViewModel){
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var nuevaMarca by remember { mutableStateOf(LatLng(0.0,0.0)) }
    var showBottomSheet by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
       val marcaActual:Marca by myViewModel.marcaActual.observeAsState(Marca("ITB",LatLng(41.4534265,2.1837151),"Inst Tecnologic de Bcn"))
       val cameraPositionState= rememberCameraPositionState{
           position = CameraPosition.fromLatLngZoom(marcaActual.ubicacion,10f)
       }

       GoogleMap(modifier = Modifier.fillMaxSize(),cameraPositionState=cameraPositionState,
           onMapClick = {showBottomSheet=true;nuevaMarca=it}){
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
                    Button(onClick = {
                        myViewModel.addTOList(Marca(nombre,nuevaMarca,descripcion))
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                        myViewModel.changeActual(Marca(nombre,nuevaMarca,descripcion))
                    }) {
                        Text("Add")
                    }
                }

            }
        }
    }
}

