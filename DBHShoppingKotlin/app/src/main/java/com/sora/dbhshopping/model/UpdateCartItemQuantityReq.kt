package com.sora.dbhshopping.model

data class UpdateCartItemQuantityReq(
    val cartItemId: String,
    val quantity: Int
)
