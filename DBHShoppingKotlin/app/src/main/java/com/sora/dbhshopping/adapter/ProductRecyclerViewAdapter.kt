package com.sora.dbhshopping.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sora.dbhshopping.R
import com.sora.dbhshopping.model.Product

class ProductRecyclerViewAdapter(): PagedListAdapter<Product, ProductRecyclerViewAdapter.ProductRecyclerViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Product>(){
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.id.equals(newItem.id, true)

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.id.equals(newItem.id, true)

        }
    }

    private var itemClickListener: ((product: Product) -> Unit)? = null

    fun setOnItemClickListener(itemClickListener: ((product: Product) -> Unit)){
        this.itemClickListener = itemClickListener
    }

    class ProductRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var ivImg: ImageView
        private var tvName: TextView
        private var tvPrice: TextView
        private var tvDiscount: TextView

        init {
            ivImg = itemView.findViewById(R.id.item_product_iv)
            tvName = itemView.findViewById(R.id.item_product_tv_name)
            tvPrice = itemView.findViewById(R.id.item_product_tv_price)
            tvDiscount = itemView.findViewById(R.id.item_product_tv_điscount)
        }

        fun bindData(product: Product){
            tvName.text = product.name
            tvPrice.text = product.getPriceFormat()
            tvDiscount.text = product.getPriceDiscountFormat()
            if(product.discount != null && product.discount != 0L){
                tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            Glide.with(itemView)
                .load(product.thumbnail)
                .into(ivImg)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductRecyclerViewHolder =
        ProductRecyclerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product, parent, false)
        )

    override fun onBindViewHolder(holder: ProductRecyclerViewHolder, position: Int) {
        val product = getItem(position)
        holder.bindData(product!!)
        holder.itemView.setOnClickListener{
            itemClickListener?.invoke(product!!)
        }
    }

}