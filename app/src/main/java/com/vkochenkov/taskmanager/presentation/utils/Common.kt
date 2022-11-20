package com.vkochenkov.taskmanager.presentation.utils

fun String?.isNotNull(): Boolean {
    return this != null && this != "null"
}