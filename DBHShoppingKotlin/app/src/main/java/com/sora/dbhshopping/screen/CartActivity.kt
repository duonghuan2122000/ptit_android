package com.sora.dbhshopping.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.sora.dbhshopping.R
import com.sora.dbhshopping.adapter.CartItemAdapter
import com.sora.dbhshopping.model.Resource
import com.sora.dbhshopping.model.UpdateCartItemQuantityReq
import com.sora.dbhshopping.viewmodel.CartViewModel
import com.sora.dbhshopping.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()

    private lateinit var cartItemAdapter: CartItemAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var btnCheckout: Button

    private lateinit var tvTotalPrice: TextView
    private lateinit var tvTotalDiscount: TextView
    private lateinit var tvTotalPriceDiscount: TextView

    private lateinit var btnBackHome: Button
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var layoutNotEmpty: NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartItemAdapter = CartItemAdapter()

        recyclerView = findViewById(R.id.cart_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = cartItemAdapter

        btnCheckout = findViewById(R.id.cart_btn_checkout)

        tvTotalPrice = findViewById(R.id.cart_tv_totalPrice)
        tvTotalDiscount = findViewById(R.id.cart_tv_totalDiscount)
        tvTotalPriceDiscount = findViewById(R.id.cart_tv_totalPriceDiscount)

        btnBackHome = findViewById(R.id.cart_btn_back_to_home)

        btnBackHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        layoutEmpty = findViewById(R.id.cart_layout_empty)
        layoutNotEmpty = findViewById(R.id.cart_layout_notEmpty)

        btnCheckout.setOnClickListener {
            if (cartItemAdapter.getList().size <= 0) {

            } else {
                // thực hiện tạo đơn hàng
                val cartId = cartViewModel.getCartId()

                orderViewModel.placeOrder(cartId).observe(this, Observer { resource ->
                    when (resource) {
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            val intent = Intent(this, CheckoutActivity::class.java).apply {
                                putExtra("orderId", resource.t.id)
                            }
                            startActivity(intent)
                        }
                        else -> {

                        }
                    }
                })
            }
        }

        toolbar = findViewById(R.id.cart_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        cartViewModel.getCart().observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (resource.t.cartItems.size > 0) {
                        tvTotalPrice.text = resource.t.getTotalPriceFormat()
                        tvTotalDiscount.text = resource.t.getTotalDiscount()
                        tvTotalPriceDiscount.text = resource.t.getTotalPriceDiscountFormat()
                        cartItemAdapter.setList(resource.t.cartItems.toMutableList())
                        layoutNotEmpty.visibility = View.VISIBLE
                        layoutEmpty.visibility = View.GONE
                        btnCheckout.visibility = View.VISIBLE
                    } else {
                        layoutEmpty.visibility = View.VISIBLE
                        layoutNotEmpty.visibility = View.GONE
                        btnCheckout.visibility = View.GONE
                    }

                }
                else -> {

                }
            }
        })

        cartItemAdapter.setOnChangeCartItemQuantity { cartItem ->
            val updateCartItemQuantityReq = UpdateCartItemQuantityReq(
                cartItemId = cartItem.cartItemId,
                quantity = cartItem.quantity
            )
            cartViewModel.updateCartItemQuantity(updateCartItemQuantityReq)
                .observe(this, Observer { resource ->
                    when (resource) {
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            tvTotalPrice.text = resource.t.getTotalPriceFormat()
                            tvTotalDiscount.text = resource.t.getTotalDiscount()
                            tvTotalPriceDiscount.text = resource.t.getTotalPriceDiscountFormat()
                            cartItemAdapter.setList(resource.t.cartItems.toMutableList())
                        }
                        else -> {

                        }
                    }
                })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}