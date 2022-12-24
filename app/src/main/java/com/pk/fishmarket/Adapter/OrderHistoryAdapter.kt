package com.pk.fishmarket.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.OrderDetailsActivity
import com.pk.fishmarket.R
import com.pk.fishmarket.ResponseModel.OrderDetails
import com.pk.fishmarket.ResponseModel.PRODUCT_DETAILS

class OrderHistoryAdapter (private val context: Context, private val modelList: ArrayList<OrderDetails>) :
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {
    var arrayList : ArrayList<PRODUCT_DETAILS> = ArrayList()
    lateinit var orderDetailsAdapter:OrderDetailsAdapter

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        var order_date = itemView.findViewById<TextView>(R.id.order_date)
        var order_cancel = itemView.findViewById<TextView>(R.id.order_cancel)
        var name = itemView.findViewById<TextView>(R.id.name)
        var address = itemView.findViewById<TextView>(R.id.address)
        var phone_number = itemView.findViewById<TextView>(R.id.phone_number)
        var rec_orders = itemView.findViewById<RecyclerView>(R.id.rec_orders)
        var totalpayable = itemView.findViewById<TextView>(R.id.totalpayable)
        var mode_of_payment = itemView.findViewById<TextView>(R.id.mode_of_payment)
        var order_id = itemView.findViewById<TextView>(R.id.order_id)



    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderHistoryAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.orderhistorylayout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: OrderHistoryAdapter.ViewHolder, position: Int) {

    holder.order_id.setText("Order Id: " + modelList[position].ORDER_ID)
    holder.order_date.setText("Delivery Date: " + modelList[position].DELIVARY_DATE)
    holder.phone_number.setText("Phone : " + modelList[position].USER_PHONE)
    holder.totalpayable.setText("Total Payable : Rs." + modelList[position].TOTAL_PRICE)
    holder.mode_of_payment.setText("Delivery Date: " + modelList[position].PAYMENT_MODE)
    holder.name.setText("Name: " + modelList[position].USER_NAME)
    holder.order_date.setText("Delivery Date: " + modelList[position].DELIVARY_DATE)
    holder.address.setText("Address: " + modelList[position].USER_EMAIL)

      /*  if(modelList[position].PRODUCT_DETAILS.size > 0) {
            arrayList= modelList[position].PRODUCT_DETAILS
            orderDetailsAdapter = OrderDetailsAdapter(context, arrayList)
            holder.rec_orders.adapter = orderDetailsAdapter
            holder.rec_orders.itemAnimator = DefaultItemAnimator()
            holder.rec_orders.layoutManager = GridLayoutManager(context, 1)
        }*/
        holder.order_cancel.setOnClickListener {
            var intent = Intent(context,OrderDetailsActivity::class.java)
            intent.putExtra("orderid",modelList[position].ORDER_ID)
            intent.putExtra("phone",modelList[position].USER_PHONE)
            context.startActivity(intent)
            //Toast.makeText(context, "Order details in progress.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}