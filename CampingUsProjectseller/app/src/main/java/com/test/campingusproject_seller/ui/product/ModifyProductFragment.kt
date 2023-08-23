package com.test.campingusproject_seller.ui.product

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentModifyProductBinding
import com.test.campingusproject_seller.databinding.RowProductImageBinding
import com.test.campingusproject_seller.dataclassmodel.ProductModel
import com.test.campingusproject_seller.repository.ProductRepository
import com.test.campingusproject_seller.ui.main.MainActivity
import com.test.campingusproject_seller.viewmodel.ProductViewModel
import java.io.IOException

class ModifyProductFragment : Fragment() {

    lateinit var fragmentModifyProductBinding: FragmentModifyProductBinding
    lateinit var mainActivity: MainActivity
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    lateinit var productViewModel: ProductViewModel

    var productImages = mutableListOf<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentModifyProductBinding = FragmentModifyProductBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        //상품 뷰모델 객체 생성
        productViewModel = ViewModelProvider(mainActivity)[ProductViewModel::class.java]
        productViewModel.run {
            productName.observe(mainActivity){
                fragmentModifyProductBinding.textInputEditTextModifyProductName.setText(it)
            }
            productPrice.observe(mainActivity){
                fragmentModifyProductBinding.textInputEditTextModifyProductPrice.setText(it.toString())
            }
            productImageList.observe(mainActivity){ uriList->
                productImages = uriList
                fragmentModifyProductBinding.recyclerViewModifyProductImage.adapter?.notifyDataSetChanged()
            }
            productInfo.observe(mainActivity){
                fragmentModifyProductBinding.textInputEditTextModifyProductExplanation.setText(it)
            }
            productCount.observe(mainActivity){
                val currentCountPos = if(it == 0L) 0 else (it)/5
                fragmentModifyProductBinding.spinnerModifyProductCount.setSelection(currentCountPos.toString().toInt())
            }
            productDiscountRate.observe(mainActivity){ discountRate->
                fragmentModifyProductBinding.run {
                    if(discountRate==0L){
                        textInputEditTextModifyProductDiscountRate.setText("")
                    }else{
                        textInputEditTextModifyProductDiscountRate.setText(discountRate.toString())
                    }
                }
            }
        }

        //bundle로 받은 상품 리스트 순서값
        val productIdx = arguments?.getInt("adapterPosition")!!
        val currentProductId = productViewModel.productList.value?.get(productIdx)?.productId

            //상품 정보를 가져온다
        productViewModel.getOneProductData(currentProductId!!)

        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        fragmentModifyProductBinding.run {
            materialToolbarModifyProduct.run {
                title = "제품 수정"

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MODIFY_PRODUCT_FRAGMENT)
                }

                inflateMenu(R.menu.menu_submit)

                //등록 아이콘 클릭 이벤트
                setOnMenuItemClickListener {
                    if(it.itemId == R.id.menuItemSubmit){
                        val productCount = spinnerModifyProductCount.selectedItem.toString().toLong()
                        val productName = textInputEditTextModifyProductName.text.toString()
                        val productPrice = textInputEditTextModifyProductPrice.text.toString().toLong()
                        val productInfo = textInputEditTextModifyProductExplanation.text.toString()
                        val productDiscountRate = textInputEditTextModifyProductDiscountRate.text.toString().toLong()
                        val productSellingStatus = if(productCount!=0L) true else false

                        val currentProduct = productViewModel.productList.value?.get(productIdx)!!
                        val productSellerId = currentProduct.productSellerId
                        val productImage = currentProduct.productImage
                        val productRecommendationCount = currentProduct.productRecommendationCount
                        val productId = currentProduct.productId

                        val product = ProductModel(productId, productSellerId, productName, productPrice, productImage,
                            productInfo, productCount, productSellingStatus, productDiscountRate, productRecommendationCount)

                        ProductRepository.modifyProduct(product){
                            //저장 메시지 스낵바
                            Snackbar.make(fragmentModifyProductBinding.root, "수정되었습니다.", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    false
                }
            }

            spinnerModifyProductCount.run {
                val a1 = ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, MainActivity.productCountList)
                a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                adapter = a1
                //기본값 0으로 설정
                setSelection(0)
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

            recyclerViewModifyProductImage.run {
                adapter = ModifyProductAdapter()

                //recycler view 가로로 확장되게 함
                layoutManager = LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false)
            }

//            if(productViewModel.productDiscountRate.value == 0L){
//                switchModifyProductRegistDiscount.isChecked = false
//            }else{
//                switchModifyProductRegistDiscount.isChecked = true
//            }
        }

        return fragmentModifyProductBinding.root
    }

    inner class ModifyProductAdapter : RecyclerView.Adapter<ModifyProductAdapter.ModifyProductViewHolder>(){
        inner class ModifyProductViewHolder (rowProductImageBinding: RowProductImageBinding) :
                RecyclerView.ViewHolder (rowProductImageBinding.root){
                    var imageViewRowProductImage : ImageView

                    init {
                        imageViewRowProductImage = rowProductImageBinding.imageViewRowProductImage
                    }
                }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModifyProductViewHolder {
            val rowProductImageBinding = RowProductImageBinding.inflate(layoutInflater)

            return ModifyProductViewHolder(rowProductImageBinding)
        }

        override fun getItemCount(): Int {
            return productImages.size
        }

        override fun onBindViewHolder(holder: ModifyProductViewHolder, position: Int) {
            //bitmap factory option 사용해 비트맵 크기 줄임
//            val option = BitmapFactory.Options()
//            option.inSampleSize = 4
//
//            Log.d("productImageUri", productImages[position].toString())
//            val inputStream = mainActivity.contentResolver.openInputStream(productImages[position])
//            val bitmap = BitmapFactory.decodeStream(inputStream, null, option)

            //글라이드 라이브러리로 recycler view에 이미지 출력
            Glide.with(mainActivity).load(productImages[position])
                .override(600, 600)
                .into(holder.imageViewRowProductImage)
        }
    }

    //이미지 회전 상태값 구하는 함수
    fun getOrientationOfImage(uri:Uri): Int{
        val inputStream = mainActivity.contentResolver.openInputStream(uri)
        val exif : ExifInterface? = try{
            ExifInterface(inputStream!!)
        }catch (e: IOException){
            Log.e("exifError", e.toString())
            return -1
        }
        inputStream.close()

        val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        if(orientation != -1){
            when(orientation){
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }

    //textInputLayout 입력 검사 오류 처리 함수
    fun textInputLayoutEmptyError(textInputLayout: TextInputLayout, errorMessage : String){
        textInputLayout.run {
            error = errorMessage
            setErrorIconDrawable(R.drawable.error_24px)
            requestFocus()
        }
    }

}