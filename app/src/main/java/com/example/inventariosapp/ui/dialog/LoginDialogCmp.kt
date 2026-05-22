package com.example.inventariosapp.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.ui.component.ButtonCmp
import com.example.inventariosapp.ui.component.InputWithTitleLabelCmp
import com.example.inventariosapp.ui.theme.UI_BACKGROUND_Login
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Accept
import com.example.inventariosapp.ui.view.login.LoginUiState

@Composable
fun LoginDialogCmp(
    uiState: LoginUiState,
    onClickEnter: () -> Unit,
    onUserChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onClickRememberPassword: () -> Unit,
) {
    BasicDialogCmp(
        color = UI_Backround_Btn_Accept,
        backGroundColor = UI_BACKGROUND_Login,
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                InputWithTitleLabelCmp(
                    modifier = Modifier.background(Color.White),
                    labelText = "email",
                    textValue = uiState.user,
                    onValueChange = onUserChange
                )
                HorizontalDivider(thickness = 20.dp, color = Color.Transparent)
                InputWithTitleLabelCmp(
                    modifier = Modifier.background(Color.White),
                    labelText = "Password",
                    visualTransformation = PasswordVisualTransformation(),
                    textValue = uiState.password,
                    onValueChange = onPasswordChange
                )
                HorizontalDivider(thickness = 20.dp, color = Color.Transparent)
                ButtonCmp(
                    modifier = Modifier.width(200.dp),
                    text = "Entrar",
                    textSize = 15.sp,
                    backGroundColor = Color.White,
                    disableBackGroundColor = Color.Gray,
                    txtColor = Color.Black,
                    onClick = { onClickEnter() }
                )
                HorizontalDivider(thickness = 20.dp, color = Color.Transparent)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    RadioButton(
                        selected = uiState.rememberUser,
                        onClick = { onClickRememberPassword() }
                    )
                    TextCmp(
                        modifier = Modifier
                            .padding(start = 0.dp)
                            .clickable { onClickRememberPassword() },
                        color = Color.Black,
                        text = "Recordar password"
                    )
                }
            }
        },
        onDismiss = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoginDialogCmpPreview() {
    val uiState = LoginUiState()
    LoginDialogCmp(
        uiState = uiState,
        onClickEnter = {},
        onUserChange = {},
        onPasswordChange = {},
        onClickRememberPassword = {}
    )
}