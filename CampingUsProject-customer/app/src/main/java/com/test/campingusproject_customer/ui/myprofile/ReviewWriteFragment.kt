package com.test.campingusproject_customer.ui.myprofile

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
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
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentReviewWriteBinding
import com.test.campingusproject_customer.databinding.RowReviewWriteImageBinding
import com.test.campingusproject_customer.dataclassmodel.ReviewModel
import com.test.campingusproject_customer.repository.ProductRepository
import com.test.campingusproject_customer.repository.ReviewRepository
import com.test.campingusproject_customer.ui.main.MainActivity
import com.test.campingusproject_customer.viewmodel.ProductViewModel
import com.test.campingusproject_customer.viewmodel.ReviewViewModel
import java.io.IOException

class ReviewWriteFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    lateinit var fragmentReviewWriteBinding: FragmentReviewWriteBinding
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    var reviewImageList = mutableListOf<Uri>()
    var reviewRecommendationCount = 0L

    var productImages = mutableListOf<Uri>()

    // 뷰모델
    lateinit var reviewViewModel: ReviewViewModel
    lateinit var productViewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentReviewWriteBinding = FragmentReviewWriteBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // 뷰모델 객체
        reviewViewModel = ViewModelProvider(mainActivity)[ReviewViewModel::class.java]
        productViewModel = ViewModelProvider(mainActivity)[ProductViewModel::class.java]

        productViewModel.run {
            productName.observe(mainActivity) {
                fragmentReviewWriteBinding.textViewReviewWriteTitle.text = it
            }
            productPrice.observe(mainActivity) {
                fragmentReviewWriteBinding.textViewReviewWritePrice.text = "$it 원"
            }
            productImageList.observe(mainActivity) { uri ->
                productImages = uri
            }
            productRecommendationCount.observe(mainActivity) {
                reviewRecommendationCount = it
            }
        }

        // 회원 이름 가져오기
        val sharedPreferences = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("customerUserId", null)!!

        // 번들 객체로 상품 id 가져오기
        //var productId = arguments?.getInt("productId")
        var productId = 48 // 테스트를 위해서 임이로 id값 지정

        // 좋아요 수 데이터 가져오기
        productViewModel.getOneProductData(productId.toLong())
        // reviewRecommendationCount = productViewModel.productRecommendationCount.value!!

        //앨범 런처 초기화
        albumLauncher = albumSetting()

        fragmentReviewWriteBinding.run {
            var starScore = 0.0F                 // 별점
            var recommendationCheck = false      // 추천 눌렀는지 확인
            
            // 툴바
            toolbarReviewWrite.run {
                //백버튼 설정
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.CART_FRAGMENT)
                }
            }

            // 상품에 등록된 이미지 경로로 첫 번째 이미지만 불러와 표시
            imageViewReviewWrite.run {
                //글라이드 라이브러리로 이미지 표시
                Glide.with(mainActivity).load(productImages)
                    .override(200, 200)
                    .into(imageViewReviewWrite)
            }

            // 레이팅바 (별점)
            ratingBarReviewWrite.run {
                setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    starScore = rating
                }
            }

            // 추천
            imageViewReviewWriteLiked.run {
                setOnClickListener {
                    setImageResource(R.drawable.favorite_fill_24px)
                    recommendationCheck = true

                    if(recommendationCheck) {
                        ProductRepository.likeButtonClicked(productId.toLong(), reviewRecommendationCount) {
                            Snackbar.make(mainActivity.activityMainBinding.root, "추천되었습니다.", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            // 이미지 리사이클러뷰 설정
            recyclerViewReviewWritePicture.run {
                adapter = ReviewWriteAdapter()

                //recycler view 가로로 확장되게 함
                layoutManager = LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false)
            }

            // 이미지 추가 버튼 클릭 이벤트 - 앨범 이동
            imageButtonReviewWriteRegisterPicture.setOnClickListener{
                val albumIntent = Intent(Intent.ACTION_PICK)
                albumIntent.setType("image/*")

                albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                albumLauncher.launch(albumIntent)
            }

            // 취소 버튼
            buttonReviewWriteCancel.run {
                setOnClickListener {
                    mainActivity.removeFragment(MainActivity.REVIEW_WRITE_FRAGMENT)
                }
            }

            // 완료 버튼
            buttonReviewWriteDone.run {
                setOnClickListener {
                    ReviewRepository.getReviewIdx {
                        var reviewId = it.result.value as Long
                        Log.d("ㅁㅇ", reviewId.toString())

                        // 유효성 검사
                        val reviewWriteText = textInputEditTextReviewWrite.text.toString()
                        if(reviewWriteText.isEmpty()) {
                            textInputLayoutReviewWrite.run {
                                error = "리뷰 내용을 작성해주세요."
                                setErrorIconDrawable(R.drawable.error_24px)
                                requestFocus()
                            }
                            return@getReviewIdx
                        } else {
                            textInputLayoutReviewWrite.error = null
                        }

                        if(starScore == 0F) {
                            // 다이얼로그
                            val builder = MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog)
                            builder.run {
                                setTitle("별점 오류")
                                setMessage("별점을 선택해주세요.")
                                setNegativeButton("닫기") { dialogInterface: DialogInterface, i: Int ->
                                    ratingBarReviewWrite.requestFocus()
                                }
                                show()
                                return@getReviewIdx
                            }
                        }

                        val fileDir = if(reviewImageList.isEmpty()) {
                            Snackbar.make(mainActivity.activityMainBinding.root, "이미지를 등록해주세요.", Snackbar.LENGTH_SHORT).show()
                            return@getReviewIdx
                        } else {
                            //이미지 저장될 파일 경로를 저장
                            "ReviewImage/$productId/$userId/"
                        }

                        // 리뷰 객체
                        val review = ReviewModel(reviewId, productId.toLong(), userId, starScore, fileDir, textInputEditTextReviewWrite.text.toString())

                        ReviewRepository.setReviewInfo(review) {
                            reviewId++

                            // 증가된 reviewId 값 저장
                            ReviewRepository.setReviewIdx(reviewId) {
                                ReviewRepository.uploadImages(reviewImageList, fileDir) {
                                    Snackbar.make(mainActivity.activityMainBinding.root, "저장되었습니다.", Snackbar.LENGTH_SHORT).show()
                                    mainActivity.removeFragment(MainActivity.REVIEW_WRITE_FRAGMENT)
                                }
                            }
                        }
                    }
                }
            }
        }

        return fragmentReviewWriteBinding.root
    }

    // 이미지 어댑터 클래스
    inner class ReviewWriteAdapter : RecyclerView.Adapter<ReviewWriteAdapter.ReviewWriteViewHolder>(){
        inner class ReviewWriteViewHolder (rowReviewWriteImageBinding: RowReviewWriteImageBinding) :
            RecyclerView.ViewHolder(rowReviewWriteImageBinding.root){
            var imageViewRowReviewWriteImage : ImageView
            var imageButtonRowReviewWriteImageDelete : ImageButton

            init {
                imageViewRowReviewWriteImage = rowReviewWriteImageBinding.imageViewRowReviewWriteImage
                imageButtonRowReviewWriteImageDelete = rowReviewWriteImageBinding.imageButtonRowReviewWriteImageDelete

                imageButtonRowReviewWriteImageDelete.setOnClickListener {
                    reviewImageList.removeAt(adapterPosition)
                    fragmentReviewWriteBinding.recyclerViewReviewWritePicture.adapter?.notifyDataSetChanged()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewWriteViewHolder {
            val rowReviewWriteImageBinding = RowReviewWriteImageBinding.inflate(layoutInflater)

            return ReviewWriteViewHolder(rowReviewWriteImageBinding)
        }

        override fun getItemCount(): Int {
            return reviewImageList.size
        }

        override fun onBindViewHolder(holder: ReviewWriteViewHolder, position: Int) {
            //bitmap factory option 사용해 비트맵 크기 줄임
            val option = BitmapFactory.Options()
            option.inSampleSize = 4

            val inputStream = mainActivity.contentResolver.openInputStream(reviewImageList[position])
            val bitmap = BitmapFactory.decodeStream(inputStream, null, option)

            //글라이드 라이브러리로 recycler view에 이미지 출력
            Glide.with(mainActivity).load(bitmap)
                .override(500, 500)
                .into(holder.imageViewRowReviewWriteImage)

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

    //앨범 설정 함수
    fun albumSetting() : ActivityResultLauncher<Intent>{
        // 앨범에서 이미지 가져오기
        val albumLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            //이미지 가져오기 성공
            if(it.resultCode == Activity.RESULT_OK){
                //사진 여러장 선택한 경우
                if(it.data?.clipData != null){
                    val count = it.data?.clipData?.itemCount

                    for(idx in 0 until count!!){
                        val imageUri = it.data?.clipData?.getItemAt(idx)?.uri

                        reviewImageList.add(imageUri!!)
                    }
                }
                // 한장 선택한 경우
                else{
                    it.data?.data?.let { uri ->
                        val imageUri = uri

                        if(imageUri != null){
                            reviewImageList.add(imageUri)
                        }
                    }
                }

                // recycler view 갱신
                fragmentReviewWriteBinding.recyclerViewReviewWritePicture.adapter?.notifyDataSetChanged()
            }
        }
        return albumLauncher
    }

}