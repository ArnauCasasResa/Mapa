package com.example.mymapa

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun ListaMarcadores(navController: NavController,myViewModel: MyViewModel){
    val marcadores:MutableList<Marca> by myViewModel.listaMarcas.observeAsState(mutableListOf())

    LazyColumn() {
        items(marcadores) {
            MarckerItem(marca = it, navController, myViewModel)
        }
    }

}



@Composable
fun MarckerItem(marca: Marca, navController: NavController,myViewModel: MyViewModel) {
    Card(
        border = BorderStroke(2.dp, Color.Transparent),
        modifier = Modifier
            .padding(8.dp)
            .clickable {

                navController.navigate(Routes.MapScreen.route)
            }
    ) {
        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),) {
            //Image(painter = painterResource(R.drawable.pin), contentDescription = "pin")
            Text(
                text = marca.nombre,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}