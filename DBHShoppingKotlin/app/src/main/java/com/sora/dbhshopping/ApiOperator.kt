package com.sora.dbhshopping

object ApiOperator {
    val baseUrl = "http://192.168.10.102:3000"

    val getListProductUrl = baseUrl + "/products"

    val getCart = baseUrl + "/carts"

    val getProduct = baseUrl + "/products/%s"

    val placeOrder = baseUrl + "/orders/place_order"

    val getOrder = baseUrl + "/orders/%s"

    val checkout = baseUrl + "/orders/checkout"

    val addToCart = baseUrl + "/carts/add_to_cart"

    val updateCartItemQuantity = baseUrl + "/carts/update_cart_item_quantity"

    val successUrl = "http://dbhuan.local/result_payment?status=SUCCESS&orderId=%s"

    val failureUrl = "http://dbhuan.local/result_payment?status=FAILURE&orderId=%s"
}