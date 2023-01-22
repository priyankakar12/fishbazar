package com.pk.fishmarket

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.pk.fishmarket.Utils.InternetConnection
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.dashboard.MainActivity
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.LoginViewModel
import com.pk.fishmarket.viewmodel.ViewModelFactory


class LoginActivity : AppCompatActivity(),View.OnClickListener {
    lateinit var submit_ll : RelativeLayout
    lateinit var main_ll : RelativeLayout
    lateinit var phone_number_edt : EditText
    lateinit var password_edt : EditText
    lateinit var progress_circular : ProgressBar
    lateinit var send_otp_txt : TextView
    lateinit var hideShowPwd : TextView
    lateinit var sign_in : TextView
    private lateinit var loginViewModel: LoginViewModel
    var cancel = false
    var focusView: View? = null
    var mSnackbar: Snackbar? = null
    var hidePwd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        submit_ll =findViewById(R.id.submit_ll);
        main_ll =findViewById(R.id.main_ll);
        password_edt =findViewById(R.id.password_edt);
        phone_number_edt =findViewById(R.id.phone_number_edt);
        progress_circular =findViewById(R.id.progress_circular);
        send_otp_txt =findViewById(R.id.send_otp_txt);
        sign_in =findViewById(R.id.sign_in);
        hideShowPwd =findViewById(R.id.hideShowPwd);
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        submit_ll.setOnClickListener(this)
        hideShowPwd.setOnClickListener(this)
        sign_in.setOnClickListener {
            startActivity(Intent(this@LoginActivity,RegistrationActivity::class.java))
        }
    }

    override fun onClick(v: View?) {
       when(v?.id){
           R.id.submit_ll->{
               var phonenumber = phone_number_edt.text.toString()
               var password = password_edt.text.toString()



                  if(phonenumber == "")
                  {
                      Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                  }
               else if(password == "")
                  {
                      Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                  }
                      else
                  {
                      if (InternetConnection.isConnected(this)) {

                          submitData(phonenumber,password)

                      } else {
                          mSnackbar = Snackbar
                              .make(main_ll, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                              .setAction(
                                  "Ok"
                              ) { mSnackbar!!.dismiss() }
                          mSnackbar!!.show()
                      }
                  }






           }
           R.id.hideShowPwd->{

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

       }
    }

    private fun submitData(phonenumber: String,password:String) {
        loginViewModel.getLoginResponse(phonenumber,password)
        loginViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {
                        send_otp_txt.visibility = View.GONE
                        progress_circular.visibility = View.VISIBLE
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {


                                Toast.makeText(this, response.body()?.message+ " ", Toast.LENGTH_SHORT)
                                    .show()
                                response.body()?.userid?.let { SharedPreferencesUtil().setUserId(it,this) }
                                response.body()?.phone?.let { SharedPreferencesUtil().savePhone(it,this) }
                                response.body()?.email?.let { SharedPreferencesUtil().saveEmail(it,this) }

                                var intent= Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()


                            } else {

                                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            send_otp_txt.visibility = View.VISIBLE
                            progress_circular.visibility = View.GONE
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {
                        send_otp_txt.visibility = View.GONE
                        progress_circular.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}