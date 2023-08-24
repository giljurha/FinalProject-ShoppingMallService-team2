package com.test.campingusproject_customer.ui.payment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentCartBinding
import com.test.campingusproject_customer.databinding.RowCartBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class CartFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentCartBinding: FragmentCartBinding

    var countList = arrayOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "15", "20", "25", "30", "35", "40", "45", "50", "100"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mainActivity = activity as MainActivity
        fragmentCartBinding = FragmentCartBinding.inflate(layoutInflater)

        fragmentCartBinding.run {

            // 툴바
            toolbarCart.run {
                //백버튼 설정
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.CART_FRAGMENT)
                }
            }

            // 리사이클러 뷰
            recyclerViewCart.run {
                adapter = CartAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
            }

            buttonCartBuy.run {
                setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.PAYMENT_FRAGMENT, true, true, null)
                }
            }
        }

        return fragmentCartBinding.root
    }

    // 카트 어댑터
    inner class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

        inner class CartViewHolder(rowCartBinding: RowCartBinding) : RecyclerView.ViewHolder(rowCartBinding.root) {
            val checkBoxRowCart : CheckBox
            val imageViewRowCart : ImageView
            val textViewRowCartTitle : TextView
            val buttonRowCartDelete : Button
            val spinnerRowCart : Spinner
            val textViewRowCartCost : TextView

            init {
                checkBoxRowCart = rowCartBinding.checkBoxRowCart
                imageViewRowCart = rowCartBinding.imageViewRowCart
                textViewRowCartTitle = rowCartBinding.textViewRowCartTitle
                buttonRowCartDelete = rowCartBinding.buttonRowCartDelete
                spinnerRowCart = rowCartBinding.spinnerRowCart
                textViewRowCartCost = rowCartBinding.textViewRowCartCost

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
            return 10
        }

        override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
            holder.textViewRowCartTitle.text = "title $position"

        }
    }

    // dp 단위를 px 단위로 변환
    fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

}