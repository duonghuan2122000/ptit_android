package com.sora.dbhshopping.model

import com.sora.dbhshopping.util.UtilHelper

data class CartItem(
    val cartItemId: String,
    var quantity: Int,
    val productId: String,
    val productName: String,
    val price: Long,
    val discount: Long,
    val productThumbnail: String
) {
    fun getPriceFormat(): String? = UtilHelper.formatAmount(price)

    fun getPriceDiscountFormat() : String? {
        if(discount == null || discount == 0L){
            return null
        }
        return UtilHelper.formatAmount(price - discount)
    }
}