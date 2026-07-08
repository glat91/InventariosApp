package com.example.inventariosapp.ui.view.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.R
import com.example.inventariosapp.ui.component.BasicScreenCmp
import com.example.inventariosapp.ui.component.ButtonCmp
import com.example.inventariosapp.ui.component.InputWithTitleLabelCmp

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginView(
    uiState: LoginUiState,
    onClickEnter: () -> Unit,
    onUserChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onClickRememberPassword: () -> Unit,

) {
    val bringIntoViewRequester = remember {  BringIntoViewRequester() }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Scaffold(
            content = {
                BasicScreenCmp(
                    modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
                    content = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = R.drawable.login_bg),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .bringIntoViewRequester(bringIntoViewRequester)
                                ,
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Card(
                                    modifier = Modifier.padding(),
                                    elevation = CardDefaults.elevatedCardElevation(8.dp)
                                ){
                                    Image(
                                        painter = painterResource(id = R.drawable.rb_letters),
                                        contentDescription = "Logo de la app",
                                        modifier = Modifier.size(150.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                HorizontalDivider(thickness = 20.dp, color = Color.Transparent)
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
                                    enable = uiState.enableBtn,
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
                                        color = Color.White,
                                        text = "Recordar password"
                                    )
                                }

                            }
                        }
                    }
                )
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun LoginViewPreview(){
    val uiState = LoginUiState()
    LoginView(
        uiState = uiState,
        onClickEnter = {},
        onUserChange = {},
        onPasswordChange = {},
        onClickRememberPassword = {}
    )
}