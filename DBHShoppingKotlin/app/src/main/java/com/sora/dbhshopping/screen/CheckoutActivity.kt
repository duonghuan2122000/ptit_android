package com.sora.dbhshopping.screen

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.sora.dbhshopping.ApiOperator
import com.sora.dbhshopping.R
import com.sora.dbhshopping.adapter.CartItemAdapter
import com.sora.dbhshopping.adapter.OrderItemAdapter
import com.sora.dbhshopping.model.CheckoutReq
import com.sora.dbhshopping.model.Order
import com.sora.dbhshopping.model.Resource
import com.sora.dbhshopping.viewmodel.CartViewModel
import com.sora.dbhshopping.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity() {

    private val orderViewModel: OrderViewModel by viewModels()

    private val cartViewModel: CartViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderItemAdapter: OrderItemAdapter
    private lateinit var btnCheckout: Button

    private lateinit var edFirstName: EditText
    private lateinit var edLastName: EditText
    private lateinit var edProvince: EditText
    private lateinit var edDistrict: EditText
    private lateinit var edAddress: EditText
    private lateinit var edPhoneNumber: EditText
    private lateinit var edEmail: EditText

    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        recyclerView = findViewById(R.id.checkout_recycler_view)
        orderItemAdapter = OrderItemAdapter()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = orderItemAdapter

        edFirstName = findViewById(R.id.checkout_ed_firstname)
        edLastName = findViewById(R.id.checkout_ed_lastname)
        edProvince = findViewById(R.id.checkout_ed_province)
        edDistrict = findViewById(R.id.checkout_ed_district)
        edAddress = findViewById(R.id.checkout_ed_address)
        edPhoneNumber = findViewById(R.id.checkout_ed_phone)
        edEmail = findViewById(R.id.checkout_ed_email)

        btnCheckout = findViewById(R.id.checkout_btn_payment)

        val orderId = intent.extras?.get("orderId") as String

        if(orderId.isEmpty()){
            finish()
        }

        orderViewModel.getOrder(orderId).observe(this, Observer { resource ->
            when(resource){
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    orderItemAdapter.setList(resource.t.orderItems)
                }
                else -> {

                }
            }
        })

        btnCheckout.setOnClickListener {

            if(edFirstName.text.toString().isEmpty()
                || edLastName.text.toString().isEmpty()
                || edProvince.text.toString().isEmpty()
                || edDistrict.text.toString().isEmpty()
                || edAddress.text.toString().isEmpty()
                || edPhoneNumber.text.toString().isEmpty()
                || edEmail.text.toString().isEmpty()){
                AlertDialog.Builder(this)
                    .setTitle("Cảnh báo")
                    .setMessage("Vui lòng nhập đầy đủ thông tin khách hàng")
                    .setPositiveButton("Đóng", DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.cancel()
                    }).show()
                return@setOnClickListener
            }

            var checkoutOrder = CheckoutReq(
                id = orderId,
                firstName = edFirstName.text.toString(),
                lastName = edLastName.text.toString(),
                province = edProvince.text.toString(),
                district = edDistrict.text.toString(),
                address = edAddress.text.toString(),
                phoneNumber = edPhoneNumber.text.toString(),
                email = edEmail.text.toString(),
                successUrl = String.format(ApiOperator.successUrl, orderId),
                failureUrl = String.format(ApiOperator.failureUrl, orderId),
                cartId = cartViewModel.getCartId()
            )

            orderViewModel.checkout(checkoutOrder).observe(this, Observer { resource ->
                when(resource){
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        cartViewModel.removeCartId()
                        val intent = Intent(this, PaymentActivity::class.java).apply {
                            putExtra("checkoutUrl", resource.t.checkoutUrl)
                        }
                        startActivity(intent)
                    }
                    else -> {

                    }
                }
            })
        }
    }
}