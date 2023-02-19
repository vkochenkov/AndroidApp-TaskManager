package com.vkochenkov.taskmanager.base.presentation.navigation

interface Route {

    companion object {
        fun buildDeeplink(route: String): String {
            return "https://com.vkochenkov.taskmanager/$route"
        }
    }

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