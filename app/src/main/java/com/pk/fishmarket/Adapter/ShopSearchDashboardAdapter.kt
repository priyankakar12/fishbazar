package com.pk.fishmarket.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.ProductDetailsActivity
import com.pk.fishmarket.R
import com.pk.fishmarket.ResponseModel.ShopResponse

class ShopSearchDashboardAdapter(private val context: Context, private val modelList: ArrayList<ShopResponse>) :
    RecyclerView.Adapter<ShopSearchDashboardAdapter.ViewHolder>() {
    var weightArr : ArrayList<String> = ArrayList();
    var productQty = 0
    var count = 0
    var popup: PopupWindow? = null
    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        var shop_name = itemView.findViewById<TextView>(R.id.shop_name)

        var price = itemView.findViewById<TextView>(R.id.price)
        var fish_weight = itemView.findViewById<TextView>(R.id.fish_weight)
        var qty = itemView.findViewById<TextView>(R.id.qty)
        var main_ll = itemView.findViewById<RelativeLayout>(R.id.main_ll)
        var amt_ll = itemView.findViewById<RelativeLayout>(R.id.amt_ll)
        var ll_add = itemView.findViewById<RelativeLayout>(R.id.ll_add)
        var ll_minus = itemView.findViewById<RelativeLayout>(R.id.ll_minus)
        var quantity_layout = itemView.findViewById<RelativeLayout>(R.id.quantity_layout)
        var add_ll = itemView.findViewById<RelativeLayout>(R.id.add_ll)
        var product_img = itemView.findViewById<ImageView>(R.id.product_img)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopSearchDashboardAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.shop_search_layout, parent, false)
        popup = PopupWindow(context);
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ShopSearchDashboardAdapter.ViewHolder, position: Int) {

        holder.shop_name.text = modelList[position].PRODUCT_NAME

        holder.price.text = modelList[position].PRODUCT_PRICE
        holder.shop_name.setOnClickListener {
            var intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("productid",modelList.get(position).PRODUCT_ID)
            context.startActivity(intent)
        }

    }




    override fun getItemCount(): Int {
        return modelList.size
    }
}