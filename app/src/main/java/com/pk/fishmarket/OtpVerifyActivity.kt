package com.pk.fishmarket

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pk.fishmarket.Utils.InternetConnection
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.Utils.showToast
import com.pk.fishmarket.dashboard.MainActivity
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.OtpVerifyViewModel
import com.pk.fishmarket.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar


class OtpVerifyActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var submit_ll :RelativeLayout
    lateinit var main_ll :RelativeLayout
    lateinit var editTxt1:EditText
    lateinit var editTxt2:EditText
    lateinit var editTxt3:EditText
    lateinit var editTxt4:EditText
    lateinit var submit_txt:TextView
    lateinit var please_sign_in:TextView
    lateinit var progress_circular:ProgressBar
    var phonenumber =""
    var otp =""
    var mSnackbar: Snackbar? = null
    private lateinit var otpVerifyViewModel: OtpVerifyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verify)
        submit_ll = findViewById(R.id.submit_ll)
        editTxt1 = findViewById(R.id.editTxt1)
        editTxt2 = findViewById(R.id.editTxt2)
        editTxt3 = findViewById(R.id.editTxt3)
        editTxt4 = findViewById(R.id.editTxt4)
        main_ll = findViewById(R.id.main_ll)
        progress_circular = findViewById(R.id.progress_circular)
        submit_txt = findViewById(R.id.submit_txt)
        please_sign_in = findViewById(R.id.please_sign_in)

        phonenumber=intent.extras!!.getString("phonenumber").toString()
        otp=intent.extras!!.getString("otp").toString()
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        otpVerifyViewModel = ViewModelProvider(this, factory)[OtpVerifyViewModel::class.java]

        please_sign_in.text = "Please enter " +otp +" as your verification code"
        editTxt1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length != 0){
                    editTxt1.clearFocus();
                    editTxt2.requestFocus();
                    editTxt2.setCursorVisible(true);
                }
            }
        })
        editTxt2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length != 0){
                    editTxt2.clearFocus();
                    editTxt3.requestFocus();
                    editTxt3.setCursorVisible(true);
                }
            }
        })
        editTxt3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length != 0){
                    editTxt3.clearFocus();
                    editTxt4.requestFocus();
                    editTxt4.setCursorVisible(true);
                }
            }
        })

        submit_ll.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.submit_ll->{
                var otp = editTxt1.text.toString()+editTxt2.text.toString()+editTxt3.text.toString()+editTxt4.text.toString()
                if(otp.length < 4)
                {
                    showToast("Please insert correct otp")

                }
                else
                {
                    if (InternetConnection.isConnected(this)) {

                        submitData(phonenumber,otp)

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

    private fun submitData(phonenumber: String, otp: String) {
        otpVerifyViewModel.getOtpresponse(phonenumber,otp)
        otpVerifyViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {
                        submit_txt.visibility = View.GONE
                        progress_circular.visibility = View.VISIBLE
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                Toast.makeText(this, response.body()?.message+ " ", Toast.LENGTH_SHORT)
                                    .show()
                                response.body()?.userid?.let { SharedPreferencesUtil().setUserId(it,this) }
                                response.body()?.phone?.let { SharedPreferencesUtil().savePhone(it,this) }
                                response.body()?.email?.let { SharedPreferencesUtil().saveEmail(it,this) }

                                var intent= Intent(this@OtpVerifyActivity,MainActivity::class.java)
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
                            submit_txt.visibility = View.VISIBLE
                            progress_circular.visibility = View.GONE
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {
                        submit_txt.visibility = View.GONE
                        progress_circular.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
    }
