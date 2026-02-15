package com.example.inventariosapp.ui.view.products

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    val baseViewModel: BaseViewModel,
    @ApplicationContext private val cnx: Context
) : ViewModel() {
    var uiState by mutableStateOf(ProductsUiState())
    // region Productos
    fun getProducts(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            setInternetUse(Helpers.isInternetAvailable(cnx) && MainActivity.internetBtn.value)
            val v = getProductsUseCase(uiState.internetUse)
            if (v.first != null){
                setProducts(v.first!! as ArrayList<ProductsResponseModel>)
                setServerProducts(true)
            }
            baseViewModel.hideLoader()
        }
    }
    // endregion
    // region Search
    fun getFilter(): ArrayList<ProductsResponseModel> {
        setFilterData(
            if (uiState.search.text.isBlank()) {
                setExpandSearchBar(false)
                uiState.products
            }
            else {
                setExpandSearchBar(true)
                uiState.products.filter {
                    it.descripcion!!.contains(uiState.search.text, ignoreCase = true)
                }
            } as ArrayList<ProductsResponseModel>
        )

        return uiState.filterData
    }
    // endregion
    init {
        getProducts()
    }
    // region changue uiState
    fun setInternetUse(data: Boolean){ uiState = uiState.copy(internetUse = data) }
    fun setServerProducts(data: Boolean){ uiState = uiState.copy(serverProducts = data) }
    fun setProducts(data: ArrayList<ProductsResponseModel>){ uiState = uiState.copy(products = data) }

    fun setSearch(data: TextFieldValue){ uiState = uiState.copy(search = data) }
    fun setExpandSearchBar(data: Boolean){ uiState = uiState.copy(expandenSearchBar = data) }
    fun setFilterData(data: ArrayList<ProductsResponseModel>){ uiState = uiState.copy(filterData = data) }
    // endregion
}
data class ProductsUiState(
    var internetUse: Boolean = false,
    val serverProducts: Boolean = false,
    val products: ArrayList<ProductsResponseModel> = arrayListOf(),

    val search: TextFieldValue = TextFieldValue(""),
    val expandenSearchBar: Boolean = false,
    val filterData: ArrayList<ProductsResponseModel> = arrayListOf()
)