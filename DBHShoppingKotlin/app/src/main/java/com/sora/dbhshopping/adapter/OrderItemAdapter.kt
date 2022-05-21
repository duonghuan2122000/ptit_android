package com.sora.dbhshopping.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sora.dbhshopping.R
import com.sora.dbhshopping.model.OrderItem

class OrderItemAdapter : RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>() {

    private var list: List<OrderItem>

    init {
        list = listOf()
    }

    fun setList(list: List<OrderItem>){
        this.list = list
        notifyDataSetChanged()
    }

    class OrderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var ivImg: ImageView
        private var tvName: TextView
        private var tvPrice: TextView
        private var tvDiscount: TextView
        private var tvQuantity: TextView
//        private var edQuantity: EditText
        init {
            ivImg = itemView.findViewById(R.id.item_cart_iv)
            tvName = itemView.findViewById(R.id.item_cart_tv_name)
            tvPrice = itemView.findViewById(R.id.item_cart_tv_price)
            tvDiscount = itemView.findViewById(R.id.item_cart_tv_discount)
            tvQuantity = itemView.findViewById(R.id.item_cart_quantity)
//            edQuantity = itemView.findViewById(R.id.item_cart_ed_quantity)
        }

        fun bindData(orderItem: OrderItem){
            Glide.with(itemView)
                .load(orderItem.productThumbnail)
                .into(ivImg)

            tvName.text = orderItem.productName
            tvPrice.text = orderItem.getPriceFormat()
            tvDiscount.text = orderItem.getPriceDiscountFormat()
            tvQuantity.text = "Số lượng: " + orderItem.quantity
//            edQuantity.setText(orderItem.quantity.toString())

            if(orderItem.discount != null && orderItem.discount != 0L){
                tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder =
        OrderItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_order_product, parent, false)
        )

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val orderItem = list.get(position)
        holder.bindData(orderItem)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}