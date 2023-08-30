package com.test.campingusproject_customer.ui.payment

import android.content.Context
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentPaymentBinding
import com.test.campingusproject_customer.databinding.RowPaymentBinding
import com.test.campingusproject_customer.dataclassmodel.BundleData
import com.test.campingusproject_customer.dataclassmodel.CartModel
import com.test.campingusproject_customer.dataclassmodel.CartProductModel
import com.test.campingusproject_customer.dataclassmodel.OrderModel
import com.test.campingusproject_customer.dataclassmodel.OrderProductModel
import com.test.campingusproject_customer.repository.CartRepository
import com.test.campingusproject_customer.repository.OrderRepository
import com.test.campingusproject_customer.ui.main.MainActivity
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date

class PaymentFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentPaymentBinding: FragmentPaymentBinding

    val orderproductList = mutableListOf<OrderProductModel>()

    //현재 시간 받아와 주문 번호와 주문 날짜 값 저장
    val time = System.currentTimeMillis()
    val orderId = time.toString()

    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val orderDate = sdf.format(time)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = activity as MainActivity
        fragmentPaymentBinding = FragmentPaymentBinding.inflate(layoutInflater)

        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        //현재 로그인된 유저의 정보가 담긴 sharedPreference 객체
        val sharedPreference = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)

        //장바구니에서 bundle로 전달한 선택 상품 목록
        val spinnerList = arguments?.getIntArray("spinnerList")
        val productList = arguments?.getParcelableArrayList<CartProductModel>("productList")

        if (productList != null) {
            for (idx in 0 until spinnerList?.size!!){
                val productCount = spinnerList[idx]
                val productName = productList[idx].productName
                val productPrice = productList[idx].productPrice
                val productImage = productList[idx].productImage

                Log.d("productList", "$productCount")
                Log.d("productList", "$productName")
                Log.d("productList", "$productPrice")
                Log.d("productList", "$productImage")

                val orderProduct = OrderProductModel(orderId, productName!!, productCount.toString(),
                    productPrice.toString(), productImage.toString())

                orderproductList.add(orderProduct)
            }
        }

        fragmentPaymentBinding.run {
            //주문 번호 설정
            textViewPaymentOrderId.setText(orderId)

            //주문 날짜 설정
            textViewPaymentDate.setText(orderDate)

            //주문자 설정
            textInputEditTextPaymentName.setText("${sharedPreference.getString("customerUserName", null)}")
            textInputEditTextPaymentCustomerPhone.setText("${sharedPreference.getString("customerUserPhoneNumber", null)}")

            //배송지 설정
            textInputEditTextPaymentRecipent.setText("${sharedPreference.getString("customerUserShipRecipient", null)}")
            textInputEditTextPaymentContact.setText("${sharedPreference.getString("customerUserShipContact", null)}")
            textInputEditTextPaymentAddress.setText("${sharedPreference.getString("customerUserShipAddress", null)}")

            //라디오 그룹 기본값 설정
            radioGroupPayment.check(R.id.radio_button_card)

            //결제 금액 설정
            val totalPrice = calTotalPrice()
            textViewPaymentTotalCost.setText("$totalPrice 원")
            buttonPaymentBuy.setText("$totalPrice 원 결제하기")

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
            buttonPaymentBuy.setOnClickListener {
                //입력 요소 검사
                val nameExist = textInputLayoutisEmptyCheck(textInputLayoutPaymentName, textInputEditTextPaymentName, "주문자 이름을 입력해주세요")
                val phoneNumberExist = textInputLayoutisEmptyCheck(textInputLayoutPaymentCustomerPhone, textInputEditTextPaymentCustomerPhone, "주문자 전화번호를 입력해주세요")
                val recipientExist = textInputLayoutisEmptyCheck(textInputLayoutPaymentRecipent, textInputEditTextPaymentRecipent, "받는 사람을 입력해주세요")
                val contactExist = textInputLayoutisEmptyCheck(textInputLayoutPaymentContact, textInputEditTextPaymentContact, "배송지 연락처를 입력해주세요")
                val addressExist = textInputLayoutisEmptyCheck(textInputLayoutPaymentAddress, textInputEditTextPaymentAddress, "배송지 주소를 입력해주세요")

                if(nameExist || phoneNumberExist || recipientExist || contactExist || addressExist){
                    return@setOnClickListener
                }

                val name = textInputEditTextPaymentName.text.toString()
                val phoneNumber = textInputEditTextPaymentCustomerPhone.text.toString()
                val recipient = textInputEditTextPaymentRecipent.text.toString()
                val contact = textInputEditTextPaymentContact.text.toString()
                val address = textInputEditTextPaymentAddress.text.toString()

                val payment = when(radioGroupPayment.checkedRadioButtonId){
                    R.id.radio_button_card -> radioButtonCard.text.toString()
                    R.id.radio_button_payco -> radioButtonPayco.text.toString()
                    R.id.radio_button_toss -> radioButtonToss.text.toString()
                    R.id.radio_button_no_passbook -> radioButtonNoPassbook.text.toString()
                    else -> ""
                }

                //OrderModel 저장
                val orderModel = OrderModel(sharedPreference.getString("customerUserId", null)!!,
                    orderId, orderDate, payment, "결제 완료", recipient, contact, address, name, phoneNumber)

                OrderRepository.addOrderInfo(orderModel){
                    Log.d("FirebaseSave", "ordermodel저장")
                }

                for(item in orderproductList){
                    OrderRepository.addOrderProductInfo(item){
                        Log.d("FirebaseSave", "orderProductModel 저장")
                    }
                }

                CartRepository.removeAllCartData(sharedPreference.getString("customerUserId", null)!!)

                //주문번호 다음 페이지로 넘김
                val newBundle = Bundle()
                newBundle.putString("orderId", orderId)

                mainActivity.removeFragment(MainActivity.PAYMENT_FRAGMENT)
                mainActivity.removeFragment(MainActivity.CART_FRAGMENT)
                mainActivity.replaceFragment(MainActivity.ORDER_DETAIL_FRAGMENT, true, true, newBundle)

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
            return orderproductList.size
        }

        override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
            holder.textViewRowPaymentTitle.text = orderproductList[position].orderProductName
            holder.textViewRowPaymentContent.text = "가격 : ${orderproductList[position].orderProductPrice}"
            holder.textViewRowPaymentCount.text = "수량 : ${orderproductList[position].orderProductCount}"

            CartRepository.getProductFirstImage(orderproductList[position].orderProductImage){
                Glide.with(mainActivity).load(it.result)
                    .into(holder.imageViewRowPayment)
            }

        }
    }

    //textInputLayout 오류 표시 함수
    fun textInputLayoutEmptyError(textInputLayout: TextInputLayout, errorMessage : String){
        textInputLayout.run {
            error = errorMessage
            requestFocus()
        }
    }

    //textInputLayout 입력 검사 함수
    fun textInputLayoutisEmptyCheck(
        textInputLayout: TextInputLayout,
        textInputEditText: TextInputEditText,
        errorMessage: String) : Boolean
    {
        if(textInputEditText.text.toString().isEmpty()){
            //입력되지 않았으면 오류 표시
            textInputLayoutEmptyError(textInputLayout, errorMessage)
            mainActivity.focusOnView(textInputEditText)
            return true
        }
        else{
            textInputLayout.error = null
            return false
        }
    }

    fun calTotalPrice() : Int{
        var totalPrice = 0
        for(item in orderproductList){
            totalPrice += (item.orderProductPrice.toInt() * item.orderProductCount.toInt())
        }
        return totalPrice
    }
}