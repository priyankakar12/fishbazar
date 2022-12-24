package com.pk.fishmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import com.pk.fishmarket.dashboard.MainActivity

class SuccessPageActivity : AppCompatActivity() {
    lateinit var submit_ll:RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_page)
        submit_ll= findViewById(R.id.submit_ll)
        submit_ll.setOnClickListener {
            var i= Intent(this,MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}