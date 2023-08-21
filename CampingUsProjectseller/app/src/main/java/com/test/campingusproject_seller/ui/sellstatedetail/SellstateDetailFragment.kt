package com.test.campingusproject_seller.ui.sellstatedetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentSellstateDetailBinding

class SellstateDetailFragment : Fragment() {
    lateinit var fragmentSellstateDetailBinding: FragmentSellstateDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSellstateDetailBinding = FragmentSellstateDetailBinding.inflate(inflater)

        return fragmentSellstateDetailBinding.root
    }
}