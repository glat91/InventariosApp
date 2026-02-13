package com.example.inventariosapp.ui.component.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.database.entity.PostSaleEntity
import com.example.inventariosapp.database.entity.PostSaleWithProducts
import com.example.inventariosapp.ui.theme.PADDING_4
import com.example.inventariosapp.ui.theme.PADDING_8
import com.example.inventariosapp.util.CustomEnums

@Composable
fun CardPenndingSaleCmp(
    modifier: Modifier,
    data: PostSaleWithProducts,
    status: CustomEnums.StatusType,
    onClick: (PostSaleWithProducts) -> Unit,
) {
    val colorBackground = when(status){
        CustomEnums.StatusType.ERROR -> { Color.Red }
        CustomEnums.StatusType.PENDING -> { Color.LightGray }
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(colorBackground)
            .padding(PADDING_4)
            .clickable { onClick(data) }
        ,
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(2.dp, Color.Black.copy(alpha = .1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 0.dp),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PADDING_4)
                .clip(RoundedCornerShape(10.dp)),
        ) {
            val (venta_id, fecha, total, cliente_id) = createRefs()
            TextCmp(
                text = "Venta ID: ${data.sale.ventaId}",
                modifier = Modifier.constrainAs(venta_id){
                    top.linkTo(parent.top, PADDING_8)
                    start.linkTo(parent.start, PADDING_8)
                },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 2
            )
            TextCmp(
                text = "Fecha: ${data.sale.fechaIngreso}",
                modifier = Modifier.constrainAs(fecha){
                    top.linkTo(venta_id.bottom, PADDING_8)
                    start.linkTo(parent.start, PADDING_8)

                },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 2
            )
            TextCmp(
                text = "Total: ${data.sale.total}",
                modifier = Modifier.constrainAs(total){
                    top.linkTo(fecha.bottom, PADDING_8)
                    start.linkTo(parent.start, PADDING_8)
                },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 2
            )
            TextCmp(
                text = "Direccion: ${data.sale.direccion}",
                modifier = Modifier.constrainAs(cliente_id){
                    top.linkTo(total.bottom, PADDING_8)
                    start.linkTo(parent.start, PADDING_8)
                },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLine = 2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPenndingSaleCmpPreview(){
    CardPenndingSaleCmp(
        modifier = Modifier,
        data = PostSaleWithProducts(
            sale = PostSaleEntity(id = "1233245", fechaIngreso = "11-11-2025", direccion = "San felix #5653 col vista sol CP 67128", clienteId = 12345, total = 2000.20),
            productos = arrayListOf()
        ),
        status = CustomEnums.StatusType.ERROR,
        onClick = {}
    )
}