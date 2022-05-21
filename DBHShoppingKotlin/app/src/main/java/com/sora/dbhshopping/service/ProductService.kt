package com.sora.dbhshopping.service

import com.sora.dbhshopping.model.AddToCartReq
import com.sora.dbhshopping.model.PagedResult
import com.sora.dbhshopping.model.Product
import retrofit2.Response
import retrofit2.http.*

interface ProductService {

    @GET
    suspend fun getList(
        @Url url: String,
        @Query("start") start: Int,
        @Query("limit") limit: Int,
        @Query("query") query: String
    ): PagedResult<Product>

    @GET
    suspend fun getById(@Url url: String): Product

    @POST
    @Headers("Content-Type: application/json")
    suspend fun addToCart(@Url url: String, @Body addToCartReq: AddToCartReq): Response<Any>

}