package com.example.mymapa

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymapa.Clases.Marca

@SuppressLint("SuspiciousIndentation")
@Composable
fun ListaMarcadores(navController: NavController,myViewModel: MyViewModel){
    myViewModel.getMarkers()
    val marcadores: MutableList<Marca> by myViewModel.listaMarcas.observeAsState(mutableListOf<Marca>())
    if (marcadores.isNotEmpty()){
        LazyColumn() {
            items(marcadores) {
                MarckerItem(marca = it, navController, myViewModel)
            }
        }
    }else{
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = "No hay personajes en favoritos.",
                fontStyle = FontStyle.Italic,
                color = Color.LightGray)
        }
    }
}



@Composable
fun MarckerItem(marca: Marca, navController: NavController, myViewModel: MyViewModel) {
    val imagen=when(marca.tipo){
        "Hospital"-> R.drawable.hospital
        "Hotel"-> R.drawable.hotel
        "Restaurante"-> R.drawable.restaurant
        "Escuela"-> R.drawable.university
        "Veterinario"-> R.drawable.veterinary
        else -> R.drawable.pin
    }
    Card(
        border = BorderStroke(2.dp, Color.Transparent),
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                myViewModel.changeActual(marca)
                navController.navigate(Routes.DetallMarcador.route)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Image(painter = painterResource(imagen),
                contentDescription = "pin",Modifier.size(60.dp))
            Text(
                text = marca.nombre,
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = nameFont,
                fontSize = 45.sp,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { myViewModel.deleteTOList(marca)}) {
                Icon(imageVector = Icons.Filled.Close,contentDescription = "delete" )
            }
        }
    }
}

