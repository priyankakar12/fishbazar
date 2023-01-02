package com.pk.fishmarket.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.R
import com.pk.fishmarket.ResponseModel.CartDetails
import com.pk.fishmarket.Utils.AddToCartInterface

class CartAdapter (private val context: Context, private val modelList: ArrayList<CartDetails>, var addToCartInterface: AddToCartInterface) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    var weightArr : ArrayList<String> = ArrayList();
    var productQty = 0
    var count = 0
    var popup: PopupWindow? = null
    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        var shop_name = itemView.findViewById<TextView>(R.id.name)

        var price = itemView.findViewById<TextView>(R.id.price)
        var fish_weight = itemView.findViewById<TextView>(R.id.fish_weight)
        var qty = itemView.findViewById<TextView>(R.id.qty)
        var main_ll = itemView.findViewById<RelativeLayout>(R.id.main_ll)
        var amt_ll = itemView.findViewById<RelativeLayout>(R.id.amt_ll)
        var ll_add = itemView.findViewById<RelativeLayout>(R.id.ll_add)
        var ll_minus = itemView.findViewById<RelativeLayout>(R.id.ll_minus)
        var quantity_layout = itemView.findViewById<RelativeLayout>(R.id.quantity_layout)

        var product_img = itemView.findViewById<ImageView>(R.id.product_img)
        var img_delete = itemView.findViewById<ImageView>(R.id.img_delete)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_layout, parent, false)
        popup = PopupWindow(context);
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {

        holder.fish_weight.text = modelList[position].BASE_AMOUNT+" gms"
        holder.shop_name.text = modelList[position].PRODUCT_TITLE
        holder.qty.text = modelList[position].PRODUCT_QUANTITY
        holder.price.text = modelList[position].PRODUCT_PRICE
        Log.d("gvuvu",modelList[position].PRODUCT_PRICE)
        holder.img_delete.setOnClickListener{
            addToCartInterface.updateCart(modelList.get(position).PRODUCT_ID,"0",holder.qty.text.toString(),holder.price.text.toString(),"4",modelList[position].PRODUCT_QUANTITY,"","")

        }


        holder.ll_add.setOnClickListener {
            count = holder.qty.text.toString().toInt()
//                int quantity1 = Integer.parseInt(modelList.get(position).getVariationList().get(0).);
//                if (count >= quantity1) {
//                    // holder.add.setClickable(false);
//                    Toast.makeText(context, "Sorry current stock does not contain this amount.", Toast.LENGTH_SHORT).show();
//                } else {
            //                int quantity1 = Integer.parseInt(modelList.get(position).getVariationList().get(0).);
//                if (count >= quantity1) {
//                    // holder.add.setClickable(false);
//                    Toast.makeText(context, "Sorry current stock does not contain this amount.", Toast.LENGTH_SHORT).show();
//                } else {
            count = count + 1
            holder.qty.text = count.toString()
            productQty = holder.qty.text.toString().toInt()

            if (productQty >= 2) {
                //minus_jar.setEnabled(false);
                Log.e("QTYyy", "" + productQty)
            } else {
                //minus_jar.setEnabled(true);
                Log.e("QTYyy111", "" + productQty)
            }
            val actualPrice: String = java.lang.String.valueOf(
                modelList[position].PRODUCT_PRICE
            )
            val actprice = actualPrice.toFloat()
            var price1 = 0f
            price1 = actprice * count
            val priceActual = String.format("%.0f", price1)
            holder.price.setText(priceActual)
            addToCartInterface.updateCart(modelList.get(position).PRODUCT_ID,"0",holder.qty.text.toString(),
                holder.price.text.toString(),"1","",modelList[position].BASE_AMOUNT,modelList[position].BASE_PRICE)


        }
        holder.ll_minus.setOnClickListener {
            count = holder.qty.text.toString().toInt()
            if (count > 1) {
                count--
                holder.qty.text = count.toString()
                val actualPrice: String = java.lang.String.valueOf(
                    modelList[position].PRODUCT_PRICE
                )
                val actprice = actualPrice.toFloat()
                var price1 = 0f
                price1 = actprice * count
                val priceActual = String.format("%.0f", price1)
                holder.price.setText(priceActual)


                if (productQty >= 2) {
                    //minus_jar.setEnabled(false);
                    Log.e("QTYyy", "" + productQty)
                } else {
                    //minus_jar.setEnabled(true);
                    Log.e("QTYyy111", "" + productQty)
                }
                addToCartInterface.updateCart(modelList.get(position).PRODUCT_ID,"0",holder.qty.text.toString(),
                    holder.price.text.toString(),"0","",modelList[position].BASE_AMOUNT,modelList[position].BASE_PRICE)

            }
            else
            {
                addToCartInterface.updateCart(modelList.get(position).PRODUCT_ID,"0",holder.qty.text.toString(),holder.price.text.toString(),"4",modelList[position].PRODUCT_QUANTITY,"","")

            }
        }
    }




    override fun getItemCount(): Int {
        return modelList.size
    }
}