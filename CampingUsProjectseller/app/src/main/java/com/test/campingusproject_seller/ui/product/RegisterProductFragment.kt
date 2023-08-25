package com.test.campingusproject_seller.ui.product

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentRegisterProductBinding
import com.test.campingusproject_seller.databinding.RowProductImageBinding
import com.test.campingusproject_seller.dataclassmodel.ProductModel
import com.test.campingusproject_seller.repository.ProductRepository
import com.test.campingusproject_seller.ui.main.MainActivity
import java.io.IOException
import kotlin.NumberFormatException

class RegisterProductFragment : Fragment() {

    lateinit var fragmentRegisterProductBinding: FragmentRegisterProductBinding
    lateinit var mainActivity: MainActivity
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    var productImageList = mutableListOf<Uri>()

    var keywordList : HashMap<String, Boolean> = hashMapOf("해변" to false, "계곡" to false, "호수" to false,
        "산" to false, "강" to false, "숲" to false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentRegisterProductBinding = FragmentRegisterProductBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        //앨범 런처 초기화
        albumLauncher = albumSetting()

        fragmentRegisterProductBinding.run {

            //spinner 목록 셋팅
            spinnerRegisterProductCount.run {
                val a1 = ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, MainActivity.productCountList)
                a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                adapter = a1
                //기본값 0으로 설정
                setSelection(0)
            }

            //이미지 추가 버튼 클릭 이벤트 - 앨범 이동
            imageButtonRegisterProductImage.setOnClickListener{
                val albumIntent = Intent(Intent.ACTION_PICK)
                albumIntent.setType("image/*")

                albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                albumLauncher.launch(albumIntent)
            }

            //툴바
            materialToolbarRegisterProduct.run {
                title = "제품 등록"

                inflateMenu(R.menu.menu_submit)
                //등록 아이콘 클릭 이벤트
                setOnMenuItemClickListener {

                    if(it.itemId == R.id.menuItemSubmit){

                        //상품 Id 값 가져옴
                        ProductRepository.getProductId {
                            var productId = it.result.value as Long
                            val productCount = spinnerRegisterProductCount.selectedItem.toString().toLong()

                            //제품 이름 입력 검사
                            val productName = textInputEditTextRegisterProductName.text.toString()
                            if(productName.isEmpty()){
                                textInputLayoutEmptyError(textInputLayoutRegisterProductName, "제품 이름을 입력하세요")
                                return@getProductId
                            }
                            else{
                                textInputLayoutRegisterProductName.error = null
                            }

                            //제품 가격 입력 검사
                            val productPrice = try{
                                textInputEditTextRegisterProductPrice.text.toString().toLong()
                            }catch(e:NumberFormatException){
                                textInputLayoutEmptyError(textInputLayoutRegisterProductPrice, "제품 가격을 입력하세요")
                                return@getProductId
                            }
                            textInputLayoutRegisterProductPrice.error = null

                            //제품 정보 입력 검사
                            val productInfo = textInputEditTextRegisterProductExplanation.text.toString()
                            if(productInfo.isEmpty()){
                                textInputLayoutEmptyError(textInputLayoutRegisterProductExplanation, "제품 설명을 입력하세요")
                                return@getProductId
                            }else{
                                textInputLayoutRegisterProductExplanation.error = null
                            }

                            // userId 값 임시 지정
                            val productSellerId = "jieun"

                            //브랜드명 입력 검사
                            val productBrand = textInputEditTextRegisterProductBrand.text.toString()
                            if(productBrand.isEmpty()){
                                textInputLayoutEmptyError(textInputLayoutRegisterProductBrand, "브랜드를 입력해주세요")
                                return@getProductId
                            }else{
                                textInputLayoutRegisterProductBrand.error = null
                            }

                            //할인율 입력 검사
                            //특가 등록 상태이면 값 받아오고 아니면 0 반환
                            //값 받아올때 입력 안된 상태면 toLong()에서 NumberFormatException 발생하는 것 이용해 입력 오류 출력
                            val productDiscountRate = if(switchRegisterProductRegistDiscount.isChecked){
                                try{
                                    textInputEditTextRegisterProductDiscountRate.text.toString().toLong()
                                }catch(e:NumberFormatException){
                                    textInputLayoutEmptyError(textInputLayoutRegisterProductDiscountRate, "할인율을 입력해주세요")
                                    return@getProductId
                                }
                            }else{
                                0
                            }
                            textInputLayoutRegisterProductDiscountRate.error = null

                            //이미지 개수 검사
                            val fileDir = if(productImageList.isEmpty()){
                                Snackbar.make(mainActivity.activityMainBinding.root, "이미지를 등록해주세요.", Snackbar.LENGTH_SHORT).show()
                                return@getProductId
                            }else{
                                //이미지 저장될 파일 경로를 저장
                                "ProductImage/$productSellerId/$productId/"
                            }

                            //상품 수량에 따라 판매 상태 값 결정
                            val productSellingStatus = if(productCount!=0L) true else false

                            //상품 객체 생성
                            val product = ProductModel(productId, productSellerId, productName,
                                productPrice, fileDir, productInfo, productCount, productSellingStatus,
                                productDiscountRate, 0L, productBrand, keywordList)

                            //제품 등록
                            ProductRepository.addProductInfo(product){
                                productId++

                                //증가된 productId 값 저장
                                ProductRepository.setProductId(productId){
                                    ProductRepository.uploadImages(productImageList, fileDir){
                                        Snackbar.make(mainActivity.activityMainBinding.root, "저장되었습니다.", Snackbar.LENGTH_SHORT).show()
                                        mainActivity.removeFragment(MainActivity.REGISTER_PRODUCT_FRAGMENT)
                                    }
                                }
                            }
                        }

                    }
                    false
                }

                //백버튼 설정
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.REGISTER_PRODUCT_FRAGMENT)
                }
            }

            //특가 등록 스위치
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

            //이미지 리싸이클러뷰 설정
            recyclerViewRegisterProductImage.run {
                adapter = RegisterProductAdapter()

                //recycler view 가로로 확장되게 함
                layoutManager = LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false)
            }

            //키워드 칩 체크 상태 변경 설정
            chipCheckStatusChanged(chipRegisterProductForest)
            chipCheckStatusChanged(chipRegisterProductBeach)
            chipCheckStatusChanged(chipRegisterProductLake)
            chipCheckStatusChanged(chipRegisterProductMountain)
            chipCheckStatusChanged(chipRegisterProductRiver)
            chipCheckStatusChanged(chipRegisterProductValley)

        }

        return fragmentRegisterProductBinding.root
    }

    //어댑터 클래스
    inner class RegisterProductAdapter : RecyclerView.Adapter<RegisterProductAdapter.RegisterProductViewHolder>(){
        inner class RegisterProductViewHolder (rowProductImageBinding: RowProductImageBinding) :
                RecyclerView.ViewHolder(rowProductImageBinding.root){
                    var imageViewRowProductImage : ImageView
                    var imageButtonRowDelete : ImageButton

                    init {
                        imageViewRowProductImage = rowProductImageBinding.imageViewRowProductImage
                        imageButtonRowDelete = rowProductImageBinding.imageButtonRowDelete

                        imageButtonRowDelete.setOnClickListener {
                            productImageList.removeAt(adapterPosition)
                            fragmentRegisterProductBinding.recyclerViewRegisterProductImage.adapter?.notifyDataSetChanged()
                        }
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
            //bitmap factory option 사용해 비트맵 크기 줄임
            val option = BitmapFactory.Options()
            option.inSampleSize = 4

            val inputStream = mainActivity.contentResolver.openInputStream(productImageList[position])
            val bitmap = BitmapFactory.decodeStream(inputStream, null, option)

            //글라이드 라이브러리로 recycler view에 이미지 출력
            Glide.with(mainActivity).load(bitmap)
                .override(500, 500)
                .into(holder.imageViewRowProductImage)

        }
    }

    //이미지 회전 상태값 구하는 함수
    fun getOrientationOfImage(uri:Uri): Int{
        val inputStream = mainActivity.contentResolver.openInputStream(uri)
        val exif : ExifInterface? = try{
            ExifInterface(inputStream!!)
        }catch (e:IOException){
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

    //앨범 설정 함수
    fun albumSetting() : ActivityResultLauncher<Intent>{
        //앨범에서 이미지 가져오기
        val albumLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

            //이미지 가져오기 성공
            if(it.resultCode == RESULT_OK){

                //사진 여러장 선택한 경우
                if(it.data?.clipData != null){
                    val count = it.data?.clipData?.itemCount

                    for(idx in 0 until count!!){
                        val imageUri = it.data?.clipData?.getItemAt(idx)?.uri

                        productImageList.add(imageUri!!)
                    }
                }
                //한장 선택한 경우
                else{
                    it.data?.data?.let { uri ->
                        val imageUri = uri

                        if(imageUri != null){
                            productImageList.add(imageUri)
                        }
                    }
                }

                //recycler view 갱신
                fragmentRegisterProductBinding.recyclerViewRegisterProductImage.adapter?.notifyDataSetChanged()
            }
        }
        return albumLauncher
    }

    //chip의 체크 상태 변경에 따라 hashmap에 체크상태를 저장하는 함수
    fun chipCheckStatusChanged(chip: Chip){
        chip.setOnCheckedChangeListener { compoundButton, checked ->
            keywordList[compoundButton.text.toString()] = checked
            //Log.d("chipchecked", "${compoundButton.text}_${keywordList[compoundButton.text.toString()]}")
        }
    }
}