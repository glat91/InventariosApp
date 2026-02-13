package com.example.inventariosapp.ui.component.lazyColumn

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appgeneric.ui.component.TextCmp
import com.example.inventariosapp.domain.model.product.ProductsResponseModel
import com.example.inventariosapp.ui.theme.TEXT_List_Title
import com.example.inventariosapp.ui.theme.UI_Divier
import com.example.inventariosapp.ui.theme.UI_List_Row_1
import com.example.inventariosapp.ui.theme.UI_List_Row_2
import com.example.inventariosapp.ui.theme.UI_List_Title
import java.util.ArrayList

@Composable
fun InventoryListCmp(
    titleColumn: List<String>,
    dbData: MutableState<ArrayList<ProductsResponseModel>>,
    onClickRow: (ProductsResponseModel) -> Unit
) {
    Column(
        modifier = Modifier
            .padding()
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(UI_List_Title)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextCmp(
                text = titleColumn.get(0),
                modifier = Modifier.width(60.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = TEXT_List_Title
            )
            TextCmp(
                text = titleColumn.get(1),
                modifier = Modifier.width(120.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = TEXT_List_Title
            )
            TextCmp(
                text = titleColumn.get(2),
                modifier = Modifier.width(60.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = TEXT_List_Title
            )
            TextCmp(
                text = titleColumn.get(3),
                modifier = Modifier.width(60.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = TEXT_List_Title
            )
        }
        // Lista con los datos
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            var switchColor = true
            items(dbData.value) {
                var colorRow = if (switchColor) UI_List_Row_1 else UI_List_Row_2
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState())
                        .background(colorRow)
                        .padding(8.dp)
                        .clickable { onClickRow(it) }
                    ,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextCmp(
                        maxLine = 1,
                        text = it. codigo.toString(),
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center
                    )

                    TextCmp(
                        maxLine = 4,
                        text = it.descripcion.toString(),
                        modifier = Modifier.width(120.dp),
                        textAlign = TextAlign.Center
                    )
                    TextCmp(
                        maxLine = 4,
                        text = it.precioVenta1.toString(),
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center
                    )
                    TextCmp(
                        maxLine = 4,
                        text = it.precioVenta2.toString(),
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = UI_Divier, )
                switchColor = !switchColor
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun InventoryListCmpPreview(){
    InventoryListCmp(
        titleColumn = arrayListOf("Cant", "Producto", "Stock Min", "Stock Max"),
        dbData = remember { mutableStateOf(arrayListOf()) },
        onClickRow = { }
    )
}