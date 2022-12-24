package com.pk.fishmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.dashboard.MainActivity
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.AddRatingViewModel
import com.pk.fishmarket.viewmodel.ViewModelFactory

class AddRatingActivity : AppCompatActivity() {
    lateinit var rating_one : ImageView
    lateinit var rating_two : ImageView
    lateinit var rating_three : ImageView
    lateinit var rating_four : ImageView
    lateinit var rating_five : ImageView
    lateinit var submit_ll : RelativeLayout
    lateinit var feedback_edt : EditText
    lateinit var send_otp_txt : TextView
    lateinit var progress_circular : ProgressBar
    var rating =""
    var userid =""
    var shopid =""
    lateinit var addreviewViewModel: AddRatingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rating)
        rating_one = findViewById(R.id.rating_one)
        rating_two = findViewById(R.id.rating_two)
        rating_three = findViewById(R.id.rating_three)
        rating_four = findViewById(R.id.rating_four)
        rating_five = findViewById(R.id.rating_five)
        submit_ll = findViewById(R.id.submit_ll)
        feedback_edt = findViewById(R.id.feedback_edt)
        send_otp_txt = findViewById(R.id.send_otp_txt)
        progress_circular = findViewById(R.id.progress_circular)
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        userid= SharedPreferencesUtil().getUserId(this).toString();
        shopid= intent.extras!!.getString("shopid").toString()
        addreviewViewModel = ViewModelProvider(this, factory)[AddRatingViewModel::class.java]
        rating_one.setOnClickListener{
            rating_one.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_two.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_unselected));
            rating_three.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_unselected));
            rating_four.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_unselected));
            rating_five.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_unselected));
            rating ="1"
        }
          rating_two.setOnClickListener{
            rating_one.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_two.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_three.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_unselected));
            rating_four.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_unselected));
            rating_five.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_unselected));
              rating ="2"
        }
        rating_three.setOnClickListener{
            rating_one.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_two.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_three.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_four.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_unselected));
            rating_five.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_unselected));
            rating ="3"
        }
        rating_four.setOnClickListener{
            rating_one.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_two.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_three.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_four.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_five.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_unselected));
            rating ="4"
        }
        rating_five.setOnClickListener{
            rating_one.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_two.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_three.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_four.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating_five.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rating_selected));
            rating ="5"
        }

        submit_ll.setOnClickListener {
          var feedback = feedback_edt.text.toString()
            submitRating(rating,feedback,userid,shopid)
        }

    }

    private fun submitRating(rating: String, feedback: String, userid: String, shopid: String) {
        addreviewViewModel.getRatingData(rating,feedback,userid,shopid)
        addreviewViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {
                    is Resource.Success -> {
                        send_otp_txt.visibility = View.GONE
                        progress_circular.visibility = View.VISIBLE
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                Toast.makeText(this, response.body()?.message, Toast.LENGTH_LONG)
                                    .show()
                                var intent = Intent(this@AddRatingActivity,MainActivity::class.java)
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