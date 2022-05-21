package com.sora.dbhshopping.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.material.appbar.MaterialToolbar
import com.sora.dbhshopping.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        toolbar = findViewById(R.id.payment_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val checkoutUrl = intent.extras?.get("checkoutUrl") as String

        webView = findViewById(R.id.payment_webview)
        webView.loadUrl(checkoutUrl)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if(request?.url.toString().contains("dbhuan.local")){
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(request?.url)
                    startActivity(intent)
                    finish()
                }

                return super.shouldOverrideUrlLoading(view, request)
            }
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