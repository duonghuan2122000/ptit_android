package com.sora.dbhshopping.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.sora.dbhshopping.R
import com.sora.dbhshopping.screen.CartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment: Fragment() {

    private lateinit var viewCart: CardView

    private lateinit var viewAccount: CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewCart = view.findViewById(R.id.user_cart)
        viewCart.setOnClickListener {
            val intent = Intent(activity, CartActivity::class.java)
            startActivity(intent)
        }

        viewAccount = view.findViewById(R.id.user_account)
        viewAccount.setOnClickListener {
            val alert = AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage("Tính năng đang phát triển")
                .setPositiveButton("Đóng", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
                .show()
        }
    }
}