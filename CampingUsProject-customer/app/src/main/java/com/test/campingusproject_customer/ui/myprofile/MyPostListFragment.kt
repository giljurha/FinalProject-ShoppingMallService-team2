package com.test.campingusproject_customer.ui.myprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.ListFragment
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentMyPostListBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class MyPostListFragment : Fragment() {
    lateinit var fragmentMyPostListBinding: FragmentMyPostListBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentMyPostListBinding = FragmentMyPostListBinding.inflate(layoutInflater)



        return fragmentMyPostListBinding.root
    }
}