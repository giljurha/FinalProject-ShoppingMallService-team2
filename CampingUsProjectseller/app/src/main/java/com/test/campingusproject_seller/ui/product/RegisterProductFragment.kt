package com.test.campingusproject_seller.ui.product

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.carousel.CarouselLayoutManager
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentRegisterProductBinding
import com.test.campingusproject_seller.databinding.RowProductImageBinding
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

    var productImageList = mutableListOf<Bitmap>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentRegisterProductBinding = FragmentRegisterProductBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        albumLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val option = BitmapFactory.Options()
            option.inSampleSize = 4

            //이미지 가져오기 성공
            if(it.resultCode == RESULT_OK){

                //사진 여러장 선택한 경우
                if(it.data?.clipData != null){
                    val count = it.data?.clipData?.itemCount

                    for(idx in 0 until count!!){
                        val imageUri = it.data?.clipData?.getItemAt(idx)?.uri

                        val inputStream = mainActivity.contentResolver.openInputStream(imageUri!!)
                        val bitmap = BitmapFactory.decodeStream(inputStream, null, option)

                        inputStream?.close()

                        productImageList.add(bitmap!!)
                    }
                }
                //한장 선택한 경우
                else{
                        it.data?.data?.let { uri ->
                            val imageUri = uri

                            val inputStream = mainActivity.contentResolver.openInputStream(imageUri!!)
                            val bitmap = BitmapFactory.decodeStream(inputStream, null, option)

                            inputStream?.close()

                            if(bitmap != null){
                                productImageList.add(bitmap)
                            }
                        }
                }

                //recycler view 갱신
                fragmentRegisterProductBinding.recyclerViewRegisterProductImage.adapter?.notifyDataSetChanged()
            }
        }

        fragmentRegisterProductBinding.run {

            //spinner 목록 셋팅
            spinnerRegisterProductCount.run {
                val a1 = ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, productCountList)
                a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                adapter = a1
                //기본값 0으로 설정
                setSelection(0)
            }

            //이미지 추가 버튼 클릭 이벤트
            imageButtonRegisterProductImage.setOnClickListener{
                val albumIntent = Intent(Intent.ACTION_PICK)
                albumIntent.setType("image/*")

                albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                albumLauncher.launch(albumIntent)
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

            recyclerViewRegisterProductImage.run {
                adapter = RegisterProductAdapter()

                //recycler view 가로로 확장되게 함
                layoutManager = LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false)
            }
        }

        return fragmentRegisterProductBinding.root
    }

    inner class RegisterProductAdapter : RecyclerView.Adapter<RegisterProductAdapter.RegisterProductViewHolder>(){
        inner class RegisterProductViewHolder (rowProductImageBinding: RowProductImageBinding) :
                RecyclerView.ViewHolder(rowProductImageBinding.root){
                    var imageViewRowProductImage : ImageView

                    init {
                        imageViewRowProductImage = rowProductImageBinding.imageViewRowProductImage
                    }
                }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterProductViewHolder {
            val rowProductImageBinding = RowProductImageBinding.inflate(layoutInflater)

            return RegisterProductViewHolder(rowProductImageBinding)
        }

        override fun getItemCount(): Int {
            return productImageList.size
        }

        override fun onBindViewHolder(holder: RegisterProductViewHolder, position: Int) {

            Glide.with(mainActivity).load(productImageList[position])
                .override(500, 500)
                .into(holder.imageViewRowProductImage)

        }
    }

}