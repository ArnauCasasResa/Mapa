package com.example.mymapa.Register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mymapa.MyViewModel
import com.example.mymapa.R
import com.example.mymapa.Routes

@Composable
fun UsuarioDetall(navController: NavHostController, myViewModel: MyViewModel) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier =Modifier.height(50.dp))
        Image(painter = painterResource(R.drawable.pin), contentDescription ="pfp" )
        Spacer(modifier =Modifier.height(30.dp))
        Text(text = "Usuario: ${myViewModel._loggedUser.value}")
        Column (Modifier.fillMaxHeight(),verticalArrangement = Arrangement.Bottom){
            Button(onClick = {
                navController.navigate(Routes.MapScreen.route)
                myViewModel.logOut()
                myViewModel.log(false)
            }) {
                Text(text = "Log Out")
            }
            Spacer(modifier =Modifier.height(20.dp))
        }
    }
}