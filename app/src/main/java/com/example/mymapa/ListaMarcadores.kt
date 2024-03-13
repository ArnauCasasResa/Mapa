package com.example.mymapa

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun ListaMarcadores(navController: NavController,myViewModel: MyViewModel){
    val marcadores: MutableList<Marca> by myViewModel.listaMarcas.observeAsState(mutableListOf<Marca>(Marca("ITB",LatLng(41.4534265,2.1837151),"Inst Tecnologic de Bcn")))
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
fun MarckerItem(marca: Marca, navController: NavController,myViewModel: MyViewModel) {
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
            Image(painter = painterResource(R.drawable.pin),
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

