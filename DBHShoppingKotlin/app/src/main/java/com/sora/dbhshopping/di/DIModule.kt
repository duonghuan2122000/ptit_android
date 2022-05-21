package com.sora.dbhshopping.di

import com.sora.dbhshopping.service.CartService
import com.sora.dbhshopping.service.OrderService
import com.sora.dbhshopping.service.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object DIModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return Retrofit.Builder()
            .baseUrl("http://localhost")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideProductService(retrofit: Retrofit) =
        retrofit.create(ProductService::class.java)

    @Provides
    fun provideCartService(retrofit: Retrofit) =
        retrofit.create(CartService::class.java)

    @Provides
    fun provideOrderService(retrofit: Retrofit) =
        retrofit.create(OrderService::class.java)
}