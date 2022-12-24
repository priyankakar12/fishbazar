package com.pk.fishmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.RegistrationViewModel
import com.pk.fishmarket.viewmodel.ViewModelFactory

class RegistrationActivity : AppCompatActivity() {
    lateinit var gotoLogin:TextView
    lateinit var submit_ll:RelativeLayout
    lateinit var first_name_edt:EditText
    lateinit var last_name_edt:EditText
    lateinit var phone_number_edt:EditText
    lateinit var email_edt:TextView
    private lateinit var registrationViewModel: RegistrationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        gotoLogin = findViewById(R.id.gotoLogin)
        email_edt = findViewById(R.id.email_edt)
        first_name_edt = findViewById(R.id.first_name_edt)
        last_name_edt = findViewById(R.id.last_name_edt)
        phone_number_edt = findViewById(R.id.phone_number_edt)
        submit_ll = findViewById(R.id.submit_ll)
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        registrationViewModel = ViewModelProvider(this, factory)[RegistrationViewModel::class.java]
        gotoLogin.setOnClickListener {
            startActivity(Intent(this@RegistrationActivity,LoginActivity::class.java))
        }
        submit_ll.setOnClickListener {
        var phonenumber = phone_number_edt.text.toString()
        var username = first_name_edt.text.toString()+" "+last_name_edt.text.toString()

        var email = email_edt.text.toString()
        submitData(phonenumber,username,email)
        }
    }
    private fun submitData(phonenumber: String, username:String, email: String) {
        registrationViewModel.getLoginResponse(phonenumber,username,email)
        registrationViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                Toast.makeText(this, response.body()?.message+ " "+response.body()?.otp, Toast.LENGTH_LONG)
                                    .show()

                                var intent = Intent(this,OtpVerifyActivity::class.java)
                                intent.putExtra("phonenumber",phonenumber)
                                intent.putExtra("otp",response.body()?.otp.toString())
                                startActivity(intent)


                            } else {

                                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT)
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