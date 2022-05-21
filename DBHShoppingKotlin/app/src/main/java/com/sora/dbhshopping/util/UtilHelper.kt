package com.sora.dbhshopping.util

import java.text.DecimalFormat

object UtilHelper {
    fun formatAmount(amount: Long): String? {
        val df = DecimalFormat("###,###,###")
        if(amount != null){
            return df.format(amount) + "VNĐ"
        }
        return null
    }
}