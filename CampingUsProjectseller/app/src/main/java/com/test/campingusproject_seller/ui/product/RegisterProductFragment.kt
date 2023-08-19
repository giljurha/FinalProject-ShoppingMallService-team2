package com.test.campingusproject_seller.ui.product

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentRegisterProductBinding
import com.test.campingusproject_seller.dataclassmodel.ProductModel
import com.test.campingusproject_seller.repository.ProductRepository
import com.test.campingusproject_seller.ui.main.MainActivity
import java.lang.NumberFormatException

class RegisterProductFragment : Fragment() {

    lateinit var fragmentRegisterProductBinding: FragmentRegisterProductBinding
    lateinit var mainActivity: MainActivity
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    val productCountList = arrayOf(
        "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50",
        "55", "60", "65", "70", "75", "80", "85", "90", "95", "100"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentRegisterProductBinding = FragmentRegisterProductBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        

        fragmentRegisterProductBinding.run {

            spinnerRegisterProductCount.run {
                val a1 = ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, productCountList)
                a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                adapter = a1
                //기본값 0으로 설정
                setSelection(0)
            }

            materialToolbarRegisterProduct.run {
                title = "제품 등록"

                inflateMenu(R.menu.menu_submit)

                setOnMenuItemClickListener {
                    //등록 버튼 클릭 이벤트
                    if(it.itemId == R.id.menuItemSubmit){

                        ProductRepository.getProductId {
                            var productId = it.result.value as Long
                            val productName = textInputEditTextRegisterProductName.text.toString()
                            val productPrice = textInputEditTextRegisterProductPrice.text.toString().toLong()
                            val productCount = spinnerRegisterProductCount.selectedItem.toString().toLong()
                            val productInfo = textInputEditTextRegisterProductExplanation.text.toString()

                            //할인율이 등록되어있지 않으면 Long타입으로 변환할 때 오류 발생하므로 0 반환 처리
                            val productDiscountRate = try{
                                textInputEditTextRegisterProductDiscountRate.text.toString().toLong()
                            }catch(e:NumberFormatException){
                                0
                            }

                            val productImage = null //임시

                            //상품 수량에 따라 판매 상태 결정
                            val productSellingStatus = if(productCount!=0L) true else false

                            //상품 객체 생성
                            val product = ProductModel(productId, "jieun", productName,
                                productPrice, productImage, productInfo, productCount, productSellingStatus,
                                productDiscountRate, 0L)

                            ProductRepository.addProductInfo(product){
                                productId++
                                ProductRepository.setProductId(productId){
                                    //이미지 저장 처리
                                }
                            }
                        }

                    }
                    false
                }

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.REGISTER_PRODUCT_FRAGMENT)
                }
            }

            switchRegisterProductRegistDiscount.setOnCheckedChangeListener { compoundButton, b ->
                if(b){
                    textInputEditTextRegisterProductDiscountRate.visibility = View.VISIBLE
                    textInputLayoutRegisterProductDiscountRate.visibility = View.VISIBLE
                }
                else{
                    //스위치 off시 입력되어있던 내용 초기화
                    textInputEditTextRegisterProductDiscountRate.setText("")

                    textInputEditTextRegisterProductDiscountRate.visibility = View.GONE
                    textInputLayoutRegisterProductDiscountRate.visibility = View.GONE
                }
            }
        }

        return fragmentRegisterProductBinding.root
    }

}