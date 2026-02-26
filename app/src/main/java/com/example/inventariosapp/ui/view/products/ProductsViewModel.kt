package com.example.inventariosapp.ui.view.products

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.domain.use_case.product.GetProductsUseCase
import com.example.inventariosapp.domain.model.product.ProductsResponseModel
import com.example.inventariosapp.util.Helpers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    val baseViewModel: BaseViewModel,
    @ApplicationContext private val cnx: Context
) : ViewModel() {
    val internetUse = mutableStateOf(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
    // region Productos
    val serverProducts = MutableStateFlow(false)
    val products: MutableState<ArrayList<ProductsResponseModel>> = mutableStateOf(arrayListOf())
    fun getProducts(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            internetUse.value = Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value
            val v = getProductsUseCase(internetUse.value)
            if (v.first != null){
                products.value = v.first!! as ArrayList<ProductsResponseModel>
                serverProducts.value = true
            }
            baseViewModel.hideLoader()
        }
    }
    // endregion
    // region Search
    val search = mutableStateOf(TextFieldValue(""))
    val expandenSearchBar = mutableStateOf(false)
    val filterData: MutableState<ArrayList<ProductsResponseModel>> = mutableStateOf(arrayListOf())
    fun getFilter(): MutableState<ArrayList<ProductsResponseModel>> {
        filterData.value = if (search.value.text.isBlank()) {
            expandenSearchBar.value = false
            products.value
        }
        else {
            expandenSearchBar.value = true
            products.value.filter {
                it.descripcion!!.contains(search.value.text, ignoreCase = true)
            }
        } as ArrayList<ProductsResponseModel>

        return filterData
    }
    // endregion
    init {
        getProducts()
    }
}