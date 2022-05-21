package com.sora.dbhshopping.model

import com.sora.dbhshopping.util.UtilHelper
import java.io.Serializable
import java.util.*

data class Order(
    val id: String,
    val code: String,
    val totalPrice: Long,
    val totalDiscount: Long?,
    val firstName: String?,
    val lastName: String?,
    val province: String?,
    val district: String?,
    val address: String?,
    val phoneNumber: String?,
    val email: String?,
    val createdDate: Date,
    val orderItems: List<OrderItem>
): Serializable {
    fun getFullName() = "Họ và tên: " + lastName + " " + firstName

    fun getFullAddress() = "Địa chỉ: " + (address ?: "") + ", " + (district ?: "") + ", " + (province ?: "")

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

    fun getTotalPriceDiscountStr() : String? {
        if(totalDiscount == null || totalDiscount == 0L){
            return UtilHelper.formatAmount(totalPrice)
        }
        return UtilHelper.formatAmount(totalPrice - totalDiscount)
    }

    fun getEmailStr() = "Email: " + email ?: ""
    fun getPhoneNumberStr() = "SĐT: " + phoneNumber ?: ""
    fun getOrderCode() = "Đơn hàng: " + code
}
