package com.vkochenkov.taskmanager.data.repos

import android.content.SharedPreferences
import com.vkochenkov.taskmanager.data.DefaultData.COMMON_DELIMITER
import com.vkochenkov.taskmanager.data.DefaultData.defaultStatuses
import com.vkochenkov.taskmanager.data.utils.asString

class StatusRepository(
    private val statusPrefs: SharedPreferences
) {

    private val defaultStatusesStr = defaultStatuses.asString(COMMON_DELIMITER)

    fun getStatuses(): List<String> {
        return (statusPrefs.getString(STATUSES_LIST_KEY, defaultStatusesStr)
            ?: defaultStatusesStr)
            .split(COMMON_DELIMITER)
    }

    fun rewriteStatuses(newStatuses: List<String>) {
        statusPrefs.edit().putString(STATUSES_LIST_KEY, newStatuses.asString(COMMON_DELIMITER)).apply()
    }

    companion object {
        const val STATUSES_PREFS_KEY = "STATUSES_PREFS"
        private const val STATUSES_LIST_KEY = "STATUSES_LIST"
    }
}