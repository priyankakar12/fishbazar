package com.pk.fishmarket.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.R


import com.pk.fishmarket.ResponseModel.FavouriteItems.ORDERDETAILS
import com.pk.fishmarket.Utils.FavouriteDeleteInterface

class FavouritesShopAdapter  (private val context: Context, private val modelList: ArrayList<ORDERDETAILS>, var deleteFavourateItem: FavouriteDeleteInterface) :
    RecyclerView.Adapter<FavouritesShopAdapter.ViewHolder>() {

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        var shop_name = itemView.findViewById<TextView>(R.id.shop_name)
        var removeFavourites = itemView.findViewById<ImageView>(R.id.removeFavourites)




    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouritesShopAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.favouriteshoplayout, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: FavouritesShopAdapter.ViewHolder, position: Int) {

        holder.shop_name.text = modelList[position].SHOP_TITLE

        holder.removeFavourites.setOnClickListener{
            deleteFavourateItem.deleteItem(modelList.get(position).SHOP_ID)

        }


    }




    override fun getItemCount(): Int {
        return modelList.size
    }
}