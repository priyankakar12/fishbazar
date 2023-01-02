package com.pk.fishmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.Adapter.CartAdapter
import com.pk.fishmarket.ResponseModel.CartDetails
import com.pk.fishmarket.Utils.AddToCartInterface
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.*

class CartActivity : AppCompatActivity(), AddToCartInterface {
    var arrayList: ArrayList<CartDetails> = ArrayList()
    lateinit var rec_cart:RecyclerView

    lateinit var shop_name:TextView
    lateinit var total:TextView
    lateinit var delivery:TextView
    lateinit var subtotal:TextView
    lateinit var totaltxt:TextView
    lateinit var back_ll:RelativeLayout
    lateinit var main_ll:RelativeLayout
    lateinit var rl_more:RelativeLayout
    lateinit var checkout:RelativeLayout
    lateinit var cart_emp:RelativeLayout
    lateinit var cart_ll_layout:RelativeLayout
    lateinit var proceed_ll:RelativeLayout
    lateinit var tv_pro:ProgressBar
    var userid =""
    lateinit var getCartDetailsViewModel: GetCartDetailsViewModel
    lateinit var cartUpdateViewModel: CartUpdateViewModel
    lateinit var deleteCartIViewModel: DeleteCartIViewModel
    lateinit var cartAdapter: CartAdapter
    var latitude = ""
    var longitude = ""
    var distance = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        rec_cart=findViewById(R.id.rec_cart)

        back_ll=findViewById(R.id.back_ll)
        rl_more=findViewById(R.id.rl_more)
        total=findViewById(R.id.total)
        delivery=findViewById(R.id.delivery)
        subtotal=findViewById(R.id.subtotal)
        main_ll=findViewById(R.id.mainll)
        checkout=findViewById(R.id.checkout)
        shop_name=findViewById(R.id.shop_name)
        totaltxt=findViewById(R.id.totaltxt)
        cart_ll_layout=findViewById(R.id.cart_ll_layout)
        tv_pro=findViewById(R.id.tv_pro)
        proceed_ll=findViewById(R.id.proceed_ll)
        cart_emp=findViewById(R.id.cart_emp)

        latitude= SharedPreferencesUtil().getLat(this).toString();
        longitude= SharedPreferencesUtil().getLong(this).toString();
        userid= SharedPreferencesUtil().getUserId(this).toString();
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        getCartDetailsViewModel = ViewModelProvider(this, factory)[GetCartDetailsViewModel::class.java]
        cartUpdateViewModel = ViewModelProvider(this, factory)[CartUpdateViewModel::class.java]
        deleteCartIViewModel = ViewModelProvider(this, factory)[DeleteCartIViewModel::class.java]
        back_ll.setOnClickListener {
            finish()
        }
        rl_more.setOnClickListener {
            finish()
        }
        checkout.setOnClickListener {
            startActivity(Intent(this,CheckoutActivity::class.java))
        }

        getCartItems(userid)
    }

    private fun getCartItems(userid: String) {
        Log.d("latitude",latitude)
        Log.d("longitude",longitude)
        arrayList.clear()
        arrayList=ArrayList()
        tv_pro.visibility = View.VISIBLE
        getCartDetailsViewModel.getCartResponse(userid,latitude,longitude)
        getCartDetailsViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        arrayList.clear()
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                tv_pro.visibility = View.GONE
                                main_ll.visibility= View.VISIBLE
                                cart_ll_layout.visibility= View.VISIBLE
                                proceed_ll.visibility= View.VISIBLE
                                arrayList=response.body()!!.CART_DETAILS

                                var cart_total = arrayList.size.toString()
                                SharedPreferencesUtil().saveCartCount(cart_total,this)
                                subtotal.text=response.body()!!.SUBTOTAL
                                var sub_total=response.body()!!.SUBTOTAL.toString().toInt()
                                var distance = response.body()!!.CART_DETAILS[0].SHOP_DISTANCE.toString().toInt()
                                var distance_partial = distance - 2
                                var distance_charge = 35 + distance_partial

                                if(distance.equals("2"))
                                {
                                    var allTotal = sub_total+35
                                        delivery.text = "35"
                                    total.text = allTotal.toString()
                                    totaltxt.text = allTotal.toString()
                                }
                                else
                                {
                                    delivery.text = distance_charge.toString()
                                    var allTotal = sub_total+distance_charge
                                    total.text = allTotal.toString()
                                    totaltxt.text = allTotal.toString()


                                }
                                shop_name.text=response.body()!!.CART_DETAILS[0].SHOP_NAME
                                //total.text=response.body()!!.CART_DETAILS.PRODUCT_PRICE
                                cartAdapter = CartAdapter(this@CartActivity, arrayList,this)
                                rec_cart.adapter = cartAdapter
                                rec_cart.itemAnimator = DefaultItemAnimator()
                                rec_cart.layoutManager = GridLayoutManager(this@CartActivity, 1)

                            } else {
                                var cart_total = ""
                                SharedPreferencesUtil().saveCartCount(cart_total,this)
                                tv_pro.visibility = View.GONE
                                proceed_ll.visibility= View.GONE
                                cart_ll_layout.visibility= View.GONE
                                cart_emp.visibility= View.VISIBLE
                                Toast.makeText(this, "cart is empty", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    is Resource.Error -> {
                        arrayList.clear()
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

    override fun updateCart(
        productid: String,
        shopid: String,
        product_quantity: String,
        price: String,
        status: String,
        quantity_amount:String,
        base_amount:String,base_price:String
    ) {
        if(status.equals("4"))
        {
            deleteCartItem(productid,userid)
        }
        else
        {
            updateExistingCart(productid,product_quantity,userid,price,base_amount,base_price,status)
        }

    }

    private fun deleteCartItem(productid: String, userid: String) {
        arrayList.clear()
        deleteCartIViewModel.getReviewResponse(productid,userid)
        deleteCartIViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        arrayList.clear()
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                getCartItems(userid)
                                Toast.makeText(this,"Cart item has been deleted succesfully",Toast.LENGTH_LONG).show()
                            } else {

                                Toast.makeText(this, "cart is empty", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    is Resource.Error -> {
                        arrayList.clear()
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

    private fun updateExistingCart(
        productid: String,
        productQuantity: String,
        userid: String,
        price: String,
        base_amount: String,
        base_price: String,
        status: String
    ) {
        arrayList.clear()
        cartUpdateViewModel.getReviewResponse(productid,productQuantity,userid,price,base_amount,base_price,status)
        cartUpdateViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        arrayList.clear()
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                Log.d("productQty",productQuantity)
                                Log.d("status",status)
                                if(productQuantity == "1" && status == "0")
                                {
                                   deleteCartItem(productid,userid)
                                }
                                else
                                {
                                    getCartItems(userid)
                                    Toast.makeText(this,"Cart has been updated succesfully",Toast.LENGTH_LONG).show()

                                }

                                         } else {

                                Toast.makeText(this, "cart is empty", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    is Resource.Error -> {
                        arrayList.clear()
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