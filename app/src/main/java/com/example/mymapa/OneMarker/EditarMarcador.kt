package com.example.mymapa.OneMarker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mymapa.MyViewModel
import com.example.mymapa.Routes
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun EditarMarcador(navController: NavController,myViewModel: MyViewModel){
    var expanded by remember { mutableStateOf(false) }
    val opciones = listOf("Hospital", "Hotel", "Restaurant","Escuela","")

    val show:Boolean by myViewModel.showImage.observeAsState(false)
    val marca=myViewModel.marcaActual.value
    val cameraPositionState= rememberCameraPositionState{
        if (marca != null) {
            position = CameraPosition.fromLatLngZoom(marca.ubicacion, 15f)
        }
    }
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Card(modifier = Modifier
            .size(500.dp)
            .clickable {
                if (marca != null) myViewModel.changeActual(marca)
                navController.navigate(Routes.MapScreen.route)
            }
            .padding(8.dp),border = BorderStroke(2.dp, Color.Transparent)) {
            GoogleMap(cameraPositionState=cameraPositionState){
                Marker(state = MarkerState(position = marca!!.ubicacion),
                    title = marca.nombre,
                    snippet = "Marker at ITB")
            }
        }
        Column(modifier=Modifier.padding(8.dp)) {
            if (marca != null) {
                var nombre by remember { mutableStateOf(marca.nombre) }
                var descripcion by remember { mutableStateOf(marca.descripcion) }
                var tipo by remember{ mutableStateOf(marca.tipo) }
                Spacer(modifier =Modifier.height(10.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )


                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripcion") }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(125.dp)
                ) {
                    opciones.forEach { option ->
                        DropdownMenuItem(text = { Text(text = option) },
                            modifier = Modifier.height(40.dp),
                            onClick = {
                                expanded = false
                                tipo=option
                            })
                    }
                }
                
                Spacer(modifier =Modifier.height(20.dp))
                if (marca.imagenes.isNotEmpty()) {
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .background(Color.Gray, RoundedCornerShape(10.dp))){
                        LazyRow {
                            items(marca.imagenes) {
                                CartaImagen(it, myViewModel)
                            }
                        }
                    }
                }else{
                    Column(modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally){
                        Button(onClick = { navController.navigate(Routes.CameraScreen.route) }) {
                            Text(text = "Añadir imagen")
                        }
                    }
                }
                Spacer(modifier =Modifier.height(10.dp))
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Button(onClick = { myViewModel.saveChanges(nombre,descripcion,tipo);myViewModel.editMarker()
                        navController.navigate(Routes.DetallMarcador.route) }) {
                        Text(text = "Guardar cambios")
                    }
                }
                 MyDialogImage(show,{ myViewModel.turnFalseImage() },myViewModel)
            }

        }
    }
}





