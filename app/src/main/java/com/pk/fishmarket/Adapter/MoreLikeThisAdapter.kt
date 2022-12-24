package com.pk.fishmarket.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.ProductDetailsActivity
import com.pk.fishmarket.R
import com.pk.fishmarket.ResponseModel.RelatedProducts

class MoreLikeThisAdapter (private val context: Context, private val modelList: ArrayList<RelatedProducts>) :
    RecyclerView.Adapter<MoreLikeThisAdapter.ViewHolder>() {

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        var main_ll = itemView.findViewById<CardView>(R.id.main_ll)
        var shop_name = itemView.findViewById<TextView>(R.id.shop_name)
        var currencyicon = itemView.findViewById<TextView>(R.id.currencyicon)
        var price = itemView.findViewById<TextView>(R.id.price)
        var fish_weight = itemView.findViewById<TextView>(R.id.fish_weight)




    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoreLikeThisAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.morelikelayout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: MoreLikeThisAdapter.ViewHolder, position: Int) {
        // Picasso.get().load(modelList[position].IMAGE_PATH).into(holder.imgView)
        holder.shop_name.text = modelList[position].PRODUCT_NAME

        holder.price.text = modelList[position].PRODUCT_PRICE
        holder.fish_weight.text = "250 g"
        holder.main_ll.setOnClickListener {
            var intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("productid",modelList.get(position).PRODUCT_ID)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}