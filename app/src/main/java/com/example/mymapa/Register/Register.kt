package com.example.mymapa.Register

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mymapa.Clases.UserPrefs
import com.example.mymapa.ClickOutsideToDismissKeyboard
import com.example.mymapa.MyViewModel
import com.example.mymapa.Routes
import com.example.mymapa.nameFont
import com.example.mymapa.titleFont
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SesioScreen(navController: NavHostController, myViewModel: MyViewModel) {
    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var login by remember { mutableStateOf(0) }
    val showError: Boolean by myViewModel._goToNext.observeAsState(true)

    val context = LocalContext.current
    val userPrefs = UserPrefs(context)
    val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())
    var remember by remember { mutableStateOf(false) }
    if (myViewModel.primeraVez) {
        if (storedUserData.value.isNotEmpty() && storedUserData.value[0] != "" && storedUserData.value[1] != "") {
            storedUserData.value.let {
                mail = it[0]
                password = it[1]
                myViewModel.primeraVez = false
            }
        }
    }
    ClickOutsideToDismissKeyboard {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (login == 0) "INICIAR SESION" else if (login == 1) "REGISTRARSE" else "",
                fontFamily = nameFont,
                fontSize = 30.sp
            )
            if (login != 2) {
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
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true

                )
                if (!showError) {
                    Box(
                        modifier = Modifier
                            .background(Color.Red)
                            .padding(10.dp)
                            .width(260.dp)
                    ) {
                        if (login == 0)
                            Text(
                                text = "Correo electronico o contraseña incorrectos.",
                                textAlign = TextAlign.Center
                            )
                        else if (login == 1)
                            Text(
                                text = "El correo electronico ya esta en uso.",
                                textAlign = TextAlign.Center
                            )

                    }
                }
                if (login == 0) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(50.dp))
                        Checkbox(checked = remember, onCheckedChange = { remember = it })
                        Text("Recordarme")
                    }
                } else {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Row {
                    if (login == 1) {

                        Text("¿Ya tienes cuenta? ")
                        Text(
                            "Iniciar sesion",
                            modifier = Modifier.clickable { login = 0 },
                            color = Color.Blue
                        )
                    } else if (login == 0) {
                        Text("¿No tienes cuenta? ")
                        Text(
                            "Registrate",
                            modifier = Modifier.clickable { login = 1 },
                            color = Color.Blue
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (login == 0) {
                    Button(onClick = {
                        myViewModel.login(mail, password)
                        if (myViewModel._goToNext.value == true) {
                            CoroutineScope(Dispatchers.IO).launch {
                                if (remember) {
                                    userPrefs.saveUserData(mail, password)
                                }
                            }
                            myViewModel.log(true)
                        }
                    })
                    {
                        Text(text = "Log In")
                    }
                } else if (login == 1) {
                    Button(onClick = {
                        myViewModel.register(mail, password)
                        if (myViewModel._goToNext.value == true) {
                            login = 2
                        }
                    }) {
                        Text(text = "Register")
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Usuari creat correctament!",
                        color = Color.Green,
                        fontSize = 30.sp,
                        fontFamily = titleFont,
                        textAlign = TextAlign.Center
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(3500)
                        login = 0
                    }
                }
            }
        }
    }
}