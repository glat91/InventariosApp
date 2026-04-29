package com.example.inventariosapp.ui.view.new_sale

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.inventariosapp.domain.model.product.ProductsResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AddProductUiState(
    val state: TextFieldValue = TextFieldValue(""),
    val quantity: String = "",
    val product: ProductsResponseModel? = null,
    val comentarios: String = "",
    val expanded: Boolean = false,
    val precio1: Boolean = false,
    val precio2: Boolean = false,
    val precio3: Boolean = false,
    val precio4: Boolean = false,
    val enableBtn: Boolean = false
)
@HiltViewModel
class AddProductDialogViewModel @Inject constructor() : ViewModel( ){
    // Internal mutable state flow
    private val _uiState = MutableStateFlow(AddProductUiState())
    var uiState: StateFlow<AddProductUiState> = _uiState

    fun updateState(newState: AddProductUiState) {
        _uiState.value = newState
    }

    // State updates
    fun togglePrecio(index: Int) {
        _uiState.update { state ->
            state.copy(
                precio1 = if (index == 0) !state.precio1 else state.precio1,
                precio2 = if (index == 1) !state.precio2 else state.precio2,
                precio3 = if (index == 2) !state.precio3 else state.precio3,
                precio4 = if (index == 3) !state.precio4 else state.precio4
            )
        }
    }

    fun setQuantity(value: String) {
        _uiState.update { it.copy(quantity = value) }
        updateEnableBtn()
    }

    private fun updateEnableBtn() {
        _uiState.update { it.copy(enableBtn = isEnabled(it)) }
    }

    fun setProduct(product: ProductsResponseModel) {
        _uiState.update { it.copy(product = product) }
    }

    fun resetData() {
        _uiState.value = AddProductUiState()
    }

    // Helper method
    private fun isEnabled(state: AddProductUiState): Boolean {
        return (state.precio1 || state.precio2 || state.precio3 || state.precio4) && state.quantity.isNotEmpty()
    }
}