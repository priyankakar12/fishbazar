package com.pk.fishmarket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.R
import com.pk.fishmarket.ResponseModel.Product_Details_Success
import com.squareup.picasso.Picasso

class OrderDetailsAdapter (private val context: Context, private val modelList: ArrayList<Product_Details_Success>) :
    RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        var product_name = itemView.findViewById<TextView>(R.id.shop_name)
        var price = itemView.findViewById<TextView>(R.id.price)
        var qty = itemView.findViewById<TextView>(R.id.fish_weight)
        var product_img = itemView.findViewById<ImageView>(R.id.product_img)




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
        holder.qty.setText("Qty : " + modelList[position].QUANTITY_AMOUNT+"gm"+" x "+modelList[position].PRODUCT_QUANTITY)
        holder.price.setText("Rs." + modelList[position].PRODUCT_PRICE)
        var url="http://freshfishbazar.com/Fishbazar/uploads/Product_image/"+modelList[position].PRODUCT_IMAGE
        Picasso.get().load(url).into(holder.product_img)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}