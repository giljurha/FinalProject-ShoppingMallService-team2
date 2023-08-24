package com.test.campingusproject_seller.ui.notification

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
import com.test.campingusproject_seller.databinding.FragmentNotificationPaymentBinding
import com.test.campingusproject_seller.ui.main.MainActivity

class NotificationPaymentFragment : Fragment() {
    lateinit var fragmentNotificationPaymentBinding: FragmentNotificationPaymentBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentNotificationPaymentBinding = FragmentNotificationPaymentBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        fragmentNotificationPaymentBinding.run {
            recyclerViewNotificationPayment.run {
                adapter = PaymentAdapter()
                layoutManager= LinearLayoutManager(mainActivity)
            }
        }

        return fragmentNotificationPaymentBinding.root
    }

    // 결제목록 아이템
    inner class PaymentAdapter :
        RecyclerView.Adapter<NotificationPaymentFragment.PaymentAdapter.PaymentViewHolder>() {
        inner class PaymentViewHolder(rowNotificationPaymentBinding: RowNotificationPaymentBinding) :RecyclerView.ViewHolder(rowNotificationPaymentBinding.root) {
            val imageViewRowNotificationPayment: ImageView
            val textViewRowNotificationPaymentContent:TextView
            val buttonRowNotificationPaymentToSellStateDetail: Button
            init {
                imageViewRowNotificationPayment = rowNotificationPaymentBinding.imageViewRowNotificationPayment
                textViewRowNotificationPaymentContent = rowNotificationPaymentBinding.textViewRowNotificationPaymentContent
                buttonRowNotificationPaymentToSellStateDetail = rowNotificationPaymentBinding.buttonRowNotificationPaymentToSellStateDetail
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
            val rowNotificationPaymentBinding = RowNotificationPaymentBinding.inflate(layoutInflater)
            val paymentViewHolder = PaymentViewHolder(rowNotificationPaymentBinding)

            rowNotificationPaymentBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return paymentViewHolder
        }

        override fun getItemCount(): Int {
            return 3
        }

        override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
            holder.textViewRowNotificationPaymentContent.text = "장작 상품이 5개 주문되었습니다."
            holder.buttonRowNotificationPaymentToSellStateDetail.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.SELL_STATE_DETAIL_FRAGMENT, true, true, null)
            }
        }
    }
}