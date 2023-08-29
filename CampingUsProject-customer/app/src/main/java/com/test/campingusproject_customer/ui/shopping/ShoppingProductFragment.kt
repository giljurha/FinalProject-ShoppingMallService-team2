package com.test.campingusproject_customer.ui.shopping

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentShoppingProductBinding
import com.test.campingusproject_customer.dataclassmodel.CartModel
import com.test.campingusproject_customer.repository.CartRepository
import com.test.campingusproject_customer.databinding.RowProductImageBinding
import com.test.campingusproject_customer.ui.main.MainActivity
import com.test.campingusproject_customer.viewmodel.ProductViewModel
import java.util.Locale.Category

class ShoppingProductFragment : Fragment() {
    lateinit var fragmentShoppingProductBinding: FragmentShoppingProductBinding
    lateinit var mainActivity: MainActivity

    // 상품 뷰모델
    lateinit var productViewModel: ProductViewModel

    // 이미지 변수
    var productImages = mutableListOf<Uri>()

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

        productViewModel.run {
            productName.observe(mainActivity) {
                fragmentShoppingProductBinding.textViewShoppingProductName.text = it
            }
            productPrice.observe(mainActivity) {
                fragmentShoppingProductBinding.textViewShoppingProductPrice.text = it.toString()
            }
            productInfo.observe(mainActivity) {
                fragmentShoppingProductBinding.textViewShoppingProductExplanationDetailContent.text = it
            }
            productCount.observe(mainActivity) {
                fragmentShoppingProductBinding.textViewShoppingProductNumber.text = "총 판매수량 : $it"
            }
            productDiscountRate.observe(mainActivity) {
                if(productDiscountRate.value == 0L) {
                    fragmentShoppingProductBinding.textViewShoppingProductSale.visibility = View.INVISIBLE
                }
            }
            productCategory.observe(mainActivity) {
                fragmentShoppingProductBinding.textViewShoppingProductCategory.text = it
            }

            productImageList.observe(mainActivity) { uriList ->
                productImages = uriList
                fragmentShoppingProductBinding.recyclerViewShoppingProductImage.adapter?.notifyDataSetChanged()
            }
        }

        // 회원 이름 가져오기
        val sharedPreferences = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)
        val productUserName = sharedPreferences.getString("customerUserName", null)!!

        // 번들 객체 값 가져오기
        val position = arguments?.getInt("adapterPosition")!!
        val productId = productViewModel.productList.value?.get(position)!!.productId

        // 뷰모델 상품 정보 가져오기
        productViewModel.getOneProductData(productId)

        var productName = productViewModel.productList.value?.get(position)!!.productName
        var productPrice =productViewModel.productList.value?.get(position)!!.productPrice
        var productInfo = productViewModel.productList.value?.get(position)!!.productInfo
        var productCount = productViewModel.productList.value?.get(position)!!.productCount
        var productDiscountRate = productViewModel.productList.value?.get(position)!!.productDiscountRate
        var productCategory = productViewModel.productList.value?.get(position)!!.productCategory

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
                        val cartModel = CartModel(sharedPreferences.getString("customerUserId", null).toString(), productId, 1)
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
                    newBundle.run {
                        putLong("productId", productId)
                        putString("productName", productName)
                    }
                    mainActivity.replaceFragment(MainActivity.INQUIRY_FRAGMENT, true, true, newBundle)
                }
            }

            // 여러 이미지 출력하기
            recyclerViewShoppingProductImage.run {
                adapter = ProductImageAdapter()

                // 리사이클러뷰 가로로 사용하기
                layoutManager = LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false)
            }

            textViewShoppingProductName.text = productName!!
            textViewShoppingProductNumber.text = productCount.toString()
            if (productDiscountRate == 0L) {
                textViewShoppingProductSale.visibility = View.INVISIBLE
            }
            textViewShoppingProductPrice.text = productPrice.toString() + " 원"
            textViewShoppingProductSellerName.text = productUserName
            textViewShoppingProductCategory.text = productCategory
            textViewShoppingProductExplanationDetailContent.text = productInfo
        }
        return fragmentShoppingProductBinding.root
    }

    // 이미지 리사이클러뷰
    inner class ProductImageAdapter : RecyclerView.Adapter<ProductImageAdapter.ProductImageViewHolder>(){
        inner class ProductImageViewHolder (rowProductImageBinding: RowProductImageBinding) :
            RecyclerView.ViewHolder (rowProductImageBinding.root){
            var imageViewRowProductImage : ImageView

            init {
                imageViewRowProductImage = rowProductImageBinding.imageViewRowProductImage
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageViewHolder {
            val rowProductImageBinding = RowProductImageBinding.inflate(layoutInflater)

            return ProductImageViewHolder(rowProductImageBinding)
        }

        override fun getItemCount(): Int {
            return productImages.size
        }

        override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {
            //글라이드 라이브러리로 recycler view에 이미지 출력
            Glide.with(mainActivity).load(productImages[position])
                .override(600, 600)
                .into(holder.imageViewRowProductImage)
        }
    }
}