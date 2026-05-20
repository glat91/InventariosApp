package com.example.inventariosapp.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.ui.theme.Black

@Composable
fun InputhWithTitleCmp(
    modifier: Modifier = Modifier,
    labelText: String = "",
    labelTextSize: TextUnit = 16.sp,
    keyboardType: KeyboardType = KeyboardType.Password,
    imeAction: ImeAction = ImeAction.Default,
    keyboardAction: () -> Unit = {},
    textValue: String = "",
    textValueSize: TextUnit = 15.sp,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textAlign: TextAlign = TextAlign.Start,
    fontColor: Color = Color(0xFF1A1A1A),
    backgroundColor: Color = Color.White,
    disableTextColor: Color = Color(0xFF1A1A1A).copy(alpha = 0.38f),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val isFocused = remember { mutableStateOf(false) }
    val hasValue = textValue.isNotEmpty()

    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> Color.Black.copy(alpha = 0.06f)
            isFocused.value -> Color.Black.copy(alpha = 0.3f)
            else -> Color.Black.copy(alpha = 0.08f)
        },
        animationSpec = tween(180),
        label = "border"
    )
    val bgColor by animateColorAsState(
        targetValue = when {
            !enabled -> Color(0xFFF0F0F0)
            isFocused.value -> Color.White
            else -> Color(0xFFF7F7F7)
        },
        animationSpec = tween(180),
        label = "bg"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (labelText.isNotEmpty()) {
            Text(
                text = labelText.uppercase(),
                fontSize = labelTextSize,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.07.em,
                color = if (hasValue || isFocused.value)
                    Color.Black.copy(alpha = 0.55f)
                else
                    Color.Black.copy(alpha = 0.38f)
            )
        }

        BasicTextField(
            value = textValue,
            onValueChange = { if (enabled && !readOnly) onValueChange(it) },
            singleLine = singleLine,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = TextStyle(
                fontSize = textValueSize,
                color = if (enabled) fontColor else disableTextColor,
                textAlign = textAlign
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions {
                keyboardController?.hide()
                keyboardAction()
            },
            cursorBrush = SolidColor(Color(0xFF1A1A1A)),
            modifier = Modifier
                .fillMaxWidth()

                .onFocusChanged { isFocused.value = it.isFocused },
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (singleLine) Modifier.height(52.dp)
                            else Modifier.defaultMinSize(minHeight = 52.dp)
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .background(bgColor)
                        .border(1.5.dp, borderColor, RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (textValue.isEmpty()) {
                            Text(
                                text = labelText,
                                fontSize = textValueSize,
                                color = Color.Black.copy(alpha = 0.22f),
                                textAlign = textAlign,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        innerTextField()
                    }
                    if (trailingIcon != null) {
                        Box(
                            modifier = Modifier.size(24.dp),
                            contentAlignment = Alignment.Center
                        ) { trailingIcon() }
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun InputEmailPreview() {
    InputWithTitleLabelCmp(
        labelText = "Email",
        textValue = "jose@ejemplo.com",
        keyboardType = KeyboardType.Email,
        onValueChange = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun InputPasswordPreview() {
    InputWithTitleLabelCmp(
        labelText = "Contraseña",
        textValue = "",
        keyboardType = KeyboardType.Password,
        visualTransformation = PasswordVisualTransformation(),
        trailingIcon = {
            Icon(
                Icons.Default.Visibility,
                contentDescription = null,
                tint = Color.Black.copy(alpha = 0.35f),
                modifier = Modifier.size(20.dp)
            )
        },
        onValueChange = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun InputDisabledPreview() {
    InputWithTitleLabelCmp(
        modifier = Modifier,
        labelText = "Solo lectura",
        textValue = "Valor fijo",
        backgroundColor = Color.Red,
        enabled = false,
        onValueChange = {}
    )
}
