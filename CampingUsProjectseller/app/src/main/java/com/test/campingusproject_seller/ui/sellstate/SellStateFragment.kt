package com.test.campingusproject_seller.ui.sellstate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentSellStateBinding
import com.test.campingusproject_seller.databinding.RowSellStateOrderHistoryBinding
import com.test.campingusproject_seller.databinding.RowSellStateOrderHistoryItemBinding
import com.test.campingusproject_seller.ui.main.MainActivity

class SellStateFragment : Fragment() {
    lateinit var fragmentSellStateBinding: FragmentSellStateBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSellStateBinding = FragmentSellStateBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentSellStateBinding.run {
            toolbarSellState.run {
                title = "판매현황"

                inflateMenu(R.menu.menu_sellstate)

                setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.itemNotification -> { // 알림화면으로 넘어가기
                            mainActivity.replaceFragment(MainActivity.SELL_STATE_FRAGMENT, true, true, null)
                        }
                    }
                    false
                }
            }

            recyclerViewSellState.run {
                adapter = SellStateAdapter()
                layoutManager= LinearLayoutManager(mainActivity)
            }
        }
        return fragmentSellStateBinding.root
    }

    // 판매현황 아이템
    inner class SellStateAdapter :
        RecyclerView.Adapter<SellStateFragment.SellStateAdapter.SellStateViewHolder>() {
        inner class SellStateViewHolder(rowSellStateOrderHistoryBinding: RowSellStateOrderHistoryBinding) :
            RecyclerView.ViewHolder(rowSellStateOrderHistoryBinding.root) {
            val textViewSellStateCustomerName: TextView
            val textViewSellStateDate: TextView
            val buttonSellStateToSellStateDetail: Button
            val textViewSellStateOrderHistory: TextView

            init {
                textViewSellStateCustomerName = rowSellStateOrderHistoryBinding.textViewSellStateCustomerName
                textViewSellStateDate = rowSellStateOrderHistoryBinding.textViewSellStateDate
                buttonSellStateToSellStateDetail = rowSellStateOrderHistoryBinding.buttonSellStateToSellStateDetail
                textViewSellStateOrderHistory = rowSellStateOrderHistoryBinding.textViewSellStateOrderHistory
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellStateViewHolder {
            val rowSellStateOrderHistoryBinding = RowSellStateOrderHistoryBinding.inflate(layoutInflater)
            val sellStateViewHolder = SellStateViewHolder(rowSellStateOrderHistoryBinding)

            rowSellStateOrderHistoryBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return sellStateViewHolder
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: SellStateViewHolder, position: Int) {
            TODO("Not yet implemented")
        }
    }

    // 판매현황 아이템
    inner class OrderHistoryAdapter :
        RecyclerView.Adapter<SellStateFragment.OrderHistoryAdapter.OrderHistoryViewHolder>() {
        inner class OrderHistoryViewHolder(rowSellStateOrderHistoryItemBinding: RowSellStateOrderHistoryItemBinding) :
            RecyclerView.ViewHolder(rowSellStateOrderHistoryItemBinding.root) {
            val imageViewSellStateProduct: ImageView
            val textViewSellStateProductName: TextView
            val textViewSellStateProductPrice: TextView
            val textViewSellStateProductNumber: TextView
            val buttonSellStateSend: Button
            init {

                imageViewSellStateProduct = rowSellStateOrderHistoryItemBinding.imageViewSellStateProduct
                textViewSellStateProductName = rowSellStateOrderHistoryItemBinding.textViewSellStateProductName
                textViewSellStateProductPrice = rowSellStateOrderHistoryItemBinding.textViewSellStateProductPrice
                textViewSellStateProductNumber = rowSellStateOrderHistoryItemBinding.textViewSellStateProductNumber
                buttonSellStateSend = rowSellStateOrderHistoryItemBinding.buttonSellStateSend
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
            val rowSellStateOrderHistoryItemBinding = RowSellStateOrderHistoryItemBinding.inflate(layoutInflater)
            val orderHistoryViewHolder =  OrderHistoryViewHolder(rowSellStateOrderHistoryItemBinding)

            rowSellStateOrderHistoryItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return orderHistoryViewHolder
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

    }
}