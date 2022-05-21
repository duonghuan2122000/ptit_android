package com.sora.dbhshopping.repository

import androidx.paging.PageKeyedDataSource
import com.sora.dbhshopping.ApiOperator
import com.sora.dbhshopping.service.ProductService
import com.sora.dbhshopping.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductDataSource @Inject constructor(
    private val scope: CoroutineScope,
    private val productService: ProductService,
    private val query: String
) : PageKeyedDataSource<Int, Product>() {

    companion object {
        val GET_PRODUCT_LIST_URL = ApiOperator.getListProductUrl
        val GET_PRODUCT_LIST_LIMIT = 10
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Product>
    ) {
        scope.launch {
            val pagedResult =
                productService.getList(GET_PRODUCT_LIST_URL, 0, GET_PRODUCT_LIST_LIMIT, query)
            callback.onResult(pagedResult.items ?: listOf(), null, 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
        scope.launch {
            val pagedResult = productService.getList(
                GET_PRODUCT_LIST_URL,
                params.key * GET_PRODUCT_LIST_LIMIT,
                GET_PRODUCT_LIST_LIMIT,
                query
            )
            callback.onResult(pagedResult.items, params.key + 1)
        }
    }
}