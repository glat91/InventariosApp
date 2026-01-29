package com.example.inventariosapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp

@Composable
fun ButtonCmp(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enable: Boolean = true,
    shape: Shape = RoundedCornerShape(10.dp),
    border: BorderStroke? = null,//ButtonDefaults.outlinedButtonBorder,
    txtColor: Color = Color.Blue,
    maxLines: Int = 1,
    textSize: TextUnit = 10.sp,
    backGroundColor: Color = Color.Transparent,
    disableBackGroundColor: Color = Color.Transparent,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        focusedElevation = 0.dp,
        hoveredElevation = 0.dp,
        disabledElevation = 0.dp,
    ),
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enable,
        shape = shape,
        colors = ButtonColors(
            contentColor = backGroundColor,
            containerColor = backGroundColor,
            disabledContentColor = disableBackGroundColor,
            disabledContainerColor = disableBackGroundColor
        ),
        elevation = elevation,
        border = border,//BorderStroke(1.dp, Color.Blue),
        content = {
            TextCmp(
                text = text,
                fontSize = textSize,
                maxLine = maxLines,
                color = txtColor,
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun ButtonCmpPreview(){
    ButtonCmp(
        text = "Cancelar",
        backGroundColor = Color.Gray,
        onClick = { }
    )
}