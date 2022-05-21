package com.sora.dbhshopping.viewmodel

import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sora.dbhshopping.ApiOperator
import com.sora.dbhshopping.model.AddToCartReq
import com.sora.dbhshopping.service.ProductService
import com.sora.dbhshopping.model.Product
import com.sora.dbhshopping.model.Resource
import com.sora.dbhshopping.repository.ProductDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import retrofit2.http.GET
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productService: ProductService) :
    ViewModel() {

    companion object {
        val GET_PRODUCT_BY_ID = ApiOperator.getProduct
    }

    var productsLiveData: LiveData<PagedList<Product>>

    private val reloadTrigger = MutableLiveData<Boolean>()

    private val queryProduct = MutableLiveData<String>()

    fun refreshProducts() {
        reloadTrigger.value = true
    }

    init {
        val config = PagedList.Config
            .Builder()
            .setPageSize(10)
            .setEnablePlaceholders(true)
            .build()

        productsLiveData = Transformations.switchMap(DoubleTrigger(reloadTrigger, queryProduct)) {
            initPagedListBuilder(config).build()
        }

        refreshProducts()
    }

    fun setQueryProduct(query: String){
        queryProduct.value = query
    }

    private fun initPagedListBuilder(config: PagedList.Config): LivePagedListBuilder<Int, Product> {
        val productDataSourceFactory = object : DataSource.Factory<Int, Product>() {
            override fun create(): DataSource<Int, Product> {
                return ProductDataSource(viewModelScope, productService, queryProduct.value ?: "")
            }

        }
        return LivePagedListBuilder(productDataSourceFactory, config)
    }

    fun getById(id: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        val product = productService.getById(String.format(GET_PRODUCT_BY_ID, id))
        if (product != null) {
            emit(Resource.Success(product))
        } else {
            emit(Resource.Error(""))
        }

    }

    fun addToCart(addToCartReq: AddToCartReq) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        val addToCartRes = productService.addToCart(ApiOperator.addToCart, addToCartReq)
        if (addToCartRes.isSuccessful) {
            emit(Resource.Success(true))
        } else {
            emit(Resource.Error("Lỗi"))
        }
    }

    class DoubleTrigger<A, B>(a: LiveData<A>, b: LiveData<B>) : MediatorLiveData<Pair<A?, B?>>() {
        init {
            addSource(a) { value = it to b.value }
            addSource(b) { value = a.value to it }
        }
    }
}