package com.pk.fishmarket.dashboard.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.pk.fishmarket.LoginActivity
import com.pk.fishmarket.R
import com.pk.fishmarket.Utils.SharedPreferencesUtil


class ProfileFragment : Fragment() {


    lateinit var logout_ll:RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       var view = inflater.inflate(R.layout.fragment_profile, container, false)
        logout_ll=view.findViewById(R.id.logout_ll)
        logout_ll.setOnClickListener {
            var intent= Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            SharedPreferencesUtil().setUserId("",requireContext())

            requireActivity().finish();

        }
        return view
    }


}