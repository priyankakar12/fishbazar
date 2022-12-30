package com.pk.fishmarket.dashboard.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pk.fishmarket.LoginActivity
import com.pk.fishmarket.R
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.*
import java.util.*


class ProfileFragment : Fragment() {


    lateinit var logout_ll:RelativeLayout
    lateinit var updateProfile: LinearLayout
    lateinit var userDetailsViewModel: UserDetailsViewModel
    lateinit var profileUserDetailsViewModel: ProfileUpdateViewModel
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
        updateProfile=view.findViewById(R.id.updateProfile)
        userid= SharedPreferencesUtil().getUserId(requireContext()).toString();
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        userDetailsViewModel = ViewModelProvider(this, factory)[UserDetailsViewModel::class.java]
        profileUserDetailsViewModel = ViewModelProvider(this, factory)[ProfileUpdateViewModel::class.java]
        logout_ll.setOnClickListener {
            var intent= Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            SharedPreferencesUtil().setUserId("",requireContext())

            requireActivity().finish();

        }
        updateProfile.setOnClickListener {
            profileUpdate(email.text.toString(),location.text.toString(),userid)


        }
        getProfileDetails(userid)
        return view
    }

    private fun profileUpdate(email_str: String, username: String, userid: String) {
        profileUserDetailsViewModel.profileUpdateData(email_str,username,userid)
        profileUserDetailsViewModel.response.observe(requireActivity()) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        response.data?.let { response ->
                            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            val imm: InputMethodManager? =
                                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                            imm!!.hideSoftInputFromWindow(view!!.windowToken, 0)
                            email.clearFocus();
                            location.clearFocus();
                           getProfileDetails(userid)
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