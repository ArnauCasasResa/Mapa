package com.example.mymapa

import CameraScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymapa.OneMarker.DetallMarcador
import com.example.mymapa.OneMarker.EditarMarcador
import com.example.mymapa.Register.SesioScreen
import com.example.mymapa.ui.theme.MyMapaTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myViewModel by viewModels<MyViewModel>()
        setContent {
            MyMapaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyDrawer(myViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    myViewModel: MyViewModel,
    navController: NavController,
    scope: CoroutineScope,
    state: DrawerState
) {
    TopAppBar(
        title = { Text(text = " El fokin mapa",
            fontFamily = titleFont,
            fontSize = 36.sp)},
        colors= TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        navigationIcon = {
            AbrirMenu(scope,state)
        },
        actions = {
            IconButton(onClick = { navController.navigate(Routes.UsuarioDetall.route)}) {
                Icon(imageVector = Icons.Filled.AccountCircle,contentDescription = null,
                    modifier = Modifier.size(36.dp))
            }
        }
    )
}
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyDrawer(myViewModel: MyViewModel){
    val navController= rememberNavController()
    val scope= rememberCoroutineScope()
    val state:DrawerState= rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(drawerState = state, gesturesEnabled = false, drawerContent = {
        ModalDrawerSheet(
            modifier = Modifier.clickable { scope.launch { state.close() } }
        ) {
            Text(text="Menu", modifier = Modifier.padding(16.dp), fontFamily = titleFont, fontSize = 36.sp)
            Divider()
            NavigationDrawerItem(label = { Text(text = "Mapa", fontFamily = titleFont, fontSize = 20.sp)},
                selected = false,
                onClick = {navController.navigate(Routes.MapScreen.route)
                    scope.launch { state.close() }
                })
            NavigationDrawerItem(label = { Text(text = "Lista de marcadores",fontFamily = titleFont,fontSize = 20.sp)},
                selected = false,
                onClick = {navController.navigate(Routes.ListaMarcadores.route)
                    scope.launch { state.close() }
                })
        }
    }) {
        MyScaffold(myViewModel,navController,scope,state )
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyScaffold(
    myViewModel: MyViewModel,
    navController: NavHostController,
    scope: CoroutineScope,
    state: DrawerState
){
    val loggedIn by myViewModel.loggedIn.observeAsState(false)
    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }
    if (!loggedIn){
        SesioScreen(navController, myViewModel)
    }else{
        Scaffold(topBar = {MyTopAppBar(myViewModel,navController,scope,state) }) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (permissionState.status.isGranted){
                    NavHost(
                        navController = navController,
                        startDestination = Routes.MapScreen.route
                    )
                    {
                        composable(Routes.MapScreen.route) { MapScreen(navController,myViewModel) }
                        composable(Routes.ListaMarcadores.route) { ListaMarcadores(navController,myViewModel) }
                        composable(Routes.DetallMarcador.route) { DetallMarcador(navController,myViewModel) }
                        composable(Routes.CameraScreen.route) { CameraScreen(navController,myViewModel) }
                        composable(Routes.EditarMarcador.route) { EditarMarcador(navController,myViewModel) }
                        composable(Routes.SesioScreen.route) { SesioScreen(navController,myViewModel) }
                        composable(Routes.UsuarioDetall.route) { UsuarioDetall(navController,myViewModel) }
                    }
                }

            }
        }
    }

}



@Composable
fun AbrirMenu(scope: CoroutineScope, state: DrawerState){
    IconButton(onClick = { scope.launch { state.open() } }) {
        Icon(imageVector = Icons.Filled.Menu,contentDescription = "menu" )
    }
}
val nameFont = FontFamily(
    Font(R.font.mario, FontWeight.Bold)
)
val titleFont = FontFamily(
    Font(R.font.poke, FontWeight.Bold)
)