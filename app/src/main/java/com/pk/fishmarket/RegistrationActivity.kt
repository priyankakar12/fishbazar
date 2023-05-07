package com.pk.fishmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
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
import retrofit2.http.Field

class RegistrationActivity : AppCompatActivity() {
    lateinit var gotoLogin:TextView
    lateinit var hideShowPwd:TextView
    lateinit var hideShowPwd1:TextView
    lateinit var submit_ll:RelativeLayout
    lateinit var first_name_edt:EditText
    lateinit var last_name_edt:EditText
    lateinit var phone_number_edt:EditText
    lateinit var password_edt:EditText
    lateinit var conpassword_edt:EditText
    lateinit var email_edt:TextView
    private lateinit var registrationViewModel: RegistrationViewModel
    var hidePwd =false
    var hidePwd1 =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        gotoLogin = findViewById(R.id.gotoLogin)
        hideShowPwd1 = findViewById(R.id.hideShowPwd1)
        hideShowPwd = findViewById(R.id.hideShowPwd)
        email_edt = findViewById(R.id.email_edt)
        first_name_edt = findViewById(R.id.first_name_edt)
        last_name_edt = findViewById(R.id.last_name_edt)
        phone_number_edt = findViewById(R.id.phone_number_edt)
        submit_ll = findViewById(R.id.submit_ll)
        password_edt = findViewById(R.id.password_edt)
        conpassword_edt = findViewById(R.id.conpassword_edt)
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        registrationViewModel = ViewModelProvider(this, factory)[RegistrationViewModel::class.java]
        gotoLogin.setOnClickListener {
            startActivity(Intent(this@RegistrationActivity,LoginActivity::class.java))
        }
        hideShowPwd.setOnClickListener {
            if(!hidePwd)
            {
                password_edt.transformationMethod = null;

                hideShowPwd.text = "Hide"
                hidePwd = true
            }
            else
            {
                password_edt.transformationMethod = PasswordTransformationMethod()
                hideShowPwd.text = "Show"
                hidePwd = false
            }
        }
        hideShowPwd1.setOnClickListener {

            if(!hidePwd1)
            {
                conpassword_edt.transformationMethod = null;

                hideShowPwd1.text = "Hide"
                hidePwd1 = true
            }
            else
            {
                conpassword_edt.transformationMethod = PasswordTransformationMethod()
                hideShowPwd1.text = "Show"
                hidePwd1 = false
            }
        }
        submit_ll.setOnClickListener {
        var phonenumber = phone_number_edt.text.toString()
        var username = first_name_edt.text.toString()+" "+last_name_edt.text.toString()

        var email = email_edt.text.toString()
//        var user_pass = password_edt.text.toString()
//        var user_con_pass = conpassword_edt.text.toString()
            var user_pass = ""
        var user_con_pass = ""

        var firstname = first_name_edt.text.toString()
        var lastname = last_name_edt.text.toString()
            if(phonenumber =="")
            {
                Toast.makeText(this, "phone number cannot be blank", Toast.LENGTH_SHORT).show()
            }
             else if(email =="")
            {
                Toast.makeText(this, "email cannot be blank", Toast.LENGTH_SHORT).show()
            }

            else if(first_name_edt.text.toString() == "")
            {
                Toast.makeText(this, "first name cannot be blank", Toast.LENGTH_SHORT).show()
            }
            else if(last_name_edt.text.toString() == "")
            {
                Toast.makeText(this, "last name cannot be blank", Toast.LENGTH_SHORT).show()
            }
          /*  else if(password_edt.text.toString() == "")
            {
                Toast.makeText(this, "password cannot be blank", Toast.LENGTH_SHORT).show()
            }
            else if(conpassword_edt.text.toString() == "")
            {
                Toast.makeText(this, " confirm password cannot be blank", Toast.LENGTH_SHORT).show()
            }*/

            else
            {
                submitData(phonenumber,username,email,user_pass,user_con_pass,firstname,lastname)
            }


        }
    }
    private fun submitData(phonenumber: String, username:String, email: String,user_pass:String
                           ,user_con_pass:String,firstname:String,lastname:String) {
        registrationViewModel.getLoginResponse(phonenumber,username,email,user_pass,user_con_pass,firstname,lastname)
        registrationViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                Toast.makeText(this, response.body()?.message, Toast.LENGTH_LONG)
                                    .show()

                                var intent = Intent(this,LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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