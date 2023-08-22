package com.test.campingusproject_seller.ui.sellstatedetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_seller.databinding.FragmentSellStateDetailBinding

class SellStateDetailFragment : Fragment() {
    lateinit var fragmentSellStateDetailBinding: FragmentSellStateDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSellStateDetailBinding = FragmentSellStateDetailBinding.inflate(inflater)

        fragmentSellStateDetailBinding.run {
            toolbarSellStateDetail.run {
                title = "판매현황 상세보기"

            }
        }
        return fragmentSellStateDetailBinding.root
    }
}