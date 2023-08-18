package com.test.campingusproject_seller.ui.sellstate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentSellstateBinding

class SellstateFragment : Fragment() {
    lateinit var fragmentSellstateBinding: FragmentSellstateBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSellstateBinding = FragmentSellstateBinding.inflate(inflater)

        fragmentSellstateBinding.run {
            toolbarSellstate.run {
                title = "판매현황"

                inflateMenu(R.menu.menu_sellstate)

//                setOnMenuItemClickListener {
//                    when(it.itemId) {
//                        R.id.itemNotification -> { // 알림화면으로 넘어가기
//
//                        }
//                    }
//                }
            }
        }
        return fragmentSellstateBinding.root
    }
}