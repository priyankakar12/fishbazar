package com.pk.fishmarket.dashboard.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.Adapter.FavouritesShopAdapter
import com.pk.fishmarket.Adapter.OrderHistoryAdapter
import com.pk.fishmarket.R
import com.pk.fishmarket.ResponseModel.FavouriteItems.ORDERDETAILS
import com.pk.fishmarket.Utils.FavouriteDeleteInterface
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.DeleteFavouritesViewModel
import com.pk.fishmarket.viewmodel.FavouritesViewModel
import com.pk.fishmarket.viewmodel.OrderHistoryViewModel
import com.pk.fishmarket.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FavouritesItemsFragment : Fragment(),FavouriteDeleteInterface {

    lateinit var favouritesShopAdapter: FavouritesShopAdapter
    lateinit var rec_favouriteHistory:RecyclerView
    lateinit var favouritesViewModel:FavouritesViewModel
    lateinit var deleteFavouritesViewModel: DeleteFavouritesViewModel
    var arrayList :ArrayList<ORDERDETAILS> = ArrayList()
    var userid =""
    lateinit var noData: TextView
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_favourites_items, container, false)
        userid= SharedPreferencesUtil().getUserId(requireContext()).toString();
        rec_favouriteHistory= view.findViewById(R.id.rec_favouriteHistory)
        noData= view.findViewById(R.id.noData)
        progressBar= view.findViewById(R.id.progressBar)
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        favouritesViewModel = ViewModelProvider(this, factory)[FavouritesViewModel::class.java]
        deleteFavouritesViewModel = ViewModelProvider(this, factory)[DeleteFavouritesViewModel::class.java]

        getFavourites(userid)
        return view
    }

    private fun getFavourites(userid: String) {
        arrayList.clear()
        progressBar.visibility=View.VISIBLE
        favouritesViewModel.getFavouritesResponse(userid)
        favouritesViewModel.response.observe(requireActivity()) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        progressBar.visibility=View.GONE
                        arrayList.clear()

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                arrayList= response.body()!!.ORDER_DETAILS


                                    if(arrayList.size>0)
                                    {
                                        noData.visibility= View.GONE
                                        favouritesShopAdapter = FavouritesShopAdapter(requireContext(), arrayList,this)
                                        rec_favouriteHistory.adapter = favouritesShopAdapter
                                        rec_favouriteHistory.itemAnimator = DefaultItemAnimator()
                                        rec_favouriteHistory.layoutManager = GridLayoutManager(requireContext(), 1)

                                    }
                                    else
                                    {
                                        noData.visibility= View.VISIBLE
                                    }
                                }


                            else {
                                //Toast.makeText(requireContext(), "no data found", Toast.LENGTH_SHORT).show()
                                noData.visibility= View.VISIBLE
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

    override fun deleteItem(shopid: String) {
        deleteFavourites(userid,shopid)
    }

    private fun deleteFavourites(userid: String, shopid: String) {

        arrayList.clear()
        progressBar.visibility=View.VISIBLE
        deleteFavouritesViewModel.deleteFavourites(userid,shopid)
        deleteFavouritesViewModel.response.observe(requireActivity()) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        progressBar.visibility=View.GONE
                        arrayList.clear()

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                               getFavourites(userid)
                            }


                            else {
                                Toast.makeText(requireContext(), "no data found", Toast.LENGTH_SHORT).show()
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


}