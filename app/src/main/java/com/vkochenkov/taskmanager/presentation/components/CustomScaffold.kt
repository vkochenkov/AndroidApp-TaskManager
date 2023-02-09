package com.vkochenkov.taskmanager.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun CustomScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    isBlockInteractionContent: Boolean = false,
    isBlockInteractionFull: Boolean = false,
    isLoadingPage: Boolean = false,
    isErrorPage: Boolean = false,
    content: @Composable (PaddingValues) -> Unit
) {

    Box(
        modifier = if (isBlockInteractionFull) {
            Modifier
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent(pass = PointerEventPass.Initial)
                                .changes
                                .forEach(PointerInputChange::consume)
                        }
                    }
                }
        } else {
            Modifier
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = topBar,
            bottomBar = bottomBar,
            snackbarHost = snackbarHost,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            containerColor = containerColor,
            contentColor = contentColor,
            contentWindowInsets = contentWindowInsets,
            content = {
                if (isLoadingPage) {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(60.dp)
                        )
                    }
                } else if (isErrorPage) {
                    ErrorState(modifier = Modifier.padding(it))
                } else {
                    content.invoke(it)
                }
                if (isBlockInteractionContent) {
                    Box(Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null, // Disable ripple effect
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                // Do nothing
                            }
                        )
                    )
                }
            }
        )
    }
}