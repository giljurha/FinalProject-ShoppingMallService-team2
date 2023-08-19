package com.test.campingusproject_seller.ui.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentModifyProductBinding
import com.test.campingusproject_seller.ui.main.MainActivity

class ModifyProductFragment : Fragment() {

    lateinit var fragmentModifyProductBinding: FragmentModifyProductBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentModifyProductBinding = FragmentModifyProductBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentModifyProductBinding.run {
            materialToolbarModifyProduct.run {
                title = "제품 수정"

                inflateMenu(R.menu.menu_submit)

                setOnMenuItemClickListener {
                    if(it.itemId == R.id.menuItemSubmit){
                        //등록 아이템 클릭 처리
                    }
                    false
                }

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MODIFY_PRODUCT_FRAGMENT)
                }
            }

            switchModifyProductRegistDiscount.setOnCheckedChangeListener { compoundButton, b ->
                if(b){
                    textInputLayoutModifyProductDiscountRate.visibility = View.VISIBLE
                    textInputEditTextModifyProductDiscountRate.visibility = View.VISIBLE
                }
                else{
                    //스위치 off시 입력되어있던 내용 초기화
                    textInputEditTextModifyProductDiscountRate.setText("")

                    textInputLayoutModifyProductDiscountRate.visibility = View.GONE
                    textInputEditTextModifyProductDiscountRate.visibility = View.GONE
                }
            }
        }

        return fragmentModifyProductBinding.root
    }

}