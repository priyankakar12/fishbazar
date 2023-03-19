package com.pk.fishmarket.Adapter


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.ProductDetailsActivity
import com.pk.fishmarket.R
import com.pk.fishmarket.ResponseModel.ShopResponse
import com.pk.fishmarket.Utils.AddToCartInterface
import com.squareup.picasso.Picasso


class ShopNearbyAdapter(private val context: Context, private val modelList: ArrayList<ShopResponse>,var addToCartInterface: AddToCartInterface) :
    RecyclerView.Adapter<ShopNearbyAdapter.ViewHolder>() {
    var weightArr : ArrayList<String> = ArrayList();
    var productQty = 0
    var count = 0
    var popup: PopupWindow? = null
    inner class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
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
        var shop_stock = itemView.findViewById<TextView>(R.id.shop_stock)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopNearbyAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.shop_nearby_layout, parent, false)
        popup = PopupWindow(context);
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ShopNearbyAdapter.ViewHolder, position: Int) {
        val itemHolder = holder as ViewHolder
        var url="http://freshfishbazar.com/Fishbazar/uploads/Product_image/"+modelList[position].PRODUCT_IMAGE
        Picasso.get().load(url).into(holder.product_img)
        itemHolder.shop_name.text = modelList[position].PRODUCT_NAME
        itemHolder.shop_stock.text = "Stock: "+modelList[position].PRODUCT_STOCK

        var PRODUCT_STOCK = modelList[position].PRODUCT_STOCK.toInt()
        itemHolder.amt_ll.setOnClickListener {
            weightArr = ArrayList()
            weightArr.add("1000g")
            weightArr.add("500g")
            weightArr.add("250g")
            showDialog(weightArr,itemHolder,modelList[position].PRODUCT_PRICE)
        }
        //holder.price.text = modelList[position].PRODUCT_PRICE
        var priceNew = modelList[position].PRODUCT_PRICE.toString().toInt()
        var priceUpdated = priceNew / 4
        itemHolder.price.text = modelList[position].PRODUCT_PRICE.toString()
        itemHolder.shop_name.setOnClickListener {
            var intent = Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra("productid",modelList.get(position).PRODUCT_ID)
            context.startActivity(intent)
        }
        itemHolder.add_ll.setOnClickListener {
           holder.quantity_layout.visibility = View.VISIBLE
           holder.add_ll.visibility = View.GONE
            Log.d("onClick","Clicked")
            var quantity_amount = ""
            if(holder.fish_weight.text.toString() == "1000g")
            {
                quantity_amount="1000"
            }
            else if(holder.fish_weight.text.toString() == "500g")
            {
                quantity_amount="500"
            }
            else
            {
                quantity_amount="250"
            }
            addToCartInterface.updateCart(modelList.get(position).PRODUCT_ID,
                modelList.get(position).SHOP_ID,itemHolder.qty.text.toString(),
                itemHolder.price.text.toString(),"1",quantity_amount,itemHolder.fish_weight.text.toString()
                ,modelList.get(position).PRODUCT_PRICE)
        }
        itemHolder.ll_add.setOnClickListener {
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
            if(count == PRODUCT_STOCK)
            {
             Toast.makeText(context,"This quantity is not available in stock",Toast.LENGTH_SHORT).show()
            }
            else
            {
                count = count + 1
                holder.qty.text = count.toString()
                productQty = holder.qty.text.toString().toInt()

                if (productQty >= 2) {
                    //minus_jar.setEnabled(false);
                    Log.e("QTYyy", "" + productQty)
                }
                else {
                    //minus_jar.setEnabled(true);
                    Log.e("QTYyy111", "" + productQty)
                }

                val actualPrice: String = java.lang.String.valueOf(
                    modelList[position].PRODUCT_PRICE
                )
                /*val actprice = actualPrice.toFloat()
                var price1 = 0f
                price1 = actprice * count
                val priceActual = String.format("%.0f", price1)
                holder.price.setText(priceActual)*/
                var quantity_amount = ""

                if(holder.fish_weight.text.toString() == "1000g")
                {
                    quantity_amount="1000"
                }
                else if(holder.fish_weight.text.toString() == "500g")
                {
                    quantity_amount="500"
                }
                else
                {
                    quantity_amount="250"
                }
                addToCartInterface.updateCart(modelList.get(position).PRODUCT_ID,modelList.get(position).SHOP_ID,
                    itemHolder.qty.text.toString(),itemHolder.price.text.toString(),"1",quantity_amount,
                    itemHolder.fish_weight.text.toString()
                    ,modelList.get(position).PRODUCT_PRICE)
            }



        }
        itemHolder.ll_minus.setOnClickListener {
            /*count = holder.qty.text.toString().toInt()
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
                addToCartInterface.updateCart(modelList.get(position).PRODUCT_ID,modelList.get(position).SHOP_ID,holder.qty.text.toString(),holder.price.text.toString(),"0")

            }*/
            Toast.makeText(context, "Please go to cart for updating.", Toast.LENGTH_SHORT).show()
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    private fun showDialog(weightArr: ArrayList<String>, holder: ViewHolder, productPrice: String) {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout: View = layoutInflater.inflate(R.layout.popup_layout_state, null, false)

        val main_layout_seasonlist =
            layout.findViewById<View>(R.id.main_layout_seasonlist) as LinearLayout

        // Creating the PopupWindow

        // Creating the PopupWindow
        popup!!.setContentView(layout)
        popup!!.setWidth(holder.amt_ll.getWidth())
        popup!!.setOutsideTouchable(true)
        popup!!.setFocusable(true)
        for (i in weightArr.indices) {
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            params.setMargins(10, 10, 10, 10)
            val tv = TextView(context)
            tv.layoutParams = params
            tv.setText(weightArr.get(i))
            tv.tag = weightArr.get(i)
            tv.setTextColor(Color.parseColor("#000000"))
            tv.setPadding(5, 5, 5, 5)
            tv.textSize = 15f
            val finalI: Int = i
            tv.setOnClickListener {
                var actualPrice = holder.price.text
                holder.fish_weight.text = weightArr.get(finalI)
                holder.qty.text = "0"
                if(holder.fish_weight.text.equals("250g"))
                {
                    var priceNew = productPrice.toString().toInt()
                    var priceUpdated = priceNew / 4
                    holder.price.text = priceUpdated.toString()
                }
                else if(holder.fish_weight.text.equals("500g"))
                {
                   var priceNew = productPrice.toString().toInt()
                    var priceUpdated = priceNew / 2
                    holder.price.text= priceUpdated.toString()
                }
                else
                {
                    var priceNew = productPrice.toString().toInt()
                    var priceUpdated = priceNew
                    holder.price.text= priceUpdated.toString()
                }

                popup!!.dismiss()
            }
            main_layout_seasonlist.addView(tv)

        }


        popup!!.setOnDismissListener(PopupWindow.OnDismissListener { // TODO Auto-generated method stub
            popup!!.dismiss()
        })


        popup!!.setBackgroundDrawable(BitmapDrawable())
        popup!!.showAsDropDown(holder.amt_ll)

    }


    override fun getItemCount(): Int {
        return modelList.size
    }
}