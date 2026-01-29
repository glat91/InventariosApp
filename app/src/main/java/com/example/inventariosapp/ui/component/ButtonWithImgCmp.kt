package com.example.inventariosapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp

@Composable
fun ButtonWithImgCmp(
    modifier: Modifier = Modifier,
    text: String,
    enable: Boolean = true,
    backGroundColor: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(35.dp),
        enabled = enable,
        colors = ButtonDefaults.buttonColors(
            containerColor = backGroundColor,
            disabledContainerColor = Color.Gray
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 0.dp, disabledElevation = 0.dp),
        border = BorderStroke(1.dp, Color.Black.copy(.85f)),
        content = {
            TextCmp(
                text = text,
                modifier = Modifier.padding(),
                fontSize = 14.sp,
            )
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    icon,
                    contentDescription = "",
                    modifier = Modifier.padding(2.dp),
                    tint = Color.Black
                )
            }
        }
    )
    /*
    Card(
        modifier = Modifier.shadow(8.dp, shape = RoundedCornerShape(10.dp)),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ){
        Row(
            modifier = modifier
                .clickable { onClick() }
                .background(color = backGroundColor, shape = RoundedCornerShape(10.dp))
                .height(30.dp)
                .border(width = 2.dp, color = Color.Black.copy(.7f), RoundedCornerShape(10.dp))
                .padding(PADDING_4)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextCmp(
                text = text,
                modifier = Modifier.padding(start = PADDING_4),
                fontSize = 14.sp,
            )
            VerticalDivider(thickness = 10.dp, color = Color.Transparent)
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    icon,
                    contentDescription = "",
                    modifier = Modifier.padding(2.dp).size(15.dp),
                    tint = Color.Black
                )
            }
        }
    }

     */
}

@Preview(showBackground = true)
@Composable
fun ButtonWithImgCmpPreview(){
    ButtonWithImgCmp(
        text = "Cancelar",
        backGroundColor = Color.Yellow,
        icon = Icons.Default.Delete,
        enable = false,
        onClick = { }
    )
}