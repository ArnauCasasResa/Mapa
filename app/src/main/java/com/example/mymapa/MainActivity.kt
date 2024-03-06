package com.example.mymapa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymapa.ui.theme.MyMapaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
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
                    val navigationController = rememberNavController()
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
            fontFamily = FontFamily.SansSerif,
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
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Filled.Search,contentDescription = null )
            }
        }
    )
}
@Composable
fun MyDrawer(myViewModel: MyViewModel){
    val navController= rememberNavController()
    val scope= rememberCoroutineScope()
    val state:DrawerState= rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(drawerState = state, gesturesEnabled = false, drawerContent = {
        ModalDrawerSheet {
            Text(text="Menu", modifier = Modifier.padding(16.dp))
            Divider()
            NavigationDrawerItem(label = { Text(text = "Mapa")},
                selected = false,
                onClick = {navController.navigate(Routes.MapScreen.route)
                    scope.launch { state.close() } })
            NavigationDrawerItem(label = { Text(text = "Lista de marcadores")},
                selected = false,
                onClick = {navController.navigate(Routes.ListaMarcadores.route)
                    scope.launch { state.close() } })

        }
    }) {
        MyScaffold(myViewModel,navController,scope,state )
    }
}

@Composable
fun MyScaffold(
    myViewModel: MyViewModel,
    navController: NavHostController,
    scope: CoroutineScope,
    state: DrawerState
){
    Scaffold(topBar = {MyTopAppBar(myViewModel,navController,scope,state) }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.MapScreen.route
            )
            {
                composable(Routes.MapScreen.route) { MapScreen(navController,myViewModel) }
                composable(Routes.ListaMarcadores.route) { ListaMarcadores(navController,myViewModel) }

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