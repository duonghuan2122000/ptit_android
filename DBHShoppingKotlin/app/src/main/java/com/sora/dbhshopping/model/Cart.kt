package com.sora.dbhshopping.model

import com.sora.dbhshopping.util.UtilHelper
import java.util.*

data class Cart(
    val id: String,
    val totalPrice: Long,
    val totalDiscount: Long,
    val createdDate: Date,
    val cartItems: List<CartItem>
) {
    fun getTotalPriceFormat(): String? = "Tổng tiền: " + UtilHelper.formatAmount(totalPrice)

    fun getTotalDiscount(): String? {
        if(totalDiscount == null || totalDiscount == 0L){
            return "Khuyến mại: 0VNĐ"
        }
        return "Khuyến mại: " + UtilHelper.formatAmount(totalDiscount)
    }

    fun getTotalPriceDiscountFormat() : String? {
        if(totalDiscount == null || totalDiscount == 0L){
            return "Tổng: " + UtilHelper.formatAmount(totalPrice)
        }
        return "Tổng: " + UtilHelper.formatAmount(totalPrice - totalDiscount)
    }
}
