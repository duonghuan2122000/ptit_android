package com.sora.dbhshopping.model

data class AddToCartReq(
    val cartId: String,
    val productId: String,
    val quantity: Int
)