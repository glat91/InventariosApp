package com.example.inventariosapp.ui.view.products

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.domain.repository.product.GetProductsRepositoryImp
import com.example.inventariosapp.model.product.ProductsResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsRepositoryImp,
) : ViewModel() {
    val baseViewModel = BaseViewModel()
    // region Productos
    val serverProducts = MutableStateFlow(false)
    val products: MutableState<ArrayList<ProductsResponseModel>> = mutableStateOf(arrayListOf())
    fun getProducts(){
        baseViewModel.showLoader()
        viewModelScope.launch {
            val v = getProductsUseCase(false)
            if (v.first != null){
                products.value = v.first!! as ArrayList<ProductsResponseModel>
                serverProducts.value = true
            }
            else{
                if (v.second != null){
                    MainActivity.mainDialogMsg.value = v.second!!.MsgError!!.errors!!.first().errorMessage!!
                }
                else{ MainActivity.mainDialogMsg.value = "Error 1001100" }
                MainActivity.mainDialog.value = true
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