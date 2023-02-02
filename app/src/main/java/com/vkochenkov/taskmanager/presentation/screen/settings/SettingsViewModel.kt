package com.vkochenkov.taskmanager.presentation.screen.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.vkochenkov.taskmanager.data.repos.StatusRepository
import com.vkochenkov.taskmanager.presentation.base.BaseViewModel

class SettingsViewModel(
    savedStateHandle: SavedStateHandle,
    val statusRepository: StatusRepository
) : BaseViewModel() {

    private var _state: MutableState<SettingsBodyState> = mutableStateOf(SettingsBodyState.Content)
    val state: State<SettingsBodyState> get() = _state

    // todo set true after change some data
    private var showDialogOnBack: Boolean = false

    val onAction = { action: SettingsActions ->
        when (action) {
            is SettingsActions.BackPressed -> onBackPressed(action.showDialog)
//            is SettingsActions.OpenDetails -> onOpenDetails(action.id)
//            is SettingsActions.AddNewTask -> onAddNewTask()
//            is SettingsActions.UpdateData -> onUpdateData()
        }
    }

    private fun onBackPressed(showDialog: Boolean?) {
        showDialog?.let { showDialogOnBack = it }
        if (showDialogOnBack) {
            // todo
//            _state.value = DetailsBodyState.Content(
//                task = currentTask!!,
//                statuses = statuses,
//                showOnBackDialog = true
//            )
        } else {
            navController.popBackStack()
        }
    }
}