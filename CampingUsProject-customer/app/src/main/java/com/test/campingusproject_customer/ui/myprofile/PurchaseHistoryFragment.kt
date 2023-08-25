package com.test.campingusproject_customer.ui.myprofile

import android.opengl.Visibility
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
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentPurchaseHistoryBinding
import com.test.campingusproject_customer.databinding.RowPurchaseHistoryBinding
import com.test.campingusproject_customer.databinding.RowPurchaseHistoryItemBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class PurchaseHistoryFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentPurchaseHistoryBinding: FragmentPurchaseHistoryBinding

    var state = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mainActivity = activity as MainActivity
        fragmentPurchaseHistoryBinding = FragmentPurchaseHistoryBinding.inflate(layoutInflater)

        fragmentPurchaseHistoryBinding.run {

            // 툴바
            toolbarPayment.run {
                //백버튼 설정
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.CART_FRAGMENT)
                }
            }

            // 리사이클러 뷰
            recyclerViewPurchaseHistory.run {
                adapter = PurchaseHistoryAdapter()
                layoutManager= LinearLayoutManager(mainActivity)
            }
        }

        return fragmentPurchaseHistoryBinding.root
    }

    // 구매내역 어댑터
    inner class PurchaseHistoryAdapter :
        RecyclerView.Adapter<PurchaseHistoryFragment.PurchaseHistoryAdapter.PurchaseHistoryViewHolder>() {

        inner class PurchaseHistoryViewHolder(rowPurchaseHistoryBinding: RowPurchaseHistoryBinding) :
            RecyclerView.ViewHolder(rowPurchaseHistoryBinding.root) {
            val textViewRowPurchaseHistoryDate: TextView
            val buttonRowPurchaseHistory: Button

            init {
                textViewRowPurchaseHistoryDate = rowPurchaseHistoryBinding.textViewRowPurchaseHistoryDate
                buttonRowPurchaseHistory = rowPurchaseHistoryBinding.buttonRowPurchaseHistory



                // 안쪽 주문내역 리사이클러뷰 호출
                rowPurchaseHistoryBinding.recyclerViewRowPurchaseHistory.adapter = PurchaseHistoryItemAdapter()
                rowPurchaseHistoryBinding.recyclerViewRowPurchaseHistory.layoutManager = LinearLayoutManager(mainActivity)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseHistoryViewHolder {
            val rowPurchaseHistoryBinding = RowPurchaseHistoryBinding.inflate(layoutInflater)
            val PurchaseHistoryViewHolder = PurchaseHistoryViewHolder(rowPurchaseHistoryBinding)

            rowPurchaseHistoryBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return PurchaseHistoryViewHolder
        }

        override fun getItemCount(): Int {
            return 5
        }

        override fun onBindViewHolder(holder: PurchaseHistoryViewHolder, position: Int) {
            holder.textViewRowPurchaseHistoryDate.text = "2023-08-10"
            holder.buttonRowPurchaseHistory.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.ORDER_DETAIL_FRAGMENT, true, true, null)
            }
        }
    }

    // 구매내역 아이템 어댑터
    inner class PurchaseHistoryItemAdapter :
        RecyclerView.Adapter<PurchaseHistoryFragment.PurchaseHistoryItemAdapter.PurchaseHistoryItemViewHolder>() {

        inner class PurchaseHistoryItemViewHolder(rowPurchaseHistoryItemBinding: RowPurchaseHistoryItemBinding) :
            RecyclerView.ViewHolder(rowPurchaseHistoryItemBinding.root) {
            val imageViewRowPurchaseHistoryItemProduct: ImageView
            val textViewRowPurchaseHistoryItemName: TextView
            val textViewRowPurchaseHistoryItemPrice: TextView
            val textViewRowPurchaseHistoryItemNumber: TextView
            val textViewRowPurchaseHistoryItemState: TextView
            val textViewRowPurchaseHistoryItemStateDone: TextView
            val textViewRowPurchaseHistoryItemReview: TextView

            init {
                imageViewRowPurchaseHistoryItemProduct = rowPurchaseHistoryItemBinding.imageViewRowPurchaseHistoryItemProduct
                textViewRowPurchaseHistoryItemName = rowPurchaseHistoryItemBinding.textViewRowPurchaseHistoryItemName
                textViewRowPurchaseHistoryItemPrice = rowPurchaseHistoryItemBinding.textViewRowPurchaseHistoryItemPrice
                textViewRowPurchaseHistoryItemNumber = rowPurchaseHistoryItemBinding.textViewRowPurchaseHistoryItemNumber
                textViewRowPurchaseHistoryItemState = rowPurchaseHistoryItemBinding.textViewRowPurchaseHistoryItemState
                textViewRowPurchaseHistoryItemStateDone = rowPurchaseHistoryItemBinding.textViewRowPurchaseHistoryItemStateDone
                textViewRowPurchaseHistoryItemReview = rowPurchaseHistoryItemBinding.textViewRowPurchaseHistoryItemReview

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseHistoryItemViewHolder {
            val rowPurchaseHistoryItemBinding = RowPurchaseHistoryItemBinding.inflate(layoutInflater)
            val purchaseHistoryItemViewHolder =  PurchaseHistoryItemViewHolder(rowPurchaseHistoryItemBinding)

            rowPurchaseHistoryItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return purchaseHistoryItemViewHolder
        }

        override fun getItemCount(): Int {
            return 5
        }

        override fun onBindViewHolder(holder: PurchaseHistoryItemViewHolder, position: Int) {
            holder.textViewRowPurchaseHistoryItemName.text = "장작"
            holder.textViewRowPurchaseHistoryItemPrice.text = "15000원"
            holder.textViewRowPurchaseHistoryItemNumber.text = "3개"
            if (state) {
                holder.textViewRowPurchaseHistoryItemState.visibility = View.GONE
                holder.textViewRowPurchaseHistoryItemStateDone.visibility = View.VISIBLE
                holder.textViewRowPurchaseHistoryItemReview.visibility = View.VISIBLE
            } else {
                holder.textViewRowPurchaseHistoryItemState.visibility = View.VISIBLE
                holder.textViewRowPurchaseHistoryItemStateDone.visibility = View.GONE
                holder.textViewRowPurchaseHistoryItemReview.visibility = View.GONE
            }

            holder.textViewRowPurchaseHistoryItemReview.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.REVIEW_WRITE_FRAGMENT, true, true, null)
            }
        }
    }

}
