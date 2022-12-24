package com.pk.fishmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.pk.fishmarket.Utils.InternetConnection
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.showToast
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.LoginViewModel
import com.pk.fishmarket.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity(),View.OnClickListener {
    lateinit var submit_ll : RelativeLayout
    lateinit var main_ll : RelativeLayout
    lateinit var phone_number_edt : EditText
    lateinit var progress_circular : ProgressBar
    lateinit var send_otp_txt : TextView
    lateinit var sign_in : TextView
    private lateinit var loginViewModel: LoginViewModel
    var cancel = false
    var focusView: View? = null
    var mSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        submit_ll =findViewById(R.id.submit_ll);
        main_ll =findViewById(R.id.main_ll);
        phone_number_edt =findViewById(R.id.phone_number_edt);
        progress_circular =findViewById(R.id.progress_circular);
        send_otp_txt =findViewById(R.id.send_otp_txt);
        sign_in =findViewById(R.id.sign_in);
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        submit_ll.setOnClickListener(this)
        sign_in.setOnClickListener {
            startActivity(Intent(this@LoginActivity,RegistrationActivity::class.java))
        }
    }

    override fun onClick(v: View?) {
       when(v?.id){
           R.id.submit_ll->{
               var phonenumber = phone_number_edt.text.toString()


               when {
                   TextUtils.isEmpty(phonenumber) -> {
                       showToast("Please insert phonenumber")
                       focusView = phone_number_edt
                       cancel = true
                   }

                   else -> {

                       if (InternetConnection.isConnected(this)) {

                           submitData(phonenumber)

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

           }

       }
    }

    private fun submitData(phonenumber: String) {
        loginViewModel.getLoginResponse(phonenumber)
        loginViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {
                        send_otp_txt.visibility = View.GONE
                        progress_circular.visibility = View.VISIBLE
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                Toast.makeText(this, response.body()?.message+ " "+response.body()?.otp, Toast.LENGTH_LONG)
                                    .show()

                                var intent = Intent(this@LoginActivity,OtpVerifyActivity::class.java)
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