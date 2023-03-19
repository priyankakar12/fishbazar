package com.pk.fishmarket.dashboard.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.pk.fishmarket.Adapter.HomeAdapter
import com.pk.fishmarket.Adapter.ShopSearchDashboardAdapter
import com.pk.fishmarket.ResponseModel.ShopList
import com.pk.fishmarket.ResponseModel.ShopResponse
import com.pk.fishmarket.Utils.InternetConnection
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil

import com.pk.fishmarket.repository.AppRepository

import com.google.android.material.snackbar.Snackbar
import com.pk.fishmarket.Adapter.ShopNearbyAdapter
import com.pk.fishmarket.CartActivity
import com.pk.fishmarket.Utils.AddToCartInterface
import com.pk.fishmarket.databinding.FragmentHomeBinding
import com.pk.fishmarket.viewmodel.*


class HomeFragment : Fragment(),AddToCartInterface {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var homeAdapter:HomeAdapter
    var arraySearchList:ArrayList<ShopResponse> = ArrayList()
    var arrayList:ArrayList<ShopResponse> = ArrayList()
    lateinit var dashboardViewModel: DashboardViewModel
    var address = ""
    var state = ""
    var userid = ""
    var latitude = ""
    var longitude = ""
    var mSnackbar: Snackbar? = null
    var arrayListSearch:ArrayList<ShopResponse> = ArrayList()
    lateinit var shopNearbyAdapter: ShopNearbyAdapter
    //lateinit var  searchItemViewModel: SearchShopViewModel
    lateinit var  searchItemViewModel: SearchItemViewModel
    lateinit var  addToCartViewModel: AddToCartViewModel
    lateinit var  shoplistViewModel:ShopListViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userid= SharedPreferencesUtil().getUserId(requireContext()).toString();
        address= SharedPreferencesUtil().getAddress(requireContext()).toString();
        state= SharedPreferencesUtil().getState(requireContext()).toString();
        latitude= SharedPreferencesUtil().getLat(requireContext()).toString();
        longitude= SharedPreferencesUtil().getLong(requireContext()).toString();
        binding.locationTxt.text  = address
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        searchItemViewModel = ViewModelProvider(this, factory)[SearchItemViewModel::class.java]
        dashboardViewModel = ViewModelProvider(this, factory)[DashboardViewModel::class.java]
        addToCartViewModel = ViewModelProvider(this, factory)[AddToCartViewModel::class.java]
        shoplistViewModel = ViewModelProvider(this, factory)[ShopListViewModel::class.java]

        if (InternetConnection.isConnected(requireContext())) {

          getData(userid,latitude,longitude)
            getShoplist("5")

        } else {
          Toast.makeText(requireContext(),"no internet connection",Toast.LENGTH_SHORT).show()
        }

        binding.searchTxt.addTextChangedListener(object : TextWatcher {
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
                    binding.recShops.visibility=View.GONE
                    binding.recShop.visibility=View.VISIBLE
                   /* var searchString = s.toString()
                    getSearchedData(searchString,userid,latitude,longitude)
*/
                    var searchString = s.toString()
                    getSearchedData(searchString)
                }
                else
                {
                    //getShoplist(shop_id)
                    binding.recShops.visibility=View.VISIBLE
                    binding.recShop.visibility=View.GONE
                    getData(userid,latitude,longitude)
                }
            }
        })
        binding.cartLl.setOnClickListener {
            startActivity(Intent(requireContext(), CartActivity::class.java))
        }
        return root
    }
    private fun getShoplist(shopId: String) {
        arrayList.clear()
        shoplistViewModel.getShopListResponse(shopId)
        shoplistViewModel.response.observe(requireActivity()) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        arrayList.clear()
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                arrayList= response.body()!!.PRODUCT_LIST
                                shopNearbyAdapter = ShopNearbyAdapter(requireContext(), arrayList,this)
                                binding.recShops.adapter = shopNearbyAdapter
                                binding.recShops.itemAnimator = DefaultItemAnimator()
                                binding.recShops.layoutManager = GridLayoutManager(requireContext(), 1)

                            } else {

                                Toast.makeText(requireContext(), "no data found", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    is Resource.Error -> {
                        arrayList.clear()
                        response.message?.let { message ->

                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {

                    }
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        getData(userid,latitude,longitude)
        getShoplist("5")
        if(SharedPreferencesUtil().getCartCount(requireContext()) == "")
        {
            binding.cartCount.visibility = View.GONE
        }
        else
        {
            binding.cartCount.visibility = View.VISIBLE
            binding.tvCount.text = SharedPreferencesUtil().getCartCount(requireContext())
        }
    }
  /*  private fun getSearchedData(
        searchString: String,
        userid: String,
        latitude: String,
        longitude: String
    ) {
        arrayListSearch.clear()
        arrayList.clear()
        binding.recShops.visibility = View.GONE
        searchItemViewModel.getSearchedItem(searchString,userid,latitude,longitude)
        searchItemViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {
                        arrayList.clear()
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                binding.recShops.visibility = View.VISIBLE
                                var arrayShopList = response.body()!!.SHOP_LIST
                                for(i in arrayShopList.indices)
                                {
                                    var AVRAGERATING =""
                                    var SHOP_ID =arrayShopList[i].SHOP_ID
                                   var SHOP_NAME =arrayShopList[i].SHOP_NAME
                                   var SHOP_OWNER_NAME =arrayShopList[i].SHOP_OWNER_NAME
                                   var SHOP_IMAGE =arrayShopList[i].SHOP_IMAGE
                                   var SHOP_ADDRESS =arrayShopList[i].SHOP_ADDRESS
                                   var SHOP_PHONE =arrayShopList[i].SHOP_PHONE
                                   var IMAGE_PATH =arrayShopList[i].IMAGE_PATH
                                   var DISTANCE =arrayShopList[i].DISTANCE
                                   var FAVOURITE =arrayShopList[i].FAVOURITE
                                    if(arrayShopList[i].AVRAGERATING == null)
                                    {
                                        AVRAGERATING = "0"
                                    }
                                    else
                                    {
                                        AVRAGERATING = arrayShopList[i].AVRAGERATING.toString()
                                    }
                                    var shopList = ShopList(SHOP_ID,SHOP_NAME,SHOP_OWNER_NAME,SHOP_IMAGE,SHOP_ADDRESS,SHOP_PHONE,
                                        IMAGE_PATH,DISTANCE,FAVOURITE,AVRAGERATING)
                                    arrayList.add(shopList)
                                }
                                if(arrayList.size > 0)
                                {
                                    homeAdapter = HomeAdapter(requireContext(), arrayList)
                                    binding.recShops.adapter = homeAdapter
                                    binding.recShops.itemAnimator = DefaultItemAnimator()
                                    binding.recShops.layoutManager = GridLayoutManager(requireContext(), 1)
                                }
                                else{
                                    binding.recShops.visibility = View.GONE

                                    Toast.makeText(requireContext(), "Data not found", Toast.LENGTH_SHORT)
                                        .show()
                                }



                            } else {
                                binding.recShops.visibility = View.GONE

                                Toast.makeText(requireContext(), "Data not found", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    is Resource.Error -> {
                        binding.recShops.visibility = View.GONE
                        response.message?.let { message ->

                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {
                        binding.recShops.visibility = View.GONE

                    }
                }
            }
        }
    }*/
  private fun getSearchedData(searchString: String) {
      arraySearchList.clear()
      binding.recShop.visibility = View.GONE
      searchItemViewModel.getSearchedItem(searchString)
      searchItemViewModel.response.observe(this) { event ->
          event.getContentIfNotHandled()?.let { response ->

              when (response) {
                  is Resource.Success -> {
                      arraySearchList.clear()
                      response.data?.let { response ->
                          Log.d("response", response.toString())
                          if (response.body()!!.status == 200) {
                              binding.recShop.visibility = View.VISIBLE
                              arraySearchList= response.body()!!.PRODUCT_LIST
                              shopNearbyAdapter = ShopNearbyAdapter(requireContext(), arraySearchList,this)
                              binding.recShop.adapter = shopNearbyAdapter
                              binding.recShop.itemAnimator = DefaultItemAnimator()
                              binding.recShop.layoutManager = GridLayoutManager(requireContext(), 1)

                          } else {
                              binding.recShop.visibility = View.GONE

                              Toast.makeText(requireContext(), "Data not found", Toast.LENGTH_SHORT)
                                  .show()
                          }

                      }
                  }
                  is Resource.Error -> {
                      binding.recShop.visibility = View.GONE
                      response.message?.let { message ->

                          Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                      }
                  }

                  is Resource.Loading -> {
                      binding.recShop.visibility = View.GONE

                  }
              }
          }
      }
  }
    private fun getData(userid: String, latitude: String, longitude: String) {
        arrayList.clear()
        dashboardViewModel.getDasboardResponse(userid,latitude,longitude)
        dashboardViewModel.response.observe(requireActivity()) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {
                        binding.progressbar.visibility = View.GONE
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                var cart_total = response.body()!!.cart_total
                                SharedPreferencesUtil().saveCartCount(cart_total,requireContext())
                              /*  arrayList = response.body()!!.SHOP_LIST
                                homeAdapter = HomeAdapter(requireContext(), arrayList)
                                binding.recShops.adapter = homeAdapter
                                binding.recShops.itemAnimator = DefaultItemAnimator()
                                binding.recShops.layoutManager = GridLayoutManager(requireContext(), 1)
*/

                            } else {

                                Toast.makeText(requireContext(), "Invalid phone number", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    is Resource.Error -> {
                        binding.progressbar.visibility = View.GONE
                        response.message?.let { message ->

                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun updateCart(
        productid: String,
        shopid: String,
        product_quantity: String,
        price: String,
        status: String,
        quantity_amount:String,
        base_amount:String,
        base_price:String
    ) {
        addToCartViewModel.AddToCartItems(productid,shopid,product_quantity,userid,price,status,quantity_amount,base_amount,base_price)
        addToCartViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                var cart_total = response.body()!!.cart_total
                                Log.d("hhib",cart_total)
                                SharedPreferencesUtil().saveCartCount(cart_total,requireContext())
                                binding.cartCount.visibility = View.VISIBLE
                                binding.tvCount.text = response.body()!!.cart_total

                                Toast.makeText(requireContext(), "cart updated succesfully", Toast.LENGTH_SHORT)
                                    .show()
                            } else {


                                Toast.makeText(requireContext(), "Data not found", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    is Resource.Error -> {

                        response.message?.let { message ->

                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {


                    }
                }
            }
        }
    }
}