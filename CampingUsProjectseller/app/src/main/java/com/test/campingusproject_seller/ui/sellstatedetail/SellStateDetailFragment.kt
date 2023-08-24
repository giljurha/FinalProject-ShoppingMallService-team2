package com.test.campingusproject_seller.ui.sellstatedetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.campingusproject_seller.databinding.FragmentSellStateDetailBinding
import com.test.campingusproject_seller.databinding.RowSellStateOrderHistoryBinding
import com.test.campingusproject_seller.ui.main.MainActivity
import com.test.campingusproject_seller.ui.sellstate.SellStateFragment

class SellStateDetailFragment : Fragment() {
    lateinit var fragmentSellStateDetailBinding: FragmentSellStateDetailBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSellStateDetailBinding = FragmentSellStateDetailBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentSellStateDetailBinding.run {
            toolbarSellStateDetail.run {
                title = "판매현황 상세보기"

                // 백버튼
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.SELL_STATE_DETAIL_FRAGMENT)
                }
            }
        }
        return fragmentSellStateDetailBinding.root
    }
}