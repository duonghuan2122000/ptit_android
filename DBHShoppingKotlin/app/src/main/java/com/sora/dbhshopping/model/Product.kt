package com.sora.dbhshopping.model

import com.sora.dbhshopping.util.UtilHelper
import java.io.Serializable

data class Product(
    val id: String,
    val name: String,
    val sku: String,
    val price: Long,
    val discount: Long,
    val thumbnail: String
): Serializable {
    fun getPriceFormat(): String? = UtilHelper.formatAmount(price)

    fun getPriceDiscountFormat() : String? {
        if(discount == null || discount == 0L){
            return null
        }
        return UtilHelper.formatAmount(price - discount)
   }

}
