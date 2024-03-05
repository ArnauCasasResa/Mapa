package com.example.mymapa

sealed class Routes(val route:String) {
    object MapScreen:Routes("mapa")
}