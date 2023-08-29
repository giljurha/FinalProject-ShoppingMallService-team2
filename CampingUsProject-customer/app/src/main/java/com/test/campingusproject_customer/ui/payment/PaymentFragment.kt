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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentPaymentBinding
import com.test.campingusproject_customer.databinding.RowPaymentBinding
import com.test.campingusproject_customer.dataclassmodel.BundleData
import com.test.campingusproject_customer.dataclassmodel.CartModel
import com.test.campingusproject_customer.dataclassmodel.CartProductModel
import com.test.campingusproject_customer.dataclassmodel.OrderProductModel
import com.test.campingusproject_customer.repository.OrderRepository
import com.test.campingusproject_customer.ui.main.MainActivity
import kotlinx.coroutines.runBlocking

class PaymentFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentPaymentBinding: FragmentPaymentBinding

    val orderproductList = mutableListOf<OrderProductModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mainActivity = activity as MainActivity
        fragmentPaymentBinding = FragmentPaymentBinding.inflate(layoutInflater)

        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        val orderId = System.currentTimeMillis().toString()

        //현재 로그인된 유저의 정보가 담긴 sharedPreference 객체
        val sharedPreference = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)

        fragmentPaymentBinding.run {
            //주문 번호 설정
            textViewPaymentOrderId.setText(orderId)

            //주문자 설정
            textInputEditTextPaymentName.setText("${sharedPreference.getString("customerUserName", null)}")
            textInputEditTextPaymentCustomerPhone.setText("${sharedPreference.getString("customerUserPhoneNumber", null)}")

            //배송지 설정
            textInputEditTextPaymentRecipent.setText("${sharedPreference.getString("customerUserShipRecipient", null)}")
            textInputEditTextPaymentContact.setText("${sharedPreference.getString("customerUserShipContact", null)}")
            textInputEditTextPaymentAddress.setText("${sharedPreference.getString("customerUserShipAddress", null)}")

            radioGroupPayment.check(R.id.radio_button_card)

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
                getProductList(orderId)

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

                //val totalPrice



                mainActivity.replaceFragment(MainActivity.ORDER_DETAIL_FRAGMENT, true, true, null)

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

    //모든 상품 리스트를 서버로부터 받아오는 과정
    fun getProductList(orderId : String){
        //장바구니에서 bundle로 전달한 선택 상품 목록
        val spinnerList = arguments?.getIntArray("spinnerList")
        val productList = arguments?.getParcelableArrayList<CartProductModel>("productList")



        if (productList != null) {
            for (item in productList){

                Log.d("item", "$item")
//                val productId = item.productId
//                val productCount = item.productCount
//
//                //상품 목록의 모든 값을 받아옴
//                runBlocking {
//                    OrderRepository.getOneProductData(productId){
//                        for(c1 in it.result.children){
//                            val orderProductName = c1.child("productName").value as String
//                            val orderProductPrice = c1.child("productPrice").value as Long
//                            val orderProductImage = c1.child("productImage").value as String
//
//                            val orderProduct = OrderProductModel(orderId, orderProductName, productCount.toString(),
//                                orderProductPrice.toString(), orderProductImage)
//
//                            orderproductList.add(orderProduct)
//                        }
//                    }
//                }
            }
        }
    }
}