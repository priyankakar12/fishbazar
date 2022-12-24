package com.pk.fishmarket.dashboard.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.pk.fishmarket.Adapter.OrderHistoryAdapter
import com.pk.fishmarket.LoginActivity
import com.pk.fishmarket.R
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.OrderDetailsViewModel
import com.pk.fishmarket.viewmodel.OrderHistoryViewModel
import com.pk.fishmarket.viewmodel.UserDetailsViewModel
import com.pk.fishmarket.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : Fragment() {


    lateinit var logout_ll:RelativeLayout
    lateinit var userDetailsViewModel: UserDetailsViewModel
    var userid =""
    lateinit var phone_number:TextView
    lateinit var location: EditText
    lateinit var email:EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       var view = inflater.inflate(R.layout.fragment_profile, container, false)
        logout_ll=view.findViewById(R.id.logout_ll)
        phone_number=view.findViewById(R.id.phone_number)
        location=view.findViewById(R.id.location)
        email=view.findViewById(R.id.email)
        userid= SharedPreferencesUtil().getUserId(requireContext()).toString();
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        userDetailsViewModel = ViewModelProvider(this, factory)[UserDetailsViewModel::class.java]
        logout_ll.setOnClickListener {
            var intent= Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            SharedPreferencesUtil().setUserId("",requireContext())

            requireActivity().finish();

        }

        getProfileDetails(userid)
        return view
    }

    private fun getProfileDetails(userid: String) {
        userDetailsViewModel.getUserDetailsResponse(userid)
        userDetailsViewModel.response.observe(requireActivity()) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        response.data?.let { response ->
                            phone_number.text = response.body()!!.PROFILE_DETAILS[0].USER_PHONE
                            email.setText(response.body()!!.PROFILE_DETAILS[0].USER_EMAIL.toString())
                            location.setText(response.body()!!.PROFILE_DETAILS[0].USER_NAME.toString())
                        }




                    }
                    is Resource.Error -> {

                        response.message?.let { message ->

                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {

                    }
                }
            }
        }



    }


}