package com.sora.dbhshopping.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sora.dbhshopping.ApiOperator
import com.sora.dbhshopping.model.Cart
import com.sora.dbhshopping.model.Resource
import com.sora.dbhshopping.model.UpdateCartItemQuantityReq
import com.sora.dbhshopping.service.CartService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartService: CartService,
    @ApplicationContext private val context: Context
) :
    ViewModel() {

    companion object {
        val CART_ID_FILE = "com.sora.dbh_shopping.cart_id"
        val GET_CART_URL = ApiOperator.getCart
    }

    fun updateCartItemQuantity(updateCartItemQuantityReq: UpdateCartItemQuantityReq) =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading)
            val updateCartItemQuantityRes = cartService.updateCartItemQuantity(
                ApiOperator.updateCartItemQuantity,
                updateCartItemQuantityReq
            )
            if (updateCartItemQuantityRes.isSuccessful) {
                emit(Resource.Success(updateCartItemQuantityRes.body()!!))
            } else {
                emit(Resource.Error("Lỗi"))
            }
        }

    fun getCartId(): String {
        var cartId: String? = null
        var cartIdFile = File(context.cacheDir, CART_ID_FILE)

        if (!cartIdFile.exists()) {
            cartId = UUID.randomUUID().toString()
            cartIdFile.createNewFile()
            cartIdFile.writeText(cartId)
            return cartId
        }

        cartId = cartIdFile.readText()

        return cartId
    }

    fun removeCartId() {
        var cartIdFile = File(context.cacheDir, CART_ID_FILE)
        cartIdFile.deleteOnExit()
    }

    fun getCart() = liveData {
        emit(Resource.Loading)
        val cartId = getCartId()
        val responseGetCart = cartService.getCart(GET_CART_URL, cartId)
        if (responseGetCart.isSuccessful) {
            val cart = responseGetCart.body()
            emit(Resource.Success(cart!!))
        } else {
            emit(Resource.Error("Lỗi"))
        }
    }
}