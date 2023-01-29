package com.vkochenkov.taskmanager.presentation.navigation

import com.vkochenkov.taskmanager.BuildConfig

sealed class Destination : Route {

    object Main : Destination() {

        override val route: String = "Main"
    }

    object Details : Destination(), RouteWith1Arg {

        override val argument1: String = "id"

        override val route: String = "Details"
    }
}

fun buildDeeplink(route: String): String {
    return "https://${BuildConfig.APPLICATION_ID}/$route"
}

interface Route {

    val route: String
}

interface RouteWith1Arg : Route {

    val argument1: String?

    /**
     * Use for route in AppNavHost
     */
    val routeWithArgs: String
        get() = "$route?$argument1={$argument1}"

    /**
     * Use inside navController.navigate()
     */
    fun passArguments(arg1: String?): String {
        return "$route?$argument1=$arg1"
    }
}

interface RouteWith2Arg : Route {

    val argument1: String?

    val argument2: String?

    val routeWithArgs: String
        get() = "$route?$argument1={$argument1}&$argument2={$argument2}"

    fun passArguments(arg1: String?, arg2: String?): String {
        return "$route?$argument1=$arg1&$argument2=$arg2"
    }
}