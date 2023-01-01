package com.pk.fishmarket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.R
import com.pk.fishmarket.ResponseModel.Product_Details_Success

class OrderDetailsAdapter (private val context: Context, private val modelList: ArrayList<Product_Details_Success>) :
    RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        var product_name = itemView.findViewById<TextView>(R.id.product_name)
        var price = itemView.findViewById<TextView>(R.id.price)
        var qty = itemView.findViewById<TextView>(R.id.qty)




    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderDetailsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.orderdetailslayout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: OrderDetailsAdapter.ViewHolder, position: Int) {
        holder.product_name.setText(modelList[position].PRODUCT_NAME)
        holder.qty.setText("Qty : " + modelList[position].PRODUCT_QUANTITY)
        holder.price.setText("Rs." + modelList[position].PRODUCT_PRICE)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}