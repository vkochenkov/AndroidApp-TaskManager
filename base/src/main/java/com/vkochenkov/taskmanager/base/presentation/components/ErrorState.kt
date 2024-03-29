package com.vkochenkov.taskmanager.base.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkochenkov.taskmanager.base.R

@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.main_error_text)
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 18.sp,
            text = text,
            textAlign = TextAlign.Center
        )
    }
}