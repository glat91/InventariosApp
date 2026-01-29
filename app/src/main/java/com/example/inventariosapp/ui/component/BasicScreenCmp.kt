package com.example.inventariosapp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BasicScreenCmp(
    modifier: Modifier = Modifier,
    padding: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.padding(padding)) { content() }
}