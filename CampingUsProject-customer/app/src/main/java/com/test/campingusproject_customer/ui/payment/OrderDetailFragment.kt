package com.test.campingusproject_customer.ui.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.campingusproject_customer.databinding.FragmentOrderDetailBinding
import com.test.campingusproject_customer.databinding.RowPaymentBinding
import com.test.campingusproject_customer.dataclassmodel.OrderProductModel
import com.test.campingusproject_customer.repository.CartRepository
import com.test.campingusproject_customer.ui.main.MainActivity
import com.test.campingusproject_customer.viewmodel.OrderDetailViewModel

class OrderDetailFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentOrderDetailBinding: FragmentOrderDetailBinding
    lateinit var orderDetailViewModel: OrderDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       //번들로 넘어온 값을 받는 부분
        val orderId=arguments?.getString("orderId")


        mainActivity = activity as MainActivity
        fragmentOrderDetailBinding = FragmentOrderDetailBinding.inflate(layoutInflater)
        orderDetailViewModel=ViewModelProvider(mainActivity)[OrderDetailViewModel::class.java]
        //제품 리스트 패치
        orderDetailViewModel.fetchOrderedProductList(orderId!!)
        orderDetailViewModel.run {
            orderDeliveryReceiver.observe(mainActivity){
                //받는사람
                fragmentOrderDetailBinding.textViewReceiverName.text=it
            }
            orderDeliveryReceiverPhone.observe(mainActivity){
                //연락처
                fragmentOrderDetailBinding.textViewReceiverPhoneNum.text=it
            }
            orderUserAddress.observe(mainActivity){
                //주소
                fragmentOrderDetailBinding.textViewReceiverAddress.text=it
            }
            orderUserName.observe(mainActivity){
                //이름
                fragmentOrderDetailBinding.textViewOrderName.text=it
            }
            orderUserPhone.observe(mainActivity){
                //휴대전화
                fragmentOrderDetailBinding.textViewOrderPhoneNum.text=it
            }
            orderMeans.observe(mainActivity){
                //결제수단
                fragmentOrderDetailBinding.textViewOrderWay.text=it
            }
            orderTotalPrice.observe(mainActivity){
                //금액
                fragmentOrderDetailBinding.textViewOrderTotalPrice.text=it
                val adapter=fragmentOrderDetailBinding.recyclerViewOrderDetail.adapter as OrderDetailAdapter
                adapter.notifyDataSetChanged()
            }
            orderStatus.observe(mainActivity){
                //결제 상태?
                fragmentOrderDetailBinding.textViewOrderState.text=it
            }

        }


        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        fragmentOrderDetailBinding.run {

            //결제 내용 패치
            orderDetailViewModel.fetchOrderInfo(orderId!!)

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
                textViewRowPaymentContent = rowPaymentBinding.textViewRowPaymentPrice
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
            return orderDetailViewModel.orderProductList.size
        }

        override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
            holder.textViewRowPaymentTitle.text=orderDetailViewModel.orderProductList[position].orderProductName
            holder.textViewRowPaymentContent.text=orderDetailViewModel.orderProductList[position].orderProductPrice+"원"
            holder.textViewRowPaymentCount.text=orderDetailViewModel.orderProductList[position].orderProductCount+"개"
            CartRepository.getProductFirstImage(orderDetailViewModel.orderProductList[position].orderProductImage){
                Glide.with(mainActivity).load(it.result)
                    .into(holder.imageViewRowPayment)
            }

        }
    }

    override fun onStop() {
        super.onStop()
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE
    }

}