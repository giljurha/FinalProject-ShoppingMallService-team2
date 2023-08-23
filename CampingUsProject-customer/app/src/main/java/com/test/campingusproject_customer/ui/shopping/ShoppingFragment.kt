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
            navigationViewShopping.run {
                //헤더 설정
                val headerShoppingBinding = HeaderShoppingBinding.inflate(inflater)
                headerShoppingBinding.textViewShoppingHeaderUserName.text = "김민우 님"
                addHeaderView(headerShoppingBinding.root)

                // 항목 선택 시 동작 리스너
                setNavigationItemSelectedListener {
                    when(it.itemId) {
                        // 특별
                        R.id.itemShoppingRealTimeRanking -> { // 실시간 랭킹
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShopping.title = "실시간 랭킹"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingPopularitySale -> { // 인기특가
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShopping.title = "인기특가"
                            drawerLayoutShopping.close()
                        }

                        // 상품
                        R.id.itemShoppingTotalProduct -> { // 전체 상품
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShopping.title = "전체 상품"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingCap -> { // 모자
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShopping.title = "모자"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingTop -> { // 상의
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShopping.title = "상의"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingBottoms -> { // 하의
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShopping.title = "하의"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingShoes -> { // 신발
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShopping.title = "신발"
                            drawerLayoutShopping.close()
                        }

                        // 캠핑용품
                        R.id.itemShoppingTent -> { // 텐트
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShopping.title = "텐트"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingStove -> { // 난로
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShopping.title = "난로"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingFireWood -> { // 장작
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShopping.title = "장작"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingContainer -> { // 용기
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShopping.title = "용기"
                            drawerLayoutShopping.close()
                        }
                    }
                    false
                }
            }

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

            rowShoppingBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return shoppingProductViewHolder
        }

        override fun getItemCount(): Int {
            return 9
        }

        override fun onBindViewHolder(holder: ShoppingProductViewHolder, position: Int) {
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