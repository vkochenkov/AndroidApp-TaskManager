package com.vkochenkov.taskmanager.data.utils

fun List<String>.asString(delimiter: String): String {
    var str = ""
    this.forEachIndexed { index, value ->
        if (index == 0) {
            str = value
        } else {
            str = str + delimiter + value
        }
    }
    return str
}