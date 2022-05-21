package com.sora.dbhshopping.service

import com.sora.dbhshopping.model.Cart
import com.sora.dbhshopping.model.UpdateCartItemQuantityReq
import retrofit2.Response
import retrofit2.http.*

interface CartService {

    @GET
    suspend fun getCart(@Url url: String, @Query("id") id: String): Response<Cart>

    @POST
    @Headers("Content-Type: application/json")
    suspend fun updateCartItemQuantity(@Url url: String, @Body updateCartItemQuantityReq: UpdateCartItemQuantityReq): Response<Cart>
}