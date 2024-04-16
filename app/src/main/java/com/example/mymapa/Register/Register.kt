package com.example.mymapa.Register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mymapa.MyViewModel
import com.example.mymapa.Routes

@Composable
fun SesioScreen(navController: NavHostController, myViewModel: MyViewModel) {
    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var login by remember {mutableStateOf(true)}
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = if (login) "Iniciar sesion" else "Registrarse")
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = mail,
            onValueChange = { mail = it },
            label = { Text("Correo electronico") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            if (!login) {
                Text("¿Ya tienes cuenta?")
                Text("Iniciar sesion", modifier = Modifier.clickable {login=true },color = Color.Blue)
            }else{
                Text("¿No tienes cuenta?")
                Text("Registrate", modifier=Modifier.clickable { login=false}, color = Color.Blue)
            }
        }
        if (login){
            Button(onClick = { myViewModel.login(mail,password)
                if (myViewModel.goToNext.value == true) {
                    myViewModel.log(true)
                }})
            {
                Text(text = "Log In")
            }
        }else{
            Button(onClick = { myViewModel.register(mail,password)
                if (myViewModel.goToNext.value == true) {
                    myViewModel.log(true)
                }}) {
                Text(text = "Register")
            }
        }


    }
}