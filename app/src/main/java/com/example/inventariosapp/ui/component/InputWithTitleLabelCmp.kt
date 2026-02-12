package com.example.inventariosapp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.ui.theme.Black

@Composable
fun InputWithTitleLabelCmp(
    modifier: Modifier = Modifier,
    labelText: String = "",
    labelTextSize: TextUnit = 16.sp,
    keyboardType: KeyboardType = KeyboardType.Password,
    imeAction: ImeAction = ImeAction.Default,
    keyboardAction: () -> Unit = {},
    textValue: String = "",
    textValueSize: TextUnit = 18.sp,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textAlign: TextAlign = TextAlign.Start,
    labelColor: Color = Black,
    fontColor: Color = Black,
    disableTextColor: Color = Color.Gray,
    disableBackgroundColor: Color = Color.Gray,
    unfocusedIndicatorColor: Color = Color.Black,
    focusedIndicatorColor: Color = Color.Black,
    backgroundColor: Color = Color.Transparent,
    textFieldModifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
) {
    // region Vars
    val unfocusedLabelColor = if(textValue.length > 0){ labelColor }else{ fontColor }
    val keyboardController = LocalSoftwareKeyboardController.current
    val nModifier = if (singleLine) {
        modifier
            .fillMaxWidth()
            .height(55.dp)
    } else { modifier.fillMaxWidth() }
    // endregion
    Column(modifier = nModifier.clip(RoundedCornerShape(1.dp))) {
        TextField(
            modifier = textFieldModifier.fillMaxWidth(),
            value = textValue,
            singleLine = singleLine,
            colors = TextFieldDefaults.colors(
                focusedTextColor = focusedIndicatorColor,
                unfocusedTextColor = unfocusedIndicatorColor,
                disabledTextColor = disableTextColor,
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                disabledContainerColor = disableBackgroundColor,
                selectionColors = LocalTextSelectionColors.current,
                focusedIndicatorColor = focusedIndicatorColor,
                unfocusedIndicatorColor = unfocusedIndicatorColor,
                disabledIndicatorColor = unfocusedIndicatorColor,
                focusedLabelColor = labelColor,
                unfocusedLabelColor = unfocusedLabelColor,
                disabledLabelColor = unfocusedLabelColor,
            ),
            visualTransformation = visualTransformation,
            textStyle = LocalTextStyle.current.copy(
                fontSize = textValueSize,
                textAlign = textAlign,
            ),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = keyboardType,
                imeAction = imeAction,
            ),
            keyboardActions = KeyboardActions {
                keyboardController?.hide()
                keyboardAction()
            },
            onValueChange = { onValueChange(it) },
            label = {
                TextCmp(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = if(textValue.isNotEmpty()) 12.sp else labelTextSize,
                    textAlign = textAlign,
                    overflow = TextOverflow.Ellipsis,
                    text = labelText
                )

            },
            trailingIcon = trailingIcon,
            enabled = enabled,
            readOnly = readOnly,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputCmpPreview(){
    InputWithTitleLabelCmp(
        labelText = "email",
        textValue = "afg",
        onValueChange ={
            it
        }
    )
}