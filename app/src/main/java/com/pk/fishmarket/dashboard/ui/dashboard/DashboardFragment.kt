package com.pk.fishmarket.dashboard.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.Adapter.OrderHistoryAdapter
import com.pk.fishmarket.R
import com.pk.fishmarket.ResponseModel.OrderDetails
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.OrderHistoryViewModel
import com.pk.fishmarket.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DashboardFragment : Fragment() {


    var arrayList: ArrayList<OrderDetails> = ArrayList()
    var filterArrayList: ArrayList<OrderDetails> = ArrayList()
    lateinit var orderHistoryAdapter: OrderHistoryAdapter
    var userid =""
    lateinit var orderHistoryViewModel: OrderHistoryViewModel
    lateinit var recOrderHistory:RecyclerView
    lateinit var currentOrderLL: LinearLayout
    lateinit var pastOrderLL:LinearLayout
    lateinit var currentTxt: TextView
    lateinit var pastTxt:TextView
    lateinit var noData:TextView
    var day =""
    lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        //_binding = FragmentDashboardBinding.inflate(inflater, container, false)
        var view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        userid= SharedPreferencesUtil().getUserId(requireContext()).toString();
        recOrderHistory= view.findViewById(R.id.rec_orderHistory)
        currentOrderLL= view.findViewById(R.id.currentOrderLL)
        pastOrderLL= view.findViewById(R.id.pastOrderLL)
        currentTxt= view.findViewById(R.id.curTxt)
        pastTxt= view.findViewById(R.id.pastTxt)
        noData= view.findViewById(R.id.noData)
        progressBar= view.findViewById(R.id.progressBar)

        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        orderHistoryViewModel = ViewModelProvider(this, factory)[OrderHistoryViewModel::class.java]
        getOrderDetails(userid)

        currentOrderLL.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.redsquarebtn));
        currentTxt.setTextColor(Color.WHITE)
        pastOrderLL.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.search_box));
        pastTxt.setTextColor(Color.BLACK)

        currentOrderLL.setOnClickListener {
            currentOrderLL.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.redsquarebtn));
            currentTxt.setTextColor(Color.WHITE)
            pastOrderLL.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.search_box));
            pastTxt.setTextColor(Color.BLACK)
             day = "today"
            getOrderDetails(userid)
        }

        pastOrderLL.setOnClickListener {
            currentOrderLL.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.search_box));
            currentTxt.setTextColor(Color.BLACK)
            pastOrderLL.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.redsquarebtn));
            pastTxt.setTextColor(Color.WHITE)
            day = "past"
            getOrderDetails(userid)
        }




        return view
    }

    private fun getOrderDetails(userid: String) {
        filterArrayList.clear()
        progressBar.visibility=View.VISIBLE
        orderHistoryViewModel.getOrderHistory(userid)
        orderHistoryViewModel.response.observe(requireActivity()) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        progressBar.visibility=View.GONE
                        arrayList.clear()
                        filterArrayList.clear()
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                arrayList= response.body()!!.ORDER_DETAILS
                                val c: Date = Calendar.getInstance().getTime()
                                println("Current time => $c")

                                val df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                val formattedDate: String = df.format(c)
                                Log.d("day",day)
                                if(day == "today" || day =="")
                                {
                                    for(i in arrayList.indices)
                                    {
                                        Log.d("day",formattedDate)
                                        Log.d("responseday",arrayList[i].DELIVARY_DATE)
                                        if(arrayList[i].DELIVARY_DATE == formattedDate)
                                        {
                                            filterArrayList.add(arrayList[i])
                                        }
                                    }
                                    if(filterArrayList.size>0)
                                    {
                                        noData.visibility= View.GONE
                                        orderHistoryAdapter = OrderHistoryAdapter(requireContext(), filterArrayList)
                                        recOrderHistory.adapter = orderHistoryAdapter
                                        recOrderHistory.itemAnimator = DefaultItemAnimator()
                                        recOrderHistory.layoutManager = GridLayoutManager(requireContext(), 1)

                                    }
                                    else
                                    {
                                        noData.visibility= View.VISIBLE
                                    }
                                }
                                else
                                {
                                    for(i in arrayList.indices)
                                    {
                                        Log.d("day",formattedDate)
                                        Log.d("responseday",arrayList[i].DELIVARY_DATE)
                                        if(arrayList[i].DELIVARY_DATE == formattedDate)
                                        {

                                        }
                                        else
                                        {
                                            filterArrayList.add(arrayList[i])
                                        }
                                    }
                                    if(filterArrayList.size>0)
                                    {
                                        noData.visibility= View.GONE
                                        orderHistoryAdapter = OrderHistoryAdapter(requireContext(), filterArrayList)
                                        recOrderHistory.adapter = orderHistoryAdapter
                                        recOrderHistory.itemAnimator = DefaultItemAnimator()
                                        recOrderHistory.layoutManager = GridLayoutManager(requireContext(), 1)

                                    }
                                    else
                                    {
                                        noData.visibility= View.VISIBLE
                                    }
                                }

                            } else {
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

    override fun onDestroyView() {
        super.onDestroyView()

    }
}