package com.sora.dbhshopping.service

import com.sora.dbhshopping.model.Checkout
import com.sora.dbhshopping.model.CheckoutReq
import com.sora.dbhshopping.model.Order
import retrofit2.Response
import retrofit2.http.*

interface OrderService {

    @POST
    suspend fun placeOrder(@Url url: String, @Query("cartId") cartId: String): Response<Order>

    @GET
    suspend fun getOrder(@Url url: String): Response<Order>

    @POST
    @Headers("Content-Type: application/json")
    suspend fun checkout(@Url url: String, @Body checkoutReq: CheckoutReq): Response<Checkout>
}