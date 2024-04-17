package com.example.mymapa

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController

@Composable
fun UsuarioDetall(navController: NavHostController, myViewModel: MyViewModel) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(R.drawable.pin), contentDescription ="pfp" )

        Button(onClick = {
            navController.navigate(Routes.MapScreen.route)
            myViewModel.logOut()
            myViewModel.log(false)
            }) {
            Text(text = "Log Out")
        }
    }


}