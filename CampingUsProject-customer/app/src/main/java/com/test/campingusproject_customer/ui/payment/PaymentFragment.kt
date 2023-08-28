package com.test.campingusproject_customer.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentPaymentBinding
import com.test.campingusproject_customer.databinding.RowCartBinding
import com.test.campingusproject_customer.databinding.RowPaymentBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class PaymentFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentPaymentBinding: FragmentPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mainActivity = activity as MainActivity
        fragmentPaymentBinding = FragmentPaymentBinding.inflate(layoutInflater)

        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        fragmentPaymentBinding.run {
            // 툴바
            toolbarPayment.run {
                //백버튼 설정
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.PAYMENT_FRAGMENT)
                }
            }

            // 리사이클러 뷰
            recyclerViewPaymentProduct.run {
                adapter = PaymentAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
            }

            // 결제하기 버튼
            buttonPaymentBuy.run {
                setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.ORDER_DETAIL_FRAGMENT, true, true, null)
                }
            }
        }

        return fragmentPaymentBinding.root
    }

    // Payment 어댑터
    inner class PaymentAdapter : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>(){

        inner class PaymentViewHolder(rowPaymentBinding: RowPaymentBinding) : RecyclerView.ViewHolder(rowPaymentBinding.root) {
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
            val rowPaymentBinding = RowPaymentBinding.inflate(layoutInflater)

            rowPaymentBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return PaymentViewHolder(rowPaymentBinding)
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
            holder.textViewRowPaymentTitle.text = "title $position"

        }
    }

}