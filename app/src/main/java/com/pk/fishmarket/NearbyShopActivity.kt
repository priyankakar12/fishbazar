package com.pk.fishmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.Adapter.ShopNearbyAdapter
import com.pk.fishmarket.ResponseModel.ShopResponse
import com.pk.fishmarket.Utils.AddToCartInterface
import com.pk.fishmarket.Utils.InternetConnection
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.*
import com.google.android.material.snackbar.Snackbar

class NearbyShopActivity : AppCompatActivity(),AddToCartInterface {
    lateinit var back_ll :RelativeLayout
    lateinit var main_ll :RelativeLayout
    lateinit var search_txt :EditText
    lateinit var shopNearbyAdapter:ShopNearbyAdapter
    lateinit var recShop: RecyclerView
    var arrayList:ArrayList<ShopResponse> = ArrayList()
    lateinit var  shoplistViewModel:ShopListViewModel
    lateinit var  searchItemViewModel: SearchItemViewModel
    lateinit var  addToCartViewModel: AddToCartViewModel
    lateinit var  addtoFavouriteShopViewModel: AddtoFavouriteShopViewModel
    lateinit var  tv_count: TextView
    lateinit var  cart_count: RelativeLayout
    lateinit var  favouorites_icon_ll: RelativeLayout
    lateinit var  cart_ll: RelativeLayout

    var shop_id=""
    var shop_name=""
    var address=""
    var distance=""
    var favourites=""
    var rating=""
    var favouritesBoolean=false
    lateinit var  shopName :TextView
    lateinit var  location :TextView
    lateinit var  distance_txt :TextView
    lateinit var  add_rating :TextView
    lateinit var  rating_txt :TextView
    lateinit var  favourites_icon :ImageView
    var mSnackbar: Snackbar? = null
    var userid =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_shop)
        shop_id=getIntent().extras!!.getString("shop_id").toString()
        shop_name=getIntent().extras!!.getString("shop_name").toString()
        distance=getIntent().extras!!.getString("distance").toString()
        address=getIntent().extras!!.getString("address").toString()
        favourites=getIntent().extras!!.getInt("favourites").toString()
        rating=getIntent().extras!!.getInt("rating").toString()
        Log.d("favourites_icon",favourites)
        userid= SharedPreferencesUtil().getUserId(this).toString();
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        shoplistViewModel = ViewModelProvider(this, factory)[ShopListViewModel::class.java]
        searchItemViewModel = ViewModelProvider(this, factory)[SearchItemViewModel::class.java]
        addToCartViewModel = ViewModelProvider(this, factory)[AddToCartViewModel::class.java]
        addtoFavouriteShopViewModel = ViewModelProvider(this, factory)[AddtoFavouriteShopViewModel::class.java]

        recShop = findViewById(R.id.recShop)
        back_ll = findViewById(R.id.back_ll)
        shopName = findViewById(R.id.shop_name)
        location = findViewById(R.id.location)
        distance_txt = findViewById(R.id.distance_txt)
        main_ll = findViewById(R.id.main_ll)
        search_txt = findViewById(R.id.search_txt)
        add_rating = findViewById(R.id.add_rating)
        tv_count = findViewById(R.id.tv_count)
        cart_count = findViewById(R.id.cart_count)
        cart_ll = findViewById(R.id.cart_ll)
        favourites_icon = findViewById(R.id.favourites_icon)
        favouorites_icon_ll = findViewById(R.id.favouorites_icon_ll)
        rating_txt = findViewById(R.id.rating_txt)
        rating_txt.text=rating

        if(SharedPreferencesUtil().getCartCount(this) == "")
        {
            cart_count.visibility = View.GONE
        }
        else
        {
            cart_count.visibility = View.VISIBLE
            tv_count.text = SharedPreferencesUtil().getCartCount(this)
        }
        if(favourites.equals("1"))
        {
            favourites_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favourites_selected));
        }
        else
        {
            favourites_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favourite_icon));
        }
        back_ll.setOnClickListener {
            finish()
        }
        cart_ll.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))
        }
        add_rating.setOnClickListener {
            var intent = Intent(this@NearbyShopActivity,AddRatingActivity::class.java)
            intent.putExtra("shopid",shop_id)
            startActivity(intent)
        }
        favouorites_icon_ll.setOnClickListener{
           var favourite_key=""
            if(favourites.equals("1") && favouritesBoolean == false) {
                favourite_key="0"
                addToFavor(userid, shop_id,favourite_key)
            }
            if(favourites.equals("1") && favouritesBoolean == true) {
                favourite_key="1"
                addToFavor(userid, shop_id,favourite_key)
            }

            else if(favourites.equals("0") && favouritesBoolean == false)
            {
                favourite_key="1"
                addToFavor(userid, shop_id,favourite_key)
            }
            else if(favourites.equals("0") && favouritesBoolean == true)
            {
                favourite_key="0"
                addToFavor(userid, shop_id,favourite_key)
            }

            else
            {

            }
        }
        distance_txt.text = distance +" away"
        shopName.text = shop_name
        location.text = address
        if (InternetConnection.isConnected(this)) {

            getShoplist(shop_id)

        } else {
            mSnackbar = Snackbar
                .make(main_ll, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                .setAction(
                    "Ok"
                ) { mSnackbar!!.dismiss() }
            mSnackbar!!.show()
        }
        search_txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length != 0){
                    var searchString = s.toString()
                    getSearchedData(searchString)
                }
                else
                {
                    getShoplist(shop_id)
                }
            }
        })


    }

    override fun onResume() {
        super.onResume()
        if(SharedPreferencesUtil().getCartCount(this) == "")
        {
            cart_count.visibility = View.GONE
        }
        else
        {
            cart_count.visibility = View.VISIBLE
            tv_count.text = SharedPreferencesUtil().getCartCount(this)
        }
        if (InternetConnection.isConnected(this)) {

            getShoplist(shop_id)

        } else {
            mSnackbar = Snackbar
                .make(main_ll, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                .setAction(
                    "Ok"
                ) { mSnackbar!!.dismiss() }
            mSnackbar!!.show()
        }
    }

    private fun addToFavor(userid: String, shopId: String, favouriteKey: String) {

        addtoFavouriteShopViewModel.addtoFavouriteShopItems(userid,shopId,favouriteKey)
        addtoFavouriteShopViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {

                                favourites_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favourites_selected));

                                Toast.makeText(this, "Favourites have been updated", Toast.LENGTH_SHORT)
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

    private fun getSearchedData(searchString: String) {
        arrayList.clear()
        recShop.visibility = View.GONE
        searchItemViewModel.getSearchedItem(searchString)
        searchItemViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {
                        arrayList.clear()
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                recShop.visibility = View.VISIBLE
                                arrayList= response.body()!!.PRODUCT_LIST
                                shopNearbyAdapter = ShopNearbyAdapter(this@NearbyShopActivity, arrayList,this)
                                recShop.adapter = shopNearbyAdapter
                                recShop.itemAnimator = DefaultItemAnimator()
                                recShop.layoutManager = GridLayoutManager(this@NearbyShopActivity, 1)

                            } else {
                                recShop.visibility = View.GONE

                                Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    is Resource.Error -> {
                        recShop.visibility = View.GONE
                        response.message?.let { message ->

                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {
                        recShop.visibility = View.GONE

                    }
                }
            }
        }
    }

    private fun getShoplist(shopId: String) {
        arrayList.clear()
        shoplistViewModel.getShopListResponse(shopId)
        shoplistViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        arrayList.clear()
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                arrayList= response.body()!!.PRODUCT_LIST
                                shopNearbyAdapter = ShopNearbyAdapter(this@NearbyShopActivity, arrayList,this)
                                recShop.adapter = shopNearbyAdapter
                                recShop.itemAnimator = DefaultItemAnimator()
                                recShop.layoutManager = GridLayoutManager(this@NearbyShopActivity, 1)

                            } else {

                                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT)
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
        status: String,quantity_amount:String
    ) {
        addToCartViewModel.AddToCartItems(productid,shopid,product_quantity,userid,price,status,quantity_amount)
        addToCartViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                var cart_total = response.body()!!.cart_total
                                Log.d("hhib",cart_total)
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
}