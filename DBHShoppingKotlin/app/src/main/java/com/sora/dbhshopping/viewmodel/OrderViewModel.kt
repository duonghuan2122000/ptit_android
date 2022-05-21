package com.sora.dbhshopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sora.dbhshopping.ApiOperator
import com.sora.dbhshopping.model.CheckoutReq
import com.sora.dbhshopping.model.Order
import com.sora.dbhshopping.model.Resource
import com.sora.dbhshopping.service.OrderService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val orderService: OrderService): ViewModel() {

    fun placeOrder(cartId: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        val placeOrderRes = orderService.placeOrder(ApiOperator.placeOrder, cartId)

        if(placeOrderRes.isSuccessful){
            emit(Resource.Success(placeOrderRes.body()!!))
        } else {
            emit(Resource.Error("Lỗi"))
        }
    }

    fun getOrder(orderId: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        val getOrderRes = orderService.getOrder(String.format(ApiOperator.getOrder, orderId))
        if(getOrderRes.isSuccessful){
            emit(Resource.Success(getOrderRes.body()!!))
        } else {
            emit(Resource.Error("Lỗi"))
        }
    }

    fun checkout(checkoutReq: CheckoutReq) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        val checkoutRes = orderService.checkout(ApiOperator.checkout, checkoutReq)
        if(checkoutRes.isSuccessful){
            emit(Resource.Success(checkoutRes.body()!!))
        } else {
            emit(Resource.Error("lỗi"))
        }
    }

}