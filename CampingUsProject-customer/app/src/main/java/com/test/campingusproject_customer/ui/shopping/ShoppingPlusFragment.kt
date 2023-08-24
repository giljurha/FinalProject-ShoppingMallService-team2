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
import com.test.campingusproject_customer.databinding.FragmentShoppingPlusBinding
import com.test.campingusproject_customer.databinding.HeaderShoppingBinding
import com.test.campingusproject_customer.databinding.RowShoppingBinding
import com.test.campingusproject_customer.databinding.RowShoppingPlusBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class ShoppingPlusFragment : Fragment() {
    lateinit var fragmentShoppingPlusBinding: FragmentShoppingPlusBinding
    lateinit var mainActivity: MainActivity
    lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShoppingPlusBinding = FragmentShoppingPlusBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentShoppingPlusBinding.run {
            // 툴바
            toolbarShoppingPlus.run {
                title = "흐음.."

                // drawerLayout 설정
                setNavigationIcon(R.drawable.menu_24px)
                setNavigationOnClickListener {
                    drawerLayoutShopping.open()
                }
            }

            // 드루어 레이아웃
            navigationViewShoppingPlus.run {
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
                            toolbarShoppingPlus.title = "실시간 랭킹"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingPopularitySale -> { // 인기특가
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShoppingPlus.title = "인기특가"
                            drawerLayoutShopping.close()
                        }

                        // 상품
                        R.id.itemShoppingTotalProduct -> { // 전체 상품
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShoppingPlus.title = "전체 상품"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingCap -> { // 모자
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShoppingPlus.title = "모자"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingTop -> { // 상의
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShoppingPlus.title = "상의"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingBottoms -> { // 하의
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShoppingPlus.title = "하의"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingShoes -> { // 신발
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShoppingPlus.title = "신발"
                            drawerLayoutShopping.close()
                        }

                        // 캠핑용품
                        R.id.itemShoppingTent -> { // 텐트
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShoppingPlus.title = "텐트"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingStove -> { // 난로
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShoppingPlus.title = "난로"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingFireWood -> { // 장작
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShoppingPlus.title = "장작"
                            drawerLayoutShopping.close()
                        }
                        R.id.itemShoppingContainer -> { // 용기
                            mainActivity.replaceFragment(MainActivity.SHOPPING_PLUS_FRAGMENT, false, true, null)
                            toolbarShoppingPlus.title = "용기"
                            drawerLayoutShopping.close()
                        }
                    }
                    false
                }
            }

            // 리사이클러뷰
            recyclerViewShoppingPlusProduct.run {
                adapter = ShoppingPlusProductAdapter()
                layoutManager = GridLayoutManager(context, 3)
            }
        }
        return fragmentShoppingPlusBinding.root
    }

    // 상품을 보여주는 리사이클러뷰
    inner class ShoppingPlusProductAdapter : RecyclerView.Adapter<ShoppingPlusProductAdapter.ShoppingPlusProductViewHolder>(){
        inner class ShoppingPlusProductViewHolder(rowShoppingPlusBinding: RowShoppingPlusBinding) : RecyclerView.ViewHolder(rowShoppingPlusBinding.root){
            val imageViewShoppingPlusImage: ImageView
            val textViewShoppingPlusName: TextView
            val textViewShoppingPlusSize: TextView
            val textViewShoppingPlusPrice: TextView
            val imageButtonShoppingPlusLiked: ImageView

            init{
                imageViewShoppingPlusImage = rowShoppingPlusBinding.imageViewShoppingPlusImage
                textViewShoppingPlusName = rowShoppingPlusBinding.textViewShoppingPlusName
                textViewShoppingPlusSize = rowShoppingPlusBinding.textViewShoppingPlusSize
                textViewShoppingPlusPrice = rowShoppingPlusBinding.textViewShoppingPlusPrice
                imageButtonShoppingPlusLiked = rowShoppingPlusBinding.imageButtonShoppingPlusLiked
            }
        }

        override fun onCreateViewHolder( parent: ViewGroup,viewType: Int): ShoppingPlusFragment.ShoppingPlusProductAdapter.ShoppingPlusProductViewHolder {
            val rowShoppingPlusBinding = RowShoppingPlusBinding.inflate(layoutInflater)
            val shoppingPlusProductViewHolder = ShoppingPlusProductViewHolder(rowShoppingPlusBinding)

            rowShoppingPlusBinding.root.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.SHOPPING_FRAGMENT, true,true, null)
            }

            rowShoppingPlusBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return shoppingPlusProductViewHolder
        }

        override fun getItemCount(): Int {
            return 9
        }

        override fun onBindViewHolder(holder: ShoppingPlusProductAdapter.ShoppingPlusProductViewHolder, position: Int) {
            holder.imageViewShoppingPlusImage.setImageResource(R.drawable.ic_launcher_foreground)
            holder.textViewShoppingPlusName.text = "운동화"
            holder.textViewShoppingPlusSize.text = "250"
            holder.textViewShoppingPlusPrice.text = "30000원"
        }
    }

    //뒤로가기 버튼 눌렀을 때 동작할 코드 onDetech까지
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.removeFragment(MainActivity.SHOPPING_PLUS_FRAGMENT)
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