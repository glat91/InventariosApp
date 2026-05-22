package com.example.inventariosapp.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.ui.theme.UI_BT

@Composable
fun BasicDialogCmp(
    color: Color,
    backGroundColor: Color = Color.White,
    content: @Composable () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                colors = CardDefaults.cardColors(containerColor = backGroundColor)
            ) {
                content()
            }
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(color, CircleShape)
                    .border(2.dp, Color.White, CircleShape), // borde blanco para que resalte
                contentAlignment = Alignment.Center
            ) {
                TextCmp(
                    text = "!",
                    color = Color.White,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasicDialogCmpPreviewq(){
    BasicDialogCmp(
        color = UI_BT,
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    
                }
            }
        },
        onDismiss = {}
    )
}