package com.pk.fishmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.EditAddressViewModel
import com.pk.fishmarket.viewmodel.ViewModelFactory

class EditAddressActivity : AppCompatActivity() {
    lateinit var back_ll: RelativeLayout
    lateinit var submit_ll: RelativeLayout
    lateinit var full_name_edt: EditText
    lateinit var addressone_edt: EditText
    lateinit var addresstwo_edt: EditText
    lateinit var pincode_edt: EditText
    lateinit var phone_edt: EditText
    var userid =""

    var Address_id=""
    var Address_one=""
    var Address_two=""
    var Pincode=""
    var Phone_number=""
    var full_name=""
    lateinit var  editAddressModel: EditAddressViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_address)
        userid= SharedPreferencesUtil().getUserId(this).toString();
        back_ll=findViewById(R.id.back_ll)
        submit_ll=findViewById(R.id.submit_ll)
        full_name_edt=findViewById(R.id.full_name_edt)
        addressone_edt=findViewById(R.id.addressone_edt)
        addresstwo_edt=findViewById(R.id.addresstwo_edt)
        pincode_edt=findViewById(R.id.pincode_edt)
        phone_edt=findViewById(R.id.phone_edt)
        Address_id = intent.extras!!.getString("address_id").toString()
        Address_one = intent.extras!!.getString("address_one").toString()
        Address_two = intent.extras!!.getString("address_two").toString()
        Pincode = intent.extras!!.getString("pincode").toString()
        full_name = intent.extras!!.getString("fullname").toString()
        Phone_number = intent.extras!!.getString("phone").toString()
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)

        editAddressModel = ViewModelProvider(this, factory)[EditAddressViewModel::class.java]
        full_name_edt.setText(full_name)
        phone_edt.setText(Phone_number)
        addressone_edt.setText(Address_one)
        addresstwo_edt.setText(Address_two)
        pincode_edt.setText(Pincode)
        submit_ll.setOnClickListener {
            var fullname= full_name_edt.text.toString()
            var addressone= addressone_edt.text.toString()
            var addresstwo= addresstwo_edt.text.toString()
            var pincode= pincode_edt.text.toString()
            var phone_number= phone_edt.text.toString()

            submitAddress(userid,fullname,addressone,addresstwo,pincode,phone_number)
        }
        back_ll.setOnClickListener {
            finish()
        }
    }
    private fun submitAddress(userid: String, fullname: String, addressone: String, addresstwo: String, pincode: String, phoneNumber: String) {
        editAddressModel.updateAddressResponse(userid ,fullname,addressone,addresstwo,pincode,phoneNumber,Address_id)
        editAddressModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                finish()
                                Toast.makeText(this,"Address updated Successfully", Toast.LENGTH_LONG).show()
                            } else {

                                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT)
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