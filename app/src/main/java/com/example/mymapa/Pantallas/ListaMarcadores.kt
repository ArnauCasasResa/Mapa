package com.example.mymapa

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
    var filtroSelect by remember{ mutableStateOf("Todos") }
    var expanded by remember { mutableStateOf(false) }
    val opciones =
        listOf("Todos","Marcador Comun", "Hospital", "Hotel", "Restaurante", "Escuela", "Veterinario")
    val marcadores: MutableList<Marca> by myViewModel.listaMarcas.observeAsState(mutableListOf<Marca>())
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxWidth().padding(5.dp)){
            OutlinedTextField(
                value = filtroSelect,
                onValueChange = { filtroSelect = it },
                enabled = false,
                readOnly = true,
                modifier = Modifier
                    .clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                opciones.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            expanded = false
                            filtroSelect = option
                        },
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
        }
        if (marcadores.isNotEmpty()){
            LazyColumn() {
                val listaTemp=marcadores.filter { it.tipo==filtroSelect || filtroSelect=="Todos"}
                items(listaTemp) {
                    MarckerItem(marca = it, navController, myViewModel)
                }
            }
        }else{
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(text = "No hay marcadores guardados.",
                    fontStyle = FontStyle.Italic,
                    color = Color.LightGray)
            }
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
                fontFamily = titleFont,
                fontSize = 45.sp,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { myViewModel.deleteTOList(marca)}) {
                Icon(imageVector = Icons.Filled.Close,contentDescription = "delete" )
            }
        }
    }
}

