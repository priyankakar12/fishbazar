package com.pk.fishmarket

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.Adapter.MoreLikeThisAdapter
import com.pk.fishmarket.ResponseModel.RelatedProducts
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.*
import com.squareup.picasso.Picasso


class ProductDetailsActivity : AppCompatActivity() {
    var productid=""
    lateinit var rec_more :RecyclerView
    lateinit var item_title :TextView
    lateinit var item_quantity :TextView
    lateinit var price :TextView
    lateinit var description_txt :TextView
    lateinit var  amt_ll:RelativeLayout
    lateinit var  qty:TextView
    lateinit var  fish_weight:TextView
    lateinit var  ll_minus:RelativeLayout
    lateinit var  ll_add:RelativeLayout
    lateinit var  tv_count: TextView
    lateinit var  imgView: ImageView

    lateinit var  cart_count: RelativeLayout
    lateinit var  submit_ll: RelativeLayout
    lateinit var  back_ll: RelativeLayout
    lateinit var  cart_ll: RelativeLayout
    var productQty = 0
    var count = 0
    var arrayMore :ArrayList<RelatedProducts> = ArrayList()
    lateinit var  productDetailsViewModel: ProductDetailsViewModel
    lateinit var  addToCartViewModel: AddToCartViewModel
    lateinit var  moreLikeThisAdapter: MoreLikeThisAdapter
    var priceNew =""
    var shopid =""
    var popup: PopupWindow? = null
    var userid =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        popup = PopupWindow(this);
        productid= intent.extras!!.getString("productid").toString()
        userid= SharedPreferencesUtil().getUserId(this).toString();
        rec_more =  findViewById(R.id.rec_more)
        qty = findViewById(R.id.qty)
         amt_ll = findViewById(R.id.amt_ll)
         ll_add = findViewById(R.id.ll_add)
         ll_minus = findViewById(R.id.ll_minus)
        fish_weight = findViewById(R.id.fish_weight)
        tv_count = findViewById(R.id.tv_count)
        cart_count = findViewById(R.id.cart_count)
        item_title = findViewById(R.id.item_title)
        description_txt = findViewById(R.id.description_txt)
        item_quantity = findViewById(R.id.item_quantity)
        price = findViewById(R.id.price)
        back_ll = findViewById(R.id.back_ll)
        imgView = findViewById(R.id.imgView)
        back_ll.setOnClickListener {
            finish()
        }
        cart_ll = findViewById(R.id.cart_ll)
        cart_ll.setOnClickListener { 
            startActivity(Intent(this,CartActivity::class.java))
        }
        submit_ll = findViewById(R.id.submit_ll)
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)

        productDetailsViewModel = ViewModelProvider(this, factory)[ProductDetailsViewModel::class.java]
        addToCartViewModel = ViewModelProvider(this, factory)[AddToCartViewModel::class.java]
        getProductDetails(productid)
        if(SharedPreferencesUtil().getCartCount(this) == "")
        {
            cart_count.visibility = View.GONE
        }
        else
        {
            cart_count.visibility = View.VISIBLE
            tv_count.text = SharedPreferencesUtil().getCartCount(this)
        }
        ll_add.setOnClickListener {
            count = qty.text.toString().toInt()
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
            qty.text = count.toString()
            productQty =qty.text.toString().toInt()

            if (productQty >= 2) {
                //minus_jar.setEnabled(false);
                Log.e("QTYyy", "" + productQty)
            } else {
                //minus_jar.setEnabled(true);
                Log.e("QTYyy111", "" + productQty)
            }
            val actualPrice: String = java.lang.String.valueOf(
                price.text.toString()
            )
            val actprice = actualPrice.toFloat()
            var price1 = 0f
            price1 = actprice * count
            val priceActual = String.format("%.0f", price1)
            price.setText(priceActual)

        }
        ll_minus.setOnClickListener {
            count = qty.text.toString().toInt()
            if (count > 1) {
                count--
                qty.text = count.toString()
                val actualPrice: String = java.lang.String.valueOf(
                   price.text.toString()
                )
                val actprice = actualPrice.toFloat()
                var price1 = 0f
                price1 = actprice * count
                val priceActual = String.format("%.0f", price1)
               price.setText(priceActual)


                if (productQty >= 2) {
                    //minus_jar.setEnabled(false);
                    Log.e("QTYyy", "" + productQty)
                } else {
                    //minus_jar.setEnabled(true);
                    Log.e("QTYyy111", "" + productQty)
                }
        }
        }
        submit_ll.setOnClickListener {
        addtocart(productid,
            shopid,
            qty.text.toString(),
            userid,
            price.text.toString(),
            "1",
        fish_weight.text.toString())
        }
        amt_ll.setOnClickListener {
           var  weightArr :ArrayList<String> = ArrayList()
            weightArr.add("1000g")
            weightArr.add("500g")
            weightArr.add("250g")
            shownewDialog(weightArr,priceNew)
        }
    }

    private fun addtocart(productid: String, shopid: String, product_quantity: String,userid:String, price: String,
                          status: String,quantity_amount:String) {
        addToCartViewModel.AddToCartItems(productid,shopid,product_quantity,userid,price,status,
            quantity_amount,"1000g", priceNew)
        addToCartViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                var cart_total = response.body()!!.cart_total
                                SharedPreferencesUtil().saveCartCount(cart_total,this)
                                cart_count.visibility = View.VISIBLE
                                tv_count.text = response.body()!!.cart_total

                                Toast.makeText(this, "cart updated succesfully", Toast.LENGTH_SHORT)
                                    .show()
                            } else {


                                Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    is Resource.Error -> {

                        response.message?.let { message ->

                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {


                    }
                }
            }
        }
    }

    private fun shownewDialog(weightArr: ArrayList<String>, priceNew: String) {
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout: View = layoutInflater.inflate(R.layout.popup_layout_state, null, false)

        val main_layout_seasonlist =
            layout.findViewById<View>(R.id.main_layout_seasonlist) as LinearLayout

        // Creating the PopupWindow

        // Creating the PopupWindow
        popup!!.setContentView(layout)
        popup!!.setWidth(amt_ll.getWidth())
        popup!!.setOutsideTouchable(true)
        popup!!.setFocusable(true)
        for (i in weightArr.indices) {
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            params.setMargins(10, 10, 10, 10)
            val tv = TextView(this)
            tv.layoutParams = params
            tv.setText(weightArr.get(i))
            tv.tag = weightArr.get(i)
            tv.setTextColor(Color.parseColor("#000000"))
            tv.setPadding(5, 5, 5, 5)
            tv.textSize = 15f
            val finalI: Int = i
            tv.setOnClickListener {
                var actualPrice = price.text
                fish_weight.text = weightArr.get(finalI)
                if(fish_weight.text.equals("250g"))
                {
                    var priceNew1 = priceNew.toString().toInt()
                    var priceUpdated = priceNew1 / 4
                    price.text = priceUpdated.toString()
                }
                else if(fish_weight.text.equals("500g"))
                {
                    var priceNew1 = priceNew.toString().toInt()
                    var priceUpdated = priceNew1 / 2
                    price.text = priceUpdated.toString()
                }
                else
                {

                    price.text= priceNew
                }

                popup!!.dismiss()
            }
            main_layout_seasonlist.addView(tv)

        }


        popup!!.setOnDismissListener(PopupWindow.OnDismissListener { // TODO Auto-generated method stub
            popup!!.dismiss()
        })


        popup!!.setBackgroundDrawable(BitmapDrawable())
        popup!!.showAsDropDown(amt_ll)

    }

    private fun getProductDetails(productid: String) {
       productDetailsViewModel.getDetails(productid)
        productDetailsViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                item_title.text = response.body()!!.PRODUCT_DETAILS[0].PRODUCT_NAME
                                item_quantity.text = "1000g"
                                price.text = response.body()!!.PRODUCT_DETAILS[0].PRODUCT_PRICE
                                priceNew = response.body()!!.PRODUCT_DETAILS[0].PRODUCT_PRICE
                                description_txt.text = response.body()!!.PRODUCT_DETAILS[0].PRODUCT_DESC
                                shopid = response.body()!!.PRODUCT_DETAILS[0].SHOP_ID
                                var url="http://freshfishbazar.com/Fishbazar/uploads/Product_image/"+response.body()!!.PRODUCT_DETAILS[0].PRODUCT_IMAGE
                                Picasso.get().load(url).into(imgView)
                                arrayMore = response.body()!!.RELATED_PRODUCTS
                                moreLikeThisAdapter = MoreLikeThisAdapter(this@ProductDetailsActivity, arrayMore)
                                rec_more.adapter = moreLikeThisAdapter

                            } else {


                                Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    is Resource.Error -> {

                        response.message?.let { message ->

                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {


                    }
                }
            }
        }

    }
}