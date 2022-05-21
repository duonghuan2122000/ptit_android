package com.sora.dbhshopping.model

data class PagedResult<T>(
    val totalRecord: Int,
    val items: List<T>
)