package com.sora.dbhshopping.model

data class CheckoutReq(
    val id: String,
    val firstName: String?,
    val lastName: String?,
    val province: String?,
    val district: String?,
    val address: String?,
    val phoneNumber: String?,
    val email: String?,
    val successUrl: String,
    val failureUrl: String,
    val cartId: String
)