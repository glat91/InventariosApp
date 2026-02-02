package com.example.inventariosapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp

@ExperimentalMaterial3Api
@Composable
fun SearchBarCmp(
    modifier: Modifier = Modifier,
    state: MutableState<TextFieldValue>,
    labelText: String = "",
    canModify: Boolean = true,
    opcionContent: @Composable () -> Unit,
    enableState: Boolean = true,
    shape: Shape = RoundedCornerShape(4.dp),
    onChangeText: (TextFieldValue) -> Unit = { },
) {
    Column(
        modifier = modifier.background(Color.Gray),
    ){
        TextField(modifier = Modifier
            .height(55.dp)
            .fillMaxWidth(),
            value = state.value,
            enabled = canModify,
            onValueChange = { if (canModify) onChangeText(it) },
            textStyle = TextStyle(color = Color.Black.copy(0.60f), fontSize = 16.sp),
            placeholder = {
                TextCmp(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize =  16.sp,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    text = labelText
                )
            },
            trailingIcon = {
                if (state.value.text != "") {
                    IconButton(
                        onClick = { if (canModify) onChangeText(TextFieldValue("")) }
                    ){
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            },
            singleLine = true,
            shape = shape,
            colors = TextFieldDefaults.colors (
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                disabledLeadingIconColor = Color.Black.copy(alpha = 0.38f),
                disabledTrailingIconColor = Color.Black.copy(alpha = 0.38f)
            )
        )
        opcionContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SearchCmpPreview(){
    val opcions: MutableState<ArrayList<String>> = remember { mutableStateOf(arrayListOf()) }
    opcions.value.add("Parametro numero 1")
    opcions.value.add("Parametro numero 2")
    opcions.value.add("Parametro numero 3")
    opcions.value.add("Parametro numero 4")
    opcions.value.add("Parametro numero 5")
    SearchBarCmp(
        state = remember {mutableStateOf(TextFieldValue("Para"))},
        onChangeText ={},
        opcionContent = {},
        labelText = "Seach",
    )
}