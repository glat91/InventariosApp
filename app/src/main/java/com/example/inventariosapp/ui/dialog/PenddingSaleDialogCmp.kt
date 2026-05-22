package com.example.inventariosapp.ui.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun PenddingSaleDialogCmp(
    backGroundColor: Color = Color.White,
    content: @Composable () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Card del contenido principal
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = backGroundColor)
            ) {
                content()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PenddingSaleDialogCmpPreview(){
    PenddingSaleDialogCmp(
        content = {

        },
        onDismiss = {}
    )
}