package com.test.campingusproject_customer.ui.shopping

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentShoppingProductBinding
import com.test.campingusproject_customer.dataclassmodel.CartModel
import com.test.campingusproject_customer.repository.CartRepository
import com.test.campingusproject_customer.ui.main.MainActivity

class ShoppingProductFragment : Fragment() {
    lateinit var fragmentShoppingProductBinding: FragmentShoppingProductBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShoppingProductBinding = FragmentShoppingProductBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        val sharedPreferences = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)


        fragmentShoppingProductBinding.run {
            //툴바
            toolbarShoppingProduct.run {
                title = "쇼핑"
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.SHOPPING_PRODUCT_FRAGMENT)
                }
            }

            // 장바구니 담기 클릭시 다이얼로그
            buttonShoppingProductToCart.run {
                setOnClickListener { // 버튼 클릭시 다이얼로그
                    MaterialAlertDialogBuilder(mainActivity, R.style.ThemeOverlay_App_MaterialAlertDialog).run {
                        val cartModel = CartModel(sharedPreferences.getString("customerUserId", null).toString(), 41, 1)
                        CartRepository.addCartData(cartModel) {

                        }

                        setTitle("장바구니 담기 완료")
                        setMessage("장바구니로 이동하시겠습니까?")
                        setPositiveButton("쇼핑 계속하기") { dialogInterface: DialogInterface, i: Int ->
                            mainActivity.removeFragment(MainActivity.SHOPPING_PRODUCT_FRAGMENT)
                        }
                        setNegativeButton("장바구니로 이동") { dialogInterface: DialogInterface, i: Int ->
                            mainActivity.replaceFragment(MainActivity.CART_FRAGMENT, true, true, null)
                        }
                        show()
                    }
                }
            }

            // 구매 버튼 클릭시 이동
            buttonShoppingProductToBuy.run {
                setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.PAYMENT_FRAGMENT, true, true, null)
                }
            }

            // 리뷰버튼 클릭시 화면 이동
            buttonToggleShoppingProductReview.run {
                setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.REVIEW_FRAGMENT, true, true, null)
                }
            }


            // 플로팅 버튼 클릭시 문의등록 화면 이동
            floatingActionButtonShoppingProductInquriry.run {
                setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.INQUIRY_FRAGMENT, true, true, null)
                }
            }
        }
        return fragmentShoppingProductBinding.root
    }
}