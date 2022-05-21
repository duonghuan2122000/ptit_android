package com.sora.dbhshopping.model

sealed class Resource<out T: Any> {
    object Loading: Resource<Nothing>()
    data class Success<out T: Any>(val t: T): Resource<T>()
    data class Error(val msg: String): Resource<Nothing>()
}