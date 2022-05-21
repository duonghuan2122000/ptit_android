package com.sora.dbhshopping.screen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sora.dbhshopping.R
import com.sora.dbhshopping.adapter.OrderItemAdapter
import com.sora.dbhshopping.model.Resource
import com.sora.dbhshopping.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultPaymentActivity : AppCompatActivity() {

    companion object {
        val CHANNEL_ID = "Order Status"
        val NOTIFICATION_ID = 1
    }

    private val orderViewModel: OrderViewModel by viewModels()

    private lateinit var tvStatus: TextView
    private lateinit var tvOrderCode: TextView
    private lateinit var tvName: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvPhoneNumber: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var tvTotalDiscount: TextView
    private lateinit var tvTotalPriceDiscount: TextView

    private lateinit var btnBackHome: Button

    private lateinit var orderItemAdapter: OrderItemAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_payment)

        val data: Uri? = intent?.data
        val orderId = data?.getQueryParameter("orderId")
        val status = data?.getQueryParameter("status")

        tvStatus = findViewById(R.id.result_payment_tv_status)
        tvOrderCode = findViewById(R.id.result_payment_tv_order_code)
        tvName = findViewById(R.id.result_payment_tv_name)
        tvAddress = findViewById(R.id.result_payment_tv_address)
        tvPhoneNumber = findViewById(R.id.result_payment_tv_phone_number)
        tvEmail = findViewById(R.id.result_payment_tv_email)
        tvTotalPrice = findViewById(R.id.result_payment_tv_total_price)
        tvTotalDiscount = findViewById(R.id.result_payment_tv_total_discount)
        tvTotalPriceDiscount = findViewById(R.id.result_payment_tv_total_price_discount)

        orderItemAdapter = OrderItemAdapter()
        recyclerView = findViewById(R.id.result_payment_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = orderItemAdapter

        btnBackHome = findViewById(R.id.result_payment_back_to_home)

        btnBackHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        orderViewModel.getOrder(orderId!!).observe(this, Observer { resource ->
            when(resource){
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    tvStatus.text = getStatusStr(status ?: "FAILURE")
                    tvOrderCode.text = resource.t.getOrderCode()
                    tvName.text = resource.t.getFullName()
                    tvAddress.text = resource.t.getFullAddress()
                    tvPhoneNumber.text = resource.t.getPhoneNumberStr()
                    tvEmail.text = resource.t.getEmailStr()
                    tvTotalPrice.text = resource.t.getTotalPriceFormat()
                    tvTotalDiscount.text = resource.t.getTotalDiscount()
                    tvTotalPriceDiscount.text = resource.t.getTotalPriceDiscountFormat()
                    orderItemAdapter.setList(resource.t.orderItems)

                    createNotificationPayment(getStatusStr(status ?: "FAILURE"), resource.t.code, resource.t.getTotalPriceDiscountStr()!!)
                }
                else -> {

                }
            }
        })

    }

    fun getStatusStr(status: String) = when(status){
        "SUCCESS" -> "Thanh toán thành công"
        else -> "Thanh toán thất bại"
    }

    fun createNotificationPayment(title: String, orderCode: String, amount: String){

        val mBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_shop)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentTitle(title)
            .setContentText("Đơn hàng " + orderCode + " với số tiền " + amount + " đã " + title)

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH)

            mBuilder.setChannelId(CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        mNotificationManager.notify(NOTIFICATION_ID /* Request Code */, mBuilder.build());
    }
}