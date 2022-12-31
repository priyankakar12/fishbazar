package com.pk.fishmarket.Adapter


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.EditAddressActivity
import com.pk.fishmarket.R
import com.pk.fishmarket.ResponseModel.AddressDetails
import com.pk.fishmarket.Utils.AddressInterface
import com.pk.fishmarket.networking.ApiInterface


internal class AddressAdapter(var context: Context, var modelList: List<AddressDetails>,var addressInterface: AddressInterface) :
    RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {

    var row_index = 0
    var tag = "Address"


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var fullname: TextView
        var phone_edt: TextView
        var address: TextView
        var img_delete: ImageView
        var main_ll: LinearLayout
        var img_edit: RelativeLayout


        init {
            fullname = view.findViewById(R.id.fullname)
            phone_edt = view.findViewById(R.id.phone_edt)
            address = view.findViewById(R.id.address)
            img_delete = view.findViewById(R.id.img_delete)
            main_ll = view.findViewById(R.id.main_ll)
            img_edit = view.findViewById(R.id.img_edit)

        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.address_layout, parent, false)

        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.fullname.text= modelList[position].NAME
        holder.phone_edt.text= modelList[position].PHONE
        holder.address.text=  modelList[position].ADDRESS_ONE+ ","+modelList[position].ADDRESS_TWO+","+modelList[position].PINCODE
        if (row_index == position) {

            AddressId = modelList[position].ADDRESS_ID
            holder.main_ll.setBackground(ContextCompat.getDrawable(context, R.drawable.search_box));
            //holder.main_ll.visibility = View.VISIBLE
        } else {
            holder.main_ll.setBackground(ContextCompat.getDrawable(context, R.drawable.address_uncheck));
            //holder.main_ll.setBackground(ContextCompat.getDrawable(context, R.drawable.ratingbox));
            //holder.main_ll.visibility = View.GONE
        }
        holder.img_delete.setOnClickListener {
            addressInterface.updateAddressSection(modelList[position].ADDRESS_ID,"Delete")
        }
        holder.main_ll.setOnClickListener{

                row_index = position
                notifyDataSetChanged()

            }
        holder.img_edit.setOnClickListener {
            var intent = Intent(context, EditAddressActivity::class.java)
            intent.putExtra("address_id",modelList[position].ADDRESS_ID)
            intent.putExtra("address_one",modelList[position].ADDRESS_ONE)
            intent.putExtra("address_two",modelList[position].ADDRESS_TWO)
            intent.putExtra("pincode",modelList[position].PINCODE)
            intent.putExtra("fullname",modelList[position].NAME)
            intent.putExtra("phone",modelList[position].PHONE)
            context.startActivity(intent)
        }

    }



    override fun getItemCount(): Int {
        return modelList.size
    }

    companion object {
        var AddressId = ""
    }
}