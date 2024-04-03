package com.example.mymapa.OneMarker

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import com.example.mymapa.MyViewModel
import com.example.mymapa.R
import com.example.mymapa.Routes
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallMarcador(navController: NavController,myViewModel: MyViewModel){
    val showImage:Boolean by myViewModel.showImage.observeAsState(false)
    val showOptions:Boolean by myViewModel.showOptions.observeAsState(false)
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
                Text(text = "Nombre: ${marca.nombre}",modifier = Modifier.padding(8.dp))
                Text(text = "Descripcion: ${marca.descripcion}",modifier = Modifier.padding(8.dp))
                Spacer(modifier =Modifier.height(20.dp))
                if (marca.imagenes.isNotEmpty()) {
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .background(Color.Gray, RoundedCornerShape(10.dp))){
                        LazyRow {
                            items(marca.imagenes) {
                                CartaImagen(it, myViewModel)
                            }
                            item{
                                CartaAdd(navController,myViewModel)
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

                Column(modifier =Modifier.fillMaxWidth(),horizontalAlignment = Alignment.End){
                    Button(onClick = { navController.navigate(Routes.EditarMarcador.route) }) {
                        Icon(imageVector = Icons.Filled.Edit,contentDescription = "edit" )
                    }
                }
                 MyDialogImage(showImage,{ myViewModel.turnFalseImage() },myViewModel)
            }
        }
    }
}






@Composable
fun CartaImagen(image: Bitmap,myViewModel: MyViewModel) {
    Card(
        border = BorderStroke(2.dp, Color.Transparent),
        modifier = Modifier
            .padding(8.dp)
            .size(100.dp)
            .clickable { myViewModel.changeImagenActual(image);myViewModel.turnTrueImage() })
    {
        Image(bitmap = image.asImageBitmap(), contentDescription = "Imagen de la marca", contentScale = ContentScale.Crop)
    }

}

@Composable
fun CartaAdd(navController: NavController,myViewModel: MyViewModel) {
    Card(
        border = BorderStroke(2.dp, Color.Transparent),
        modifier = Modifier
            .padding(8.dp)
            .size(100.dp)
            .clickable { navController.navigate(Routes.CameraScreen.route) }) {
        Box(Modifier.fillMaxSize()) {
            Icon(imageVector = Icons.Default.Add,contentDescription = "Añadir imagen",
                modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun MyDialogImage(show: Boolean, onDismiss: () -> Unit,myViewModel: MyViewModel){
    if(show){
        Dialog(onDismissRequest = { onDismiss() }) {
            (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0.8f)
            Column(
                Modifier
                    .background(Color.White)
                    .fillMaxWidth()) {
                myViewModel.imagenActual.value?.let { Image(bitmap = it.asImageBitmap(), contentDescription = "Imagen de la marca",contentScale = ContentScale.Crop) }
            }
        }
    }
}