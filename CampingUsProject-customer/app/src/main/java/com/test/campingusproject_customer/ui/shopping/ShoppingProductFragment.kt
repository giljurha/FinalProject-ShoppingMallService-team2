package com.test.campingusproject_customer.ui.shopping

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentShoppingProductBinding
import com.test.campingusproject_customer.repository.ProductRepository.Companion.getOneProductData
import com.test.campingusproject_customer.ui.main.MainActivity
import com.test.campingusproject_customer.viewmodel.ProductViewModel
import java.util.Locale.Category

class ShoppingProductFragment : Fragment() {
    lateinit var fragmentShoppingProductBinding: FragmentShoppingProductBinding
    lateinit var mainActivity: MainActivity

    // 상품 뷰모델
    lateinit var productViewModel: ProductViewModel

    // 다음 화면으로 넘겨줄 번들
    val newBundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShoppingProductBinding = FragmentShoppingProductBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // 상품 뷰모델 객체 생성
        productViewModel = ViewModelProvider(mainActivity)[ProductViewModel::class.java]

        // 번들 객체 값 가져오기
        val productId = arguments?.getLong("productId")

//        // 뷰모델 상품 정보 가져오기
//        productViewModel.getOneProductData(productId!!)
//
//        var productName = productViewModel.productName.value?.get(productId.toInt()).toString()
//        var productPrice = productViewModel.productPrice.value.toString()
//        var productImage = productViewModel.productImageList
//        var productInfo = productViewModel.productInfo.toString()
//        var productCount = productViewModel.productCount.toString()
//        var productSellingStatus = productViewModel.productSellingStatus.toString()
//        var productDiscountRate = productViewModel.productDiscountRate.toString()
//        var productRecommendationCount = productViewModel.productRecommendationCount.toString()
//        var productBrand = productViewModel.productBrand.toString()
//        var productCategory = productViewModel.productCategory.toString()

        var productSellerId = arguments?.getString("productSellerId")
        var productName = arguments?.getString("productName")
        var productPrice = arguments?.getLong("productPrice")
        var productImage = arguments?.getString("productImage")
        var productInfo = arguments?.getString("productInfo")
        var productCount = arguments?.getLong("productCount")
        var productSellingStatus = arguments?.getBoolean("productSellingStatus")
        var productDiscountRate = arguments?.getLong("productDiscountRate")
        var productRecommendationCount = arguments?.getLong("productRecommendationCount")
        var productBrand = arguments?.getString("productBrand")
        var productCategory = arguments?.getString("productCategory")

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
                    newBundle.run {
                        putLong("productId", productId!!)
                        putString("productName", productName)
                    }
                    mainActivity.replaceFragment(MainActivity.INQUIRY_FRAGMENT, true, true, newBundle)
                }
            }

            // 출력 View에 데이터 세팅하기
            imageViewShoppingProductImage.setImageResource(R.drawable.circle_20px)
            textViewShoppingProductName.text = productName!!
            textViewShoppingProductNumber.text = productCount.toString()
            if (productDiscountRate == 0L) {
                textViewShoppingProductSale.visibility = View.INVISIBLE
            }
            textViewShoppingProductPrice.text = productPrice.toString() + " 원"
            textViewShoppingProductSellerName.text = productName
            textViewShoppingProductCategory.text = productCategory
            textViewShoppingProductExplanationDetailContent.text = productInfo
        }
        return fragmentShoppingProductBinding.root
    }
}