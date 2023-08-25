package com.test.campingusproject_customer.ui.shopping

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.search.SearchView
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentShoppingBinding
import com.test.campingusproject_customer.databinding.HeaderShoppingBinding
import com.test.campingusproject_customer.databinding.RowShoppingBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class ShoppingFragment : Fragment() {
    lateinit var fragmentShoppingBinding: FragmentShoppingBinding
    lateinit var mainActivity: MainActivity
    lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentShoppingBinding = FragmentShoppingBinding.inflate(layoutInflater)

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

            // 드루어 레이아웃
            navigationViewShopping.run {
                //헤더 설정
                val headerShoppingBinding = HeaderShoppingBinding.inflate(inflater)
                headerShoppingBinding.textViewShoppingHeaderUserName.text = "김민우 님"
                addHeaderView(headerShoppingBinding.root)

                // 쇼핑 화면에 들어왔을 시 실시간 랭킹을 보여줌
                menu.findItem(R.id.itemShoppingRealTimeRanking).setIcon(R.drawable.circle_black_20px)

                // 항목 선택 시 동작 리스너
                setNavigationItemSelectedListener {
                    when(it.itemId) {
                        // 특별
                        R.id.itemShoppingRealTimeRanking -> { // 실시간 랭킹
                            toolbarShopping.title = "실시간 랭킹"

                            menu.findItem(R.id.itemShoppingRealTimeRanking).setIcon(R.drawable.circle_black_20px)

                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingPopularitySale -> { // 인기특가
                            toolbarShopping.title = "인기특가"

                            menu.findItem(R.id.itemShoppingPopularitySale).setIcon(R.drawable.circle_black_20px)

                            drawerLayoutShopping.close()
                        }
                        // 캠핑용품
                        R.id.itemShoppingTentAndTarp -> { // 텐트 / 타프
                            toolbarShopping.title = "텐트 / 타프"

                            menu.findItem(R.id.itemShoppingTentAndTarp).setIcon(R.drawable.circle_black_20px)

                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingSleepingBagAndMat -> { // 침낭 / 매트
                            toolbarShopping.title = "침낭 / 매트"

                            menu.findItem(R.id.itemShoppingSleepingBagAndMat).setIcon(R.drawable.circle_black_20px)

                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingTableAndChair -> { // 테이블 / 의자
                            toolbarShopping.title = "테이블 / 의자"

                            menu.findItem(R.id.itemShoppingTableAndChair).setIcon(R.drawable.circle_black_20px)

                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingLanternAndLight -> { // 랜턴 / 조명
                            toolbarShopping.title = "랜턴 / 조명"

                            menu.findItem(R.id.itemShoppingLanternAndLight).setIcon(R.drawable.circle_black_20px)

                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingKitchen -> { // 키친
                            toolbarShopping.title = "키친"

                            menu.findItem(R.id.itemShoppingKitchen).setIcon(R.drawable.circle_black_20px)

                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingBrazierAndGrill -> { // 화로 / 그릴
                            toolbarShopping.title = "화로 / 그릴"

                            menu.findItem(R.id.itemShoppingBrazierAndGrill).setIcon(R.drawable.circle_black_20px)

                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingSeasonalItems -> { // 계절용품
                            toolbarShopping.title = "계절용품"

                            menu.findItem(R.id.itemShoppingSeasonalItems).setIcon(R.drawable.circle_black_20px)

                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingContainer -> { // 용기
                            toolbarShopping.title = "용기"

                            menu.findItem(R.id.itemShoppingContainer).setIcon(R.drawable.circle_black_20px)

                            drawerLayoutShopping.close()
                        }
                    }
                    true
                }
            }
            // 리사이클러뷰
            recyclerViewShoppingProduct.run {
                adapter = ShoppingProductAdapter()
                layoutManager = GridLayoutManager(context, 3)
            }
        }

        return fragmentShoppingBinding.root
    }

    // 상품을 보여주는 리사이클러뷰
    inner class ShoppingProductAdapter : RecyclerView.Adapter<ShoppingProductAdapter.ShoppingProductViewHolder>(){
        inner class ShoppingProductViewHolder(rowShoppingBinding: RowShoppingBinding) : RecyclerView.ViewHolder(rowShoppingBinding.root){
            val imageViewShoppingImage: ImageView
            val textViewShoppingName: TextView
            val textViewShoppingSize: TextView
            val textViewShoppingPrice: TextView
            val imageButtonLiked: ImageView

            init{
                imageViewShoppingImage = rowShoppingBinding.imageViewShoppingImage
                textViewShoppingName = rowShoppingBinding.textViewShoppingName
                textViewShoppingSize = rowShoppingBinding.textViewShoppingSize
                textViewShoppingPrice = rowShoppingBinding.textViewShoppingPrice
                imageButtonLiked = rowShoppingBinding.imageButtonLiked
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
            return 9
        }

        override fun onBindViewHolder(holder: ShoppingProductAdapter.ShoppingProductViewHolder, position: Int) {
            holder.imageViewShoppingImage.setImageResource(R.drawable.ic_launcher_foreground)
            holder.textViewShoppingName.text = "운동화"
            holder.textViewShoppingSize.text = "250"
            holder.textViewShoppingPrice.text = "30000원"
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