package com.test.campingusproject_customer.ui.comunity

import android.content.Context
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentPostReadBinding
import com.test.campingusproject_customer.databinding.RowPostReadBinding
import com.test.campingusproject_customer.databinding.RowReadPostImageListBinding
import com.test.campingusproject_customer.ui.main.MainActivity
import com.test.campingusproject_customer.viewmodel.PostViewModel

class PostReadFragment : Fragment() {
    lateinit var fragmentPostReadBinding: FragmentPostReadBinding
    lateinit var mainActivity: MainActivity
    lateinit var callback: OnBackPressedCallback
    lateinit var postViewModel: PostViewModel
    var imageList = mutableListOf<Uri>()

    //게시판 종류
    val boardTypeList = arrayOf(
        "전체게시판","인기게시판", "자유게시판", "캠핑게시판", "유머게시판"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentPostReadBinding = FragmentPostReadBinding.inflate(layoutInflater)

        postViewModel = ViewModelProvider(mainActivity)[PostViewModel::class.java]
        postViewModel.run {
            postSubject.observe(mainActivity){
                fragmentPostReadBinding.textViewPostReadPostTitle.setText(it)
            }
            postText.observe(mainActivity){
                fragmentPostReadBinding.textViewPostReadPostContents.setText(it)
            }
            postUserId.observe(mainActivity){
                fragmentPostReadBinding.textViewPostReadUserName.setText(it)
            }
            postLiked.observe(mainActivity){
                fragmentPostReadBinding.textViewPostReadPostLike.setText(it.toString())
            }
            postCommentCount.observe(mainActivity){
                fragmentPostReadBinding.textViewPostReadPostComent.setText(it.toString())
            }
            postWriteDate.observe(mainActivity){
                fragmentPostReadBinding.textViewPostReadWriteDate.setText(it)
            }
            postType.observe(mainActivity){
                fragmentPostReadBinding.materialToolbarPostRead.title = boardTypeList[it.toInt()]
            }
            postImageList.observe(mainActivity){uriList->
                imageList = uriList
                fragmentPostReadBinding.recyclerViewPostReadImage.adapter?.notifyDataSetChanged()
            }
        }

        //번들로 게시글 번호 가져오기
        val postIdx = arguments?.getLong("PostIdx")
        postViewModel.getOnePostReadData(postIdx?.toDouble()!!)

        fragmentPostReadBinding.run {
            materialToolbarPostRead.run {
                title = "무슨 게시판?"

                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    // 키보드가 열려있으면 내림
                    val inputMethodManager =
                        mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                    mainActivity.removeFragment(MainActivity.POST_READ_FRAGMENT)
                }
            }

            buttonPostReadSaveButton.run {
                setOnClickListener {
                    val inputMethodManager =
                        mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                    mainActivity.removeFragment(MainActivity.POST_READ_FRAGMENT)
                }
            }
            recyclerViewComments.run {
                adapter = PostReadAdapter()
                layoutManager = LinearLayoutManager(mainActivity)

                //구분선 추가
                val divider = MaterialDividerItemDecoration(mainActivity, LinearLayoutManager.VERTICAL)
                divider.run {
                    setDividerColorResource(mainActivity, R.color.subColor)
                    dividerInsetStart = 30
                    dividerInsetEnd = 30
                }
                addItemDecoration(divider)
            }
            recyclerViewPostReadImage.run {
                adapter = PhotoAdapter()
                layoutManager = LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false)
            }
        }
        return fragmentPostReadBinding.root
    }

    //이미지 리사이클러뷰
    inner class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>(){
        inner class PhotoViewHolder(rowReadPostImageListBinding: RowReadPostImageListBinding) : RecyclerView.ViewHolder(rowReadPostImageListBinding.root) {
            val imageViewRowPostImage : ImageView // 게시판 사진
            init {
                imageViewRowPostImage = rowReadPostImageListBinding.imageViewReadPostImage
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            val rowReadPostImageListBinding = RowReadPostImageListBinding.inflate(layoutInflater)

            return PhotoViewHolder(rowReadPostImageListBinding)
        }

        override fun getItemCount(): Int {
            return imageList.size
        }

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            //글라이드 라이브러리로 recycler view에 이미지 출력
            Glide.with(mainActivity).load(imageList[position])
                .override(500, 500)
                .into(holder.imageViewRowPostImage)
        }
    }

    // 이미지 파일에 기록되어 있는 회전 정보를 가져온다.
    fun getDegree(uri: Uri) : Int{
        var exifInterface: ExifInterface? = null

        // 사진 파일로 부터 tag 정보를 관리하는 객체를 추출한다.
        try {
            val inputStream = mainActivity.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                exifInterface = ExifInterface(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var degree = 0
        if(exifInterface != null){
            // 각도 값을 가지고온다.
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

            when(orientation){
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        }
        return degree
    }

    //댓글 리사이클러 뷰
    inner class PostReadAdapter : RecyclerView.Adapter<PostReadFragment.PostReadAdapter.PostReadViewHolder>(){
        inner class PostReadViewHolder(rowPostReadBinding: RowPostReadBinding) : RecyclerView.ViewHolder(rowPostReadBinding.root) {
            val textViewRowCommentUserName : TextView // 유저 이름
            val textViewRowCommentWriteDate : TextView // 댓글 작성 날짜
            val textViewRowCommentContents : TextView // 댓글 내용

            init {
                textViewRowCommentUserName = rowPostReadBinding.textViewRowCommentUserName
                textViewRowCommentWriteDate = rowPostReadBinding.textViewRowCommentWriteDate
                textViewRowCommentContents = rowPostReadBinding.textViewRowCommentContents
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostReadViewHolder {
            val rowPostReadBinding = RowPostReadBinding.inflate(layoutInflater)

            rowPostReadBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return PostReadViewHolder(rowPostReadBinding)
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: PostReadViewHolder, position: Int) {
            holder.textViewRowCommentUserName.text = "차은우"
            holder.textViewRowCommentWriteDate.text = "2023-08-23 17:13"
            holder.textViewRowCommentContents.text = "풉ㅋ"
        }
    }


    //뒤로가기 버튼 눌렀을 때 동작할 코드 onDetech까지
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.removeFragment(MainActivity.POST_READ_FRAGMENT)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}