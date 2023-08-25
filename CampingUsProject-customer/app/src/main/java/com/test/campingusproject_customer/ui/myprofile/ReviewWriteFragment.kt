package com.test.campingusproject_customer.ui.myprofile

import android.app.Activity
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.campingusproject_customer.databinding.FragmentReviewWriteBinding
import com.test.campingusproject_customer.databinding.RowReviewWriteImageBinding
import com.test.campingusproject_customer.ui.main.MainActivity
import java.io.IOException

class ReviewWriteFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentReviewWriteBinding: FragmentReviewWriteBinding
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    var productImageList = mutableListOf<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mainActivity = activity as MainActivity
        fragmentReviewWriteBinding = FragmentReviewWriteBinding.inflate(layoutInflater)

        //앨범 런처 초기화
        albumLauncher = albumSetting()

        fragmentReviewWriteBinding.run {

            // 툴바
            toolbarReviewWrite.run {
                //백버튼 설정
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.CART_FRAGMENT)
                }
            }

            //이미지 리사이클러뷰 설정
            recyclerViewReviewWritePicture.run {
                adapter = ReviewWriteAdapter()

                //recycler view 가로로 확장되게 함
                layoutManager = LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false)
            }

            //이미지 추가 버튼 클릭 이벤트 - 앨범 이동
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
                    mainActivity.removeFragment(MainActivity.REVIEW_WRITE_FRAGMENT)
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
                    productImageList.removeAt(adapterPosition)
                    fragmentReviewWriteBinding.recyclerViewReviewWritePicture.adapter?.notifyDataSetChanged()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewWriteViewHolder {
            val rowReviewWriteImageBinding = RowReviewWriteImageBinding.inflate(layoutInflater)

            return ReviewWriteViewHolder(rowReviewWriteImageBinding)
        }

        override fun getItemCount(): Int {
            return productImageList.size
        }

        override fun onBindViewHolder(holder: ReviewWriteViewHolder, position: Int) {
            //bitmap factory option 사용해 비트맵 크기 줄임
            val option = BitmapFactory.Options()
            option.inSampleSize = 4

            val inputStream = mainActivity.contentResolver.openInputStream(productImageList[position])
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
        //앨범에서 이미지 가져오기
        val albumLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

            //이미지 가져오기 성공
            if(it.resultCode == Activity.RESULT_OK){

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
                fragmentReviewWriteBinding.recyclerViewReviewWritePicture.adapter?.notifyDataSetChanged()
            }
        }
        return albumLauncher
    }

}