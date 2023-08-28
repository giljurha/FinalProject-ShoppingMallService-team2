package com.test.campingusproject_customer.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.campingusproject_customer.databinding.FragmentOrderDetailBinding
import com.test.campingusproject_customer.databinding.RowPaymentBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class OrderDetailFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentOrderDetailBinding: FragmentOrderDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mainActivity = activity as MainActivity
        fragmentOrderDetailBinding = FragmentOrderDetailBinding.inflate(layoutInflater)

        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        fragmentOrderDetailBinding.run {
            // 툴바
            toolbarOrderDetail.run {
                //백버튼 설정
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.ORDER_DETAIL_FRAGMENT)
                    mainActivity.removeFragment(MainActivity.PAYMENT_FRAGMENT)
                    mainActivity.removeFragment(MainActivity.CART_FRAGMENT)
                }
            }

            // 리사이클러 뷰
            recyclerViewOrderDetail.run {
                adapter = OrderDetailAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
            }
        }

        return fragmentOrderDetailBinding.root
    }

    // OrderDetail 어댑터
    inner class OrderDetailAdapter : RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>(){

        inner class OrderDetailViewHolder(rowPaymentBinding: RowPaymentBinding) : RecyclerView.ViewHolder(rowPaymentBinding.root) {
            val textViewRowPaymentTitle : TextView
            val imageViewRowPayment : ImageView
            val textViewRowPaymentContent : TextView
            val textViewRowPaymentCount : TextView

            init {
                textViewRowPaymentTitle = rowPaymentBinding.textViewRowPaymentTitle
                imageViewRowPayment = rowPaymentBinding.imageViewRowPayment
                textViewRowPaymentContent = rowPaymentBinding.textViewRowPaymentContent
                textViewRowPaymentCount = rowPaymentBinding.textViewRowPaymentCount
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
            val rowPaymentBinding = RowPaymentBinding.inflate(layoutInflater)

            rowPaymentBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return OrderDetailViewHolder(rowPaymentBinding)
        }

        override fun getItemCount(): Int {
            return 3
        }

        override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
            holder.textViewRowPaymentTitle.text = "title $position"

        }
    }

}