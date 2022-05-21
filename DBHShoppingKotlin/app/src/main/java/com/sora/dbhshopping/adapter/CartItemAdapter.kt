package com.sora.dbhshopping.adapter

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sora.dbhshopping.R
import com.sora.dbhshopping.model.CartItem
import com.sora.dbhshopping.model.OrderItem

class CartItemAdapter constructor(): RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    private var list: MutableList<CartItem>

    init {
        list = mutableListOf()
    }

    private var onChangeCartItemQuantity: ((cartItem: CartItem) -> Unit)? = null

    fun setOnChangeCartItemQuantity(onChangeCartItemQuantity: ((cartItem: CartItem) -> Unit)){
        this.onChangeCartItemQuantity = onChangeCartItemQuantity
    }

    fun setList(list: MutableList<CartItem>){
        this.list = list
        notifyDataSetChanged()
    }

    fun getList(): MutableList<CartItem>{
        return list
    }

    class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var ivImg: ImageView
        private var tvName: TextView
        private var tvPrice: TextView
        private var tvDiscount: TextView
        var tvQuantity: TextView
        var btnSubtract: Button
        var btnAdd: Button
        init {
            ivImg = itemView.findViewById(R.id.item_cart_iv)
            tvName = itemView.findViewById(R.id.item_cart_tv_name)
            tvPrice = itemView.findViewById(R.id.item_cart_tv_price)
            tvDiscount = itemView.findViewById(R.id.item_cart_tv_discount)
            tvQuantity = itemView.findViewById(R.id.item_cart_quantity)
            btnSubtract = itemView.findViewById(R.id.item_cart_btn_substract)
            btnAdd = itemView.findViewById(R.id.item_cart_btn_add)
        }

        fun bindData(cartItem: CartItem){
            Glide.with(itemView)
                .load(cartItem.productThumbnail)
                .into(ivImg)

            tvName.text = cartItem.productName
            tvPrice.text = cartItem.getPriceFormat()
            tvDiscount.text = cartItem.getPriceDiscountFormat()
            tvQuantity.setText(cartItem.quantity.toString())

            if(cartItem.discount != null && cartItem.discount != 0L){
                tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder =
        CartItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cart_product, parent, false)
        )

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        var cartItem = list.get(position)
        holder.bindData(cartItem)
        holder.btnAdd.setOnClickListener {
            cartItem.quantity = cartItem.quantity + 1
            onChangeCartItemQuantity?.invoke(cartItem)
        }

        holder.btnSubtract.setOnClickListener {
            cartItem.quantity = cartItem.quantity - 1
            onChangeCartItemQuantity?.invoke(cartItem)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}