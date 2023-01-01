package com.pk.fishmarket.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.NearbyShopActivity
import com.pk.fishmarket.R
import com.pk.fishmarket.ResponseModel.ShopList
import com.squareup.picasso.Picasso

class HomeAdapter (private val context: Context, private val modelList: ArrayList<ShopList>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
    var shop_ll = itemView.findViewById<RelativeLayout>(R.id.shop_ll)
    var shop_name = itemView.findViewById<TextView>(R.id.shop_name)
    var currencyicon = itemView.findViewById<TextView>(R.id.currencyicon)
    var price = itemView.findViewById<TextView>(R.id.price)
    var favourites_icon = itemView.findViewById<ImageView>(R.id.favourites_icon)
    var rating5 = itemView.findViewById<ImageView>(R.id.rating5)
    var rating4 = itemView.findViewById<ImageView>(R.id.rating4)
    var rating3 = itemView.findViewById<ImageView>(R.id.rating3)
    var rating2 = itemView.findViewById<ImageView>(R.id.rating2)
    var rating1 = itemView.findViewById<ImageView>(R.id.rating1)
    var imgView = itemView.findViewById<ImageView>(R.id.imgView)



    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopslayout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
      //  Picasso.get().load(modelList[position].IMAGE_PATH).into(holder.imgView)
        var url="http://freshfishbazar.com/Fishbazar/uploads/Shop_image/"+modelList[position].SHOP_IMAGE
        Picasso.get().load(url).into(holder.imgView)
        holder.shop_name.text = modelList[position].SHOP_NAME
        holder.currencyicon.text = modelList[position].SHOP_OWNER_NAME
        holder.price.text = modelList[position].DISTANCE
        if(modelList[position].AVRAGERATING.toString() != "null")
        {

            var rating = modelList[position].AVRAGERATING.toString().toFloat().toInt()
            if(rating == 1)
            {
                holder.rating1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));
                holder.rating3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));
                holder.rating4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));
                holder.rating5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));

            }
            else if(rating == 2)
            {
                holder.rating1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));
                holder.rating4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));
                holder.rating5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));
            }
            else if(rating == 3)
            {
                holder.rating1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));
                holder.rating5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));
            }
            else if(rating == 4)
            {
                holder.rating1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));
            }
            else
            {
                holder.rating1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
                holder.rating5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_selected));
            }
        }
        else
        {
            holder.rating1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));
            holder.rating2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));
            holder.rating3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));
            holder.rating4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));
            holder.rating5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rating_unselected));

        }

        if(modelList[position].FAVOURITE == 1)
        {
            holder.favourites_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favourites_selected));
        }
        else
        {
            holder.favourites_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favourite_icon));
        }

         holder.shop_ll.setOnClickListener {
             if(modelList[position].AVRAGERATING.toString() == "null")
             {
                 var intent = Intent(context,NearbyShopActivity::class.java)
                 intent.putExtra("shop_id", modelList[position].SHOP_ID)
                 intent.putExtra("shop_name", modelList[position].SHOP_NAME)
                 intent.putExtra("address", modelList[position].SHOP_ADDRESS)
                 intent.putExtra("distance", modelList[position].DISTANCE)
                 intent.putExtra("favourites",modelList[position].FAVOURITE)
                 intent.putExtra("rating","0")
                 context.startActivity(intent)
             }
             else
             {
                 var intent = Intent(context,NearbyShopActivity::class.java)
                 intent.putExtra("shop_id", modelList[position].SHOP_ID)
                 intent.putExtra("shop_name", modelList[position].SHOP_NAME)
                 intent.putExtra("address", modelList[position].SHOP_ADDRESS)
                 intent.putExtra("distance", modelList[position].DISTANCE)
                 intent.putExtra("favourites",modelList[position].FAVOURITE)
                 intent.putExtra("rating",modelList[position].AVRAGERATING.toString().toFloat().toInt())
                 context.startActivity(intent)
             }

         }
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}