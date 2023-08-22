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
                            mainActivity.replaceFragment(MainActivity.NOTIFICATION_MAIN_FRAGMENT, true, true, null)
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

            init {
                textViewSellStateCustomerName = rowSellStateOrderHistoryBinding.textViewSellStateCustomerName
                textViewSellStateDate = rowSellStateOrderHistoryBinding.textViewSellStateDate
                buttonSellStateToSellStateDetail = rowSellStateOrderHistoryBinding.buttonSellStateToSellStateDetail

                // 안쪽 주문내역 리사이클러뷰 호출
                rowSellStateOrderHistoryBinding.recyclerViewSellStateOrder.adapter = OrderHistoryAdapter()
                rowSellStateOrderHistoryBinding.recyclerViewSellStateOrder.layoutManager = LinearLayoutManager(mainActivity)


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
            return 5
        }

        override fun onBindViewHolder(holder: SellStateViewHolder, position: Int) {
            holder.textViewSellStateCustomerName.text = "김민우"
            holder.textViewSellStateDate.text = "2023-08-22"
            holder.buttonSellStateToSellStateDetail.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.SELL_STATE_DETAIL_FRAGMENT, true, true, null)
            }
        }
    }

    // 주문내역 아이템
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
            return 5
        }

        override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
            holder.textViewSellStateProductName.text = "장작"
            holder.textViewSellStateProductPrice.text = "15000원"
            holder.textViewSellStateProductNumber.text = "3개"
        }
    }
}