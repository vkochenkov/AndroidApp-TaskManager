package com.vkochenkov.taskmanager.presentation.navigation

sealed class Destination: Route {

    object Main : Destination() {

        override val route: String = "Main"
    }

    object Details : Destination(), RouteWith1Arg {

        override val argument1: String = "id"

        override val route: String = "Details"
    }
}

interface Route {

    val route: String
}

interface RouteWith1Arg : Route {

    val argument1: String?

    val routeWithArgs: String
        get() = "$route?$argument1={$argument1}"

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