package com.example.inventariosapp.ui.component.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.local.entity.PostSaleEntity
import com.example.inventariosapp.local.entity.PostSaleWithProducts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import com.example.inventariosapp.util.CustomEnums

@Composable
fun CardPenndingSaleCmp(
    modifier: Modifier,
    data: PostSaleWithProducts,
    orderStatus: CustomEnums.OrderStatus,
    onClick: (PostSaleWithProducts) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable{ onClick(data) }
            .then(
                if (orderStatus == CustomEnums.OrderStatus.ERROR) {
                    Modifier.border(
                        width = 2.dp,
                        color = Color(0xFFFF4444),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                } else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (orderStatus == CustomEnums.OrderStatus.ERROR)
                Color(0xFFFFF5F5)
            else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextCmp(
                    text = data.sale.nombreCliente ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLine = 1
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (orderStatus == CustomEnums.OrderStatus.ERROR)
                        Color(0xFFFFE5E5)
                    else Color(0xFFF0F0F0),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (orderStatus == CustomEnums.OrderStatus.ERROR) {
                            Surface(
                                shape = RoundedCornerShape(2.dp),
                                color = Color(0xFFFF4444),
                                modifier = Modifier.size(6.dp)
                            ) {}
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        TextCmp(
                            text = orderStatus.name,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (orderStatus == CustomEnums.OrderStatus.ERROR)
                                Color(0xFFFF4444)
                            else Color(0xFF666666)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextCmp(
                    text = "Cliente #" + data.sale.clienteId.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    TextCmp(
                        text = "TOTAL",
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    TextCmp(
                        text = data.sale.total.toString(),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    TextCmp(
                        text = "FECHA",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    TextCmp(
                        text = data.sale.fechaVenta ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextCmp(
                text = "DIRECCIÓN",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextCmp(
                text = data.sale.direccion,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Badge de productos
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF5F5F5),
                modifier = Modifier.align(Alignment.Start)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextCmp(
                        text = data.productos.size.toString() + " productos",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

// Preview con los 2 casos de la imagen
@Preview(showBackground = true)
@Composable
private fun CardPenndingSaleCmpPreview() {
    CardPenndingSaleCmp(
        modifier = Modifier,
        data = PostSaleWithProducts(
            sale = PostSaleEntity(
                nombreCliente = "Juan Perez",
                id = "1233245",
                fechaIngreso = "11-11-2025",
                direccion = "San felix #5653 col vista sol CP 67128",
                clienteId = 12345,
                total = 2000.20
            ),
            productos = arrayListOf()
        ),
        onClick = {},
        orderStatus = CustomEnums.OrderStatus.PENDIENTE
    )
}