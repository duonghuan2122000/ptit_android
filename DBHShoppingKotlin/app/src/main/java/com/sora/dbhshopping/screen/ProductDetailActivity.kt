package com.sora.dbhshopping.screen

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.sora.dbhshopping.R
import com.sora.dbhshopping.model.AddToCartReq
import com.sora.dbhshopping.model.Product
import com.sora.dbhshopping.model.Resource
import com.sora.dbhshopping.viewmodel.CartViewModel
import com.sora.dbhshopping.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity() {

    private val productViewModel: ProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()

    private lateinit var ivImg: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDiscount: TextView
    private lateinit var btnAddToCart: Button

    private lateinit var layout: LinearLayout
    private lateinit var progressBar: ProgressBar

    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        ivImg = findViewById(R.id.product_iv)
        tvName = findViewById(R.id.product_tv_name)
        tvPrice = findViewById(R.id.product_tv_price)
        tvDiscount = findViewById(R.id.product_tv_discount)
        btnAddToCart = findViewById(R.id.product_btn_add_to_cart)

        layout = findViewById(R.id.product_layout)
        progressBar = findViewById(R.id.product_progress_bar)

        toolbar = findViewById(R.id.product_top_app_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }


        val product: Product = intent.extras?.get("product") as Product

        if(product == null){
            finish()
        }

        productViewModel.getById(product.id).observe(this, Observer { resource ->
            when(resource){
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    layout.visibility = View.GONE
                }
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    layout.visibility = View.VISIBLE

                    Glide.with(this)
                        .load(resource.t.thumbnail)
                        .into(ivImg)
                    tvName.text = resource.t.name
                    tvPrice.text = resource.t.getPriceFormat()
                    tvDiscount.text = resource.t.getPriceDiscountFormat()

                    if(resource.t.discount == null || resource.t.discount == 0L){
                        tvDiscount.visibility = View.GONE
                    } else {
                        tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    }
                }
            }
        })

        btnAddToCart.setOnClickListener {
            val addToCartReq = AddToCartReq(
                productId = product.id,
                cartId = cartViewModel.getCartId(),
                quantity = 1
            )
            productViewModel.addToCart(addToCartReq).observe(this, Observer { resource ->
                when(resource){
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        Snackbar.make(it, "Thêm vào giỏ hàng thành công", Snackbar.LENGTH_SHORT).show()
                    }
                    else -> {

                    }
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}