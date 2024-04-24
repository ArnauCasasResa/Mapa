package com.example.mymapa.Register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mymapa.Clases.UserPrefs
import com.example.mymapa.MyViewModel
import com.example.mymapa.R
import com.example.mymapa.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UsuarioDetall(navController: NavHostController, myViewModel: MyViewModel) {
    val context = LocalContext.current
    val userPrefs = UserPrefs(context)
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(50.dp))
        Icon(
            imageVector = Icons.Default.AccountBox,
            contentDescription = "Usuario",
            tint = Color.Black,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "Usuario: ${myViewModel._loggedUser.value}")
        Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    userPrefs.deleteUserData()
                }
                navController.navigate(Routes.MapScreen.route)
                myViewModel.logOut()
                myViewModel.log(false)
            }) {
                Text(text = "Log Out")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}