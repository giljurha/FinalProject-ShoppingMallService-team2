package com.test.campingusproject_customer.ui.shopping

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentShoppingBinding
import com.test.campingusproject_customer.databinding.HeaderShoppingBinding
import com.test.campingusproject_customer.databinding.RowShoppingBinding
import com.test.campingusproject_customer.dataclassmodel.ProductModel
import com.test.campingusproject_customer.repository.ProductRepository
import com.test.campingusproject_customer.ui.main.MainActivity
import com.test.campingusproject_customer.viewmodel.ProductViewModel

class ShoppingFragment : Fragment() {
    lateinit var fragmentShoppingBinding: FragmentShoppingBinding
    lateinit var mainActivity: MainActivity
    lateinit var callback: OnBackPressedCallback

    lateinit var productViewModel: ProductViewModel

    var productCheckedList = mutableListOf<Boolean>()

    // 드루어 레이아웃에 나오는 카테고리 아이템 메뉴
    val categoryIdList = arrayOf(
        R.id.itemShoppingRealTimeRanking,
        R.id.itemShoppingPopularitySale,
        R.id.itemShoppingTentAndTarp,
        R.id.itemShoppingSleepingBagAndMat,
        R.id.itemShoppingTableAndChair,
        R.id.itemShoppingLanternAndLight,
        R.id.itemShoppingKitchen,
        R.id.itemShoppingBrazierAndGrill,
        R.id.itemShoppingSeasonalItems,
        R.id.itemShoppingContainer
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentShoppingBinding = FragmentShoppingBinding.inflate(layoutInflater)

        // 상품 뷰모델 객체 생성
        productViewModel = ViewModelProvider(mainActivity)[ProductViewModel::class.java]

        productViewModel.run {
            productList.observe(mainActivity) {
                fragmentShoppingBinding.recyclerViewShoppingProduct.adapter?.notifyDataSetChanged()

                productCheckedList.clear()
                for(i in 0 until productList.value?.size!!){
                    productCheckedList.add(false)
                }
            }
        }

        fragmentShoppingBinding.run {
            // 툴바
            toolbarShopping.run {
                title = "쇼핑"

                // drawerLayout 설정
                setNavigationIcon(R.drawable.menu_24px)
                setNavigationOnClickListener {
                    drawerLayoutShopping.open()
                }
            }

            // 리사이클러뷰
            recyclerViewShoppingProduct.run {
                recyclerViewShoppingProduct.run {
                    productViewModel.getAllProductData()

                    adapter = ShoppingProductAdapter()
                    layoutManager = GridLayoutManager(context, 3)
                }
            }

            // 드루어 레이아웃
            navigationViewShopping.run {
                itemIconTintList
                //헤더 설정
                val headerShoppingBinding = HeaderShoppingBinding.inflate(inflater)
                headerShoppingBinding.textViewShoppingHeaderUserName.text = "김민우 님"
                addHeaderView(headerShoppingBinding.root)
//
//                // 쇼핑 화면에 들어왔을 시 보여줄 항목
//                menu.findItem(R.id.itemShoppingRealTimeRanking).setIcon(R.drawable.circle_20px)

                // 항목 선택 시 동작 리스너
                setNavigationItemSelectedListener {
                    when(it.itemId) {
                        // 특별
                        R.id.itemShoppingRealTimeRanking -> { // 실시간 랭킹
                            toolbarShopping.title = "실시간 랭킹"
                            getProductRealTimeRankingData()
                            setNavigationIcon(it)
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingPopularitySale -> { // 인기특가
                            toolbarShopping.title = "인기특가"
                            getProductDisCountData()
                            setNavigationIcon(it)
                            drawerLayoutShopping.close()
                        }

                        // 캠핑용품
                        R.id.itemShoppingTentAndTarp -> { // 텐트 / 타프
                            toolbarShopping.title = "텐트 / 타프"
                            getProductCategoryData("텐트 / 타프")
                            setNavigationIcon(it)
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingSleepingBagAndMat -> { // 침낭 / 매트
                            toolbarShopping.title = "침낭 / 매트"
                            getProductCategoryData("침낭 / 매트")
                            setNavigationIcon(it)
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingTableAndChair -> { // 테이블 / 의자
                            toolbarShopping.title = "테이블 / 의자"
                            getProductCategoryData("테이블 / 의자")
                            setNavigationIcon(it)
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingLanternAndLight -> { // 랜턴 / 조명
                            toolbarShopping.title = "랜턴 / 조명"
                            getProductCategoryData("랜턴 / 조명")
                            setNavigationIcon(it)
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingKitchen -> { // 키친
                            toolbarShopping.title = "키친"
                            getProductCategoryData("키친")
                            setNavigationIcon(it)
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingBrazierAndGrill -> { // 화로 / 그릴
                            toolbarShopping.title = "화로 / 그릴"
                            getProductCategoryData("화로 / 그릴")
                            setNavigationIcon(it)
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingSeasonalItems -> { // 계절용품
                            toolbarShopping.title = "계절용품"
                            getProductCategoryData("계절용품")
                            setNavigationIcon(it)
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingContainer -> { // 용기
                            toolbarShopping.title = "용기"
                            getProductCategoryData("용기")
                            setNavigationIcon(it)
                            drawerLayoutShopping.close()
                        }
                    }
                    true
                }
            }
        }

        return fragmentShoppingBinding.root
    }

    // 상품을 보여주는 리사이클러뷰
    inner class ShoppingProductAdapter : RecyclerView.Adapter<ShoppingProductAdapter.ShoppingProductViewHolder>(){
        inner class ShoppingProductViewHolder(rowShoppingBinding: RowShoppingBinding) : RecyclerView.ViewHolder(rowShoppingBinding.root){
            val progressBarRowShopping: ProgressBar
            val imageViewShoppingImage: ImageView
            val textViewShoppingName: TextView
            val textViewShoppingPrice: TextView
            var imageShoppingLiked: ImageView

            init{
                progressBarRowShopping = rowShoppingBinding.progressBarRowShopping
                imageViewShoppingImage = rowShoppingBinding.imageViewShoppingImage
                textViewShoppingName = rowShoppingBinding.textViewShoppingName
                textViewShoppingPrice = rowShoppingBinding.textViewShoppingPrice
                imageShoppingLiked = rowShoppingBinding.imageShoppingLiked
            }
        }

        override fun onCreateViewHolder( parent: ViewGroup,viewType: Int): ShoppingProductViewHolder {
            val rowShoppingBinding = RowShoppingBinding.inflate(layoutInflater)
            val shoppingProductViewHolder = ShoppingProductViewHolder(rowShoppingBinding)

            rowShoppingBinding.root.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.SHOPPING_PRODUCT_FRAGMENT, true,true, null)
            }

            rowShoppingBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return shoppingProductViewHolder
        }

        override fun getItemCount(): Int {
            return productViewModel.productList.value?.size!!
        }

        override fun onBindViewHolder(holder: ShoppingProductAdapter.ShoppingProductViewHolder, position: Int) {
            // 이미지
            // 상품에 등록된 이미지 경로로 첫 번째 이미지만 불러와 표시
            ProductRepository.getProductFirstImage(productViewModel.productList.value?.get(position)?.productImage!!){ uri->
                //글라이드 라이브러리로 이미지 표시
                //이미지 로딩 완료되거나 실패하기 전까지 프로그래스바 활성화
                Glide.with(mainActivity).load(uri.result)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            holder.progressBarRowShopping.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: com.bumptech.glide.load.DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            holder.progressBarRowShopping.visibility = View.GONE
                            return false
                        }
                    })
                        // 이미지 크기
                    .override(200, 200)
                        // 사용할 뷰
                    .into(holder.imageViewShoppingImage)
            }
            // 상품 이름
            holder.textViewShoppingName.text = productViewModel.productList.value?.get(position)?.productName

            // 상품 가격
            if(productViewModel.productList.value?.get(position)?.productSellingStatus == false) {
                holder.textViewShoppingPrice.text = "품절"
            } else if (productViewModel.productList.value?.get(position)!!.productDiscountRate != 0L) {
                // 할인율 계산
                var discountRate = productViewModel.productList.value?.get(position)!!.productDiscountRate * 0.01

                val result = (productViewModel.productList.value?.get(position)!!.productPrice * discountRate).toInt().toString()

                holder.textViewShoppingPrice.text = " ${ result } 원"
            } else {
                holder.textViewShoppingPrice.text = " ${ productViewModel.productList.value?.get(position)?.productPrice.toString() } 원"
            }

            // 좋아요 클릭시
            holder.imageShoppingLiked.run {
                setOnClickListener {
                    ProductRepository.likeButtonClicked(productViewModel.productList.value?.get(position)!!.productId, productViewModel.productList.value?.get(position)!!.productRecommendationCount) {
                        holder.imageShoppingLiked.setImageResource(R.drawable.favorite_fill_24px)
                    }
                    true
                }
            }
        }
    }

    // 카테고리 클릭시 아이콘 변경
    fun setNavigationIcon(menuItem: MenuItem) {
        for(item in categoryIdList){
            if(item == menuItem.itemId){
                menuItem.setIcon(R.drawable.circle_20px)
            }
            else{
                fragmentShoppingBinding.navigationViewShopping.menu.findItem(item).setIcon(null)
            }
        }
    }

    // 실시간 랭킹 상품 정보 가져오기
    fun getProductRealTimeRankingData() {
        fragmentShoppingBinding.run {
            recyclerViewShoppingProduct.run {
                productViewModel.getAllProductRealTimeRankingData()

                adapter = ShoppingProductAdapter()
                layoutManager = GridLayoutManager(context, 3)
            }
        }
    }

    // 인기특가 상품 정보 가져오기
    fun getProductDisCountData() {
        fragmentShoppingBinding.run {
            recyclerViewShoppingProduct.run {
                productViewModel.getAllProductDiscountData()

                adapter = ShoppingProductAdapter()
                layoutManager = GridLayoutManager(context, 3)
            }
        }
    }

    // 카테고리별 상품 정보 가져오기
    fun getProductCategoryData(category: String) {
        fragmentShoppingBinding.run {
            recyclerViewShoppingProduct.run {
                productViewModel.getAllProductCategoryData(category)

                adapter = ShoppingProductAdapter()
                layoutManager = GridLayoutManager(context, 3)
            }
        }
    }

    //뒤로가기 버튼 눌렀을 때 동작할 코드 onDetech까지
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.removeFragment(MainActivity.SHOPPING_FRAGMENT)
                mainActivity.activityMainBinding.bottomNavigationViewMain.selectedItemId = R.id.menuItemHome
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}