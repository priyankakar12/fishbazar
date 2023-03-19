package com.pk.fishmarket

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.Adapter.OrderDetailsAdapter
import com.pk.fishmarket.ResponseModel.PRODUCT_DETAILS
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.OrderCancelViewModel
import com.pk.fishmarket.viewmodel.OrderDetailsViewModel
import com.pk.fishmarket.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsActivity : AppCompatActivity() {
    var orderid= ""
    var userid =""
    var phone =""
    lateinit var order_id :TextView
    lateinit var order_date :TextView
    lateinit var delivery_date :TextView
    lateinit var rec_orders :RecyclerView
    lateinit var name :TextView
    lateinit var address :TextView
    lateinit var phone_number :TextView
    lateinit var mode_of_payment :TextView
    lateinit var totalpayable :TextView
    lateinit var orderstats :TextView
    lateinit var submit_ll :RelativeLayout
    lateinit var back_ll :RelativeLayout
    var arrayList : ArrayList<PRODUCT_DETAILS> = ArrayList()
    lateinit var orderDetailsAdapter: OrderDetailsAdapter
    lateinit var orderDetailsViewModel: OrderDetailsViewModel
    lateinit var orderCancelViewModel: OrderCancelViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        orderDetailsViewModel = ViewModelProvider(this, factory)[OrderDetailsViewModel::class.java]
        orderCancelViewModel = ViewModelProvider(this, factory)[OrderCancelViewModel::class.java]
        orderid = intent.extras!!.getString("orderid").toString()
        phone = intent.extras!!.getString("phone").toString()
        userid= SharedPreferencesUtil().getUserId(this).toString();

        order_date=findViewById(R.id.order_date)
        order_id=findViewById(R.id.order_id)
        delivery_date=findViewById(R.id.delivery_date)
        rec_orders=findViewById(R.id.rec_orders)
        name=findViewById(R.id.name)
        phone_number=findViewById(R.id.phone_number)
        mode_of_payment=findViewById(R.id.mode_of_payment)
        totalpayable=findViewById(R.id.totalpayable)
        submit_ll=findViewById(R.id.submit_ll)
        address=findViewById(R.id.address)
        back_ll=findViewById(R.id.back_ll)
        orderstats=findViewById(R.id.orderstats)
        submit_ll.setOnClickListener {
            cancelOrder(userid,orderid)
        }
        getOrderDetails(userid,orderid)
        back_ll.setOnClickListener {
            finish()
        }
    }

    private fun cancelOrder(userid: String, orderid: String) {
        orderCancelViewModel.cancelOrderHistory(userid,orderid)
        orderCancelViewModel.response.observe(this)
        {event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                              finish()
                                Toast.makeText(this, "Order has been cancelled", Toast.LENGTH_SHORT).show()

                            } else {

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

    private fun getOrderDetails(userid: String, orderid: String) {
        orderDetailsViewModel.getOrderDetailsHistory(userid,orderid)
        orderDetailsViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                val c: Date = Calendar.getInstance().getTime()
                                println("Current time => $c")

                                val df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                val formattedDate: String = df.format(c)
                                val dateCompare = df.parse(formattedDate)
                                var deliveryDate = response.body()!!.ORDER_DETAILS[0].DELIVARY_DATE
                                val format = SimpleDateFormat("dd-MM-yyyy",Locale.getDefault())
                                var orderStatus = response.body()!!.ORDER_DETAILS[0].ORDER_STATUS
                                try {
                                    val date = format.parse(deliveryDate)
                                    Log.d("date",date.toString())
                                    Log.d("date",deliveryDate.toString())


                                    if(date.compareTo(dateCompare) < 0 || orderStatus == "CANCEL")
                                    {
                                        submit_ll.visibility = View.GONE
                                    }
                                    else
                                    {
                                        submit_ll.visibility = View.VISIBLE
                                    }

                                } catch (e:Exception) {
                                    e.printStackTrace()
                                }

                              /*  if(orderStatus == "CANCEL")
                                {
                                    submit_ll.visibility = View.GONE
                                }
                                else
                                {
                                    submit_ll.visibility = View.VISIBLE
                                }*/
                                if(response.body()!!.ORDER_DETAILS[0].DELIVARY_STATUS == "0")
                                {
                                    orderstats.text= "Not Delivered"
                                    orderstats.setTextColor(ContextCompat.getColor(this,R.color.blue))
                                }
                                else if(response.body()!!.ORDER_DETAILS[0].DELIVARY_STATUS == "0")
                                {
                                    orderstats.text= "Delivered"
                                    orderstats.setTextColor(ContextCompat.getColor(this,R.color.green))
                                }
                                else
                                {
                                    orderstats.text= "Pending"
                                    orderstats.setTextColor(ContextCompat.getColor(this,R.color.grey))
                                }
                               /* if(deliveryDate == formattedDate)
                                {
                                    submit_ll.visibility = View.VISIBLE
                                }
                                else
                                {
                                    submit_ll.visibility = View.GONE
                                }*/
                                name.text = response.body()!!.ORDER_DETAILS[0].ADDRESS_DETAILS[0].NAME
                                phone_number.text = phone
                                order_id.text = "Order Id : "+response.body()!!.ORDER_DETAILS[0].ORDER_ID
                                order_date.text = "Order Date : "+response.body()!!.ORDER_DETAILS[0].ORDER_DATE
                                delivery_date.text = "Delivery Date : "+response.body()!!.ORDER_DETAILS[0].DELIVARY_DATE
                                address.text = "Address : "+response.body()!!.ORDER_DETAILS[0].ADDRESS_DETAILS[0].ADDRESS_ONE+","+response.body()!!.ORDER_DETAILS[0].ADDRESS_DETAILS[0].ADDRESS_TWO+","+response.body()!!.ORDER_DETAILS[0].ADDRESS_DETAILS[0].PINCODE
                                mode_of_payment.text = "Mode Of Payment : "+response.body()!!.ORDER_DETAILS[0].PAYMENT_MODE
                                totalpayable.text = "Total Payable : Rs."+response.body()!!.ORDER_DETAILS[0].TOTAL_PRICE+" including delivery charges"

                                var arrayList= response.body()!!.ORDER_DETAILS[0].PRODUCT_DETAILS
                                orderDetailsAdapter = OrderDetailsAdapter(this, arrayList)
                                rec_orders.adapter = orderDetailsAdapter
                                rec_orders.itemAnimator = DefaultItemAnimator()
                                rec_orders.layoutManager = GridLayoutManager(this, 1)

                            } else {

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