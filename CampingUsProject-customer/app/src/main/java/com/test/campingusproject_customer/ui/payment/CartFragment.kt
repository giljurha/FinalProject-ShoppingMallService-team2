package com.test.campingusproject_customer.ui.payment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.campingusproject_customer.databinding.FragmentCartBinding
import com.test.campingusproject_customer.databinding.RowCartBinding
import com.test.campingusproject_customer.dataclassmodel.CartModel
import com.test.campingusproject_customer.dataclassmodel.CartProductModel
import com.test.campingusproject_customer.repository.CartRepository
import com.test.campingusproject_customer.ui.main.MainActivity
import com.test.campingusproject_customer.viewmodel.CartViewModel

class CartFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentCartBinding: FragmentCartBinding
    lateinit var cartList:MutableList<CartModel>
    lateinit var productList:MutableList<CartProductModel>
    lateinit var cartViewModel: CartViewModel

    var countList = arrayOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "15", "20", "25", "30", "35", "40", "45", "50", "100"
    )

    var checkedItemList = mutableListOf<CartModel>()

    val newBundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mainActivity = activity as MainActivity
        fragmentCartBinding = FragmentCartBinding.inflate(layoutInflater)



        cartViewModel = ViewModelProvider(mainActivity).get(CartViewModel::class.java)
        cartViewModel.run {
            cartDataList.observe(mainActivity) {
                cartList=it
                cartViewModel.getCProductData()

            }
            cartProductList.observe(mainActivity){
                productList = it

            }
            load.observe(mainActivity){
                // 리사이클러 뷰
                fragmentCartBinding.recyclerViewCart.run {
                    adapter = CartAdapter()
                    layoutManager = LinearLayoutManager(mainActivity)
                }
            }

        }

        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        fragmentCartBinding.run {

            val sharedPreferences = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)
            cartViewModel.getCartData(sharedPreferences.getString("customerUserId", null).toString())



            // 툴바
            toolbarCart.run {
                //백버튼 설정
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.CART_FRAGMENT)
                }
            }



            // 구매하기 버튼
            buttonCartBuy.run {
                setOnClickListener {
//                    newBundle.putParcelableArrayList("checkedItemList", ArrayList(checkedItemList))
//                    mainActivity.replaceFragment(MainActivity.PAYMENT_FRAGMENT, true, true, newBundle)
                    Log.d("testt","카트: ${cartList.size}")
                    Log.d("testt","제품: ${cartList.size}")
                }
            }
        }

//        Log.d("size", "${cartViewModel.cartProductList.value?.size!!}")

//        cartViewModel.getCartData(sharedPreferences.getString("customerUserName", null).toString())

        return fragmentCartBinding.root
    }


    inner class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

        inner class CartViewHolder(rowCartBinding: RowCartBinding) : RecyclerView.ViewHolder(rowCartBinding.root) {
            val checkBoxRowCart : CheckBox
            val imageViewRowCart : ImageView
            val textViewRowCartTitle : TextView
            val buttonRowCartDelete : Button
            val spinnerRowCart : Spinner
            val textViewRowCartCost : TextView

            init {
                textViewRowCartTitle = rowCartBinding.textViewRowCartTitle
                imageViewRowCart = rowCartBinding.imageViewRowCart
                buttonRowCartDelete = rowCartBinding.buttonRowCartDelete
                textViewRowCartCost = rowCartBinding.textViewRowCartCost
                spinnerRowCart = rowCartBinding.spinnerRowCart
                checkBoxRowCart = rowCartBinding.checkBoxRowCart

                spinnerRowCart.run {
                    val a1 = ArrayAdapter(mainActivity, android.R.layout.simple_spinner_item, countList)
                    a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    adapter = a1
                    // 기본값 0으로 설정
                    setSelection(0)

                    // 크기 조절을 원하는 크기로 설정
                    val layoutParams = layoutParams
                    layoutParams.width = 100.dpToPx()
                    this.layoutParams = layoutParams
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
            val rowCartBinding = RowCartBinding.inflate(layoutInflater)

            rowCartBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return CartViewHolder(rowCartBinding)
        }

        override fun getItemCount(): Int {
            return cartList.size

//            return 10
        }

        override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//            CartRepository.getProductData(cartViewModel.cartDataList.value?.get(position)?.cartProductId).toString()


//            holder.textViewRowCartTitle.text = cartViewModel.cartProductList.value?.get(position)?.productName.toString()
//            holder.textViewRowCartCost.text = cartViewModel.cartProductList.value?.get(position)?.productPrice.toString()
            holder.spinnerRowCart.run {
                onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        val cartUserId: String
                        val cartProductId: Long
                        val cartProductCount: Long

                        cartUserId = cartViewModel.cartDataList.value?.get(position)?.cartUserId.toString()
                        cartProductId = cartViewModel.cartDataList.value?.get(position)?.cartProductId!!.toLong()
                        cartProductCount = cartViewModel.cartDataList.value?.get(position)?.cartProductCount!!.toLong()

                        val cartModel = CartModel(cartUserId, cartProductId, cartProductCount)

                        // 상품 개수 설정
                        CartRepository.setCartCount(cartModel)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        // TODO("Not yet implemented")
                    }

                }
            }
            holder.checkBoxRowCart.run {
                setOnCheckedChangeListener { compoundButton, isChecked ->

                    val cartUserId: String
                    val cartProductId: Long
                    val cartProductCount: Long

                    cartUserId = cartViewModel.cartDataList.value?.get(position)?.cartUserId.toString()
                    cartProductId = cartViewModel.cartDataList.value?.get(position)?.cartProductId!!.toLong()
                    cartProductCount = cartViewModel.cartDataList.value?.get(position)?.cartProductCount!!.toLong()

                    val cartModel = CartModel(cartUserId, cartProductId, cartProductCount)

                    if(isChecked == true) {
                        checkedItemList.add(cartModel)
                    } else {
                        checkedItemList.remove(cartModel)
                    }
                }
            }
        }
    }

    // dp 단위를 px 단위로 변환
    fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    override fun onStop() {
        super.onStop()
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE
    }

}