package com.test.campingusproject_customer.ui.myprofile

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentModifyMyPostBinding
import com.test.campingusproject_customer.databinding.RowPostImageBinding
import com.test.campingusproject_customer.databinding.RowReadPostImageListBinding
import com.test.campingusproject_customer.dataclassmodel.PostModel
import com.test.campingusproject_customer.repository.PostRepository
import com.test.campingusproject_customer.ui.main.MainActivity
import com.test.campingusproject_customer.viewmodel.PostViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ModifyMyPostFragment : Fragment() {
    lateinit var fragmentModifyMyPostBinding: FragmentModifyMyPostBinding

    lateinit var mainActivity: MainActivity

    lateinit var postViewModel: PostViewModel

    var imageList = mutableListOf<Uri?>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentModifyMyPostBinding = FragmentModifyMyPostBinding.inflate(layoutInflater)

        postViewModel = ViewModelProvider(mainActivity)[PostViewModel::class.java]
        postViewModel.run {
            postSubject.observe(mainActivity) {
                fragmentModifyMyPostBinding.editTextModifyMyPostInputTitle.setText(it)
            }
            postText.observe(mainActivity) {
                fragmentModifyMyPostBinding.editTextModifyMyPostInputContents.setText(it)
            }
            postImageList.observe(mainActivity) {
                imageList = it
                fragmentModifyMyPostBinding.recyclerViewPostModifyImage.adapter?.notifyDataSetChanged()
            }
        }

        val postIdx = arguments?.getLong("PostIdx")
        postViewModel.getOnePostReadData(postIdx?.toDouble()!!)

        fragmentModifyMyPostBinding.run {
            materialToolbarModifyMyPost.run {
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MODIFY_MY_POST_FRAGMENT)
                }
            }
            //저장버튼
            buttonModifyMyPostSave.run {
                setOnClickListener {
                    //저장....
                    var checkTitle = 0 // 제목 체크
                    var checkContents = 0 // 내용 체크

                    //제목 안썼을 경우
                    if (editTextModifyMyPostInputTitle.text.length == 0) {
                        checkTitle = 0
                        //다이얼로그 띄움
                        val builder = MaterialAlertDialogBuilder(
                            mainActivity,
                            R.style.ThemeOverlay_App_MaterialAlertDialog
                        )
                        builder.run {
                            setTitle("제목 입력")
                            setMessage("게시글의 제목을 입력해주세요.")
                            setNegativeButton("닫기") { dialogInterface: DialogInterface, i: Int ->
                            }
                            show()
                        }
                    } else {
                        checkTitle = 1
                    }
                    //내용 안썼을 경우
                    if (editTextModifyMyPostInputContents.text.length == 0) {
                        checkContents = 0
                        //다이얼로그 띄움
                        val builder = MaterialAlertDialogBuilder(
                            mainActivity,
                            R.style.ThemeOverlay_App_MaterialAlertDialog
                        )
                        builder.run {
                            setTitle("내용 입력")
                            setMessage("게시글의 내용을 입력해주세요.")
                            setNegativeButton("닫기") { dialogInterface: DialogInterface, i: Int ->
                            }
                            show()
                        }
                    } else {
                        checkContents = 1
                    }

                    //저장
                    if (checkTitle == 1 && checkContents == 1) {
                        //좋아요 수와 댓글 수는 처음엔 0이니까 초기화 필요 없음
                        var postIdx: Long = postIdx // 게시글 인덱스 번호
                        var postUserId = "" // 게시글 작성자 ID
                        var postType = 0L // 게시판 종류
                        var postSubject = "" // 제목
                        var postText = "" // 내용
                        var postWriteDate = "" // 작성일
                        var postImagePath = "" // 첨부이미지 파일 이름
                        var profileImagePath = ""
                        // 게시글 작성자 ID
                        val sharedPreferences = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)
                        postUserId = sharedPreferences.getString("customerUserId", null).toString()

                        // 제목
                        postSubject = editTextModifyMyPostInputTitle.text.toString()

                        // 내용
                        postText = editTextModifyMyPostInputContents.text.toString()

                        //이미지 경로
                        postImagePath = postViewModel.postImagePath.value.toString()

                        // 첨부이미지 파일 이름
                        if (imageList.isEmpty()) {
                                postImagePath = "null"
                        } else {
                            //이미지 저장될 파일 경로를 지정
                            postImagePath = "PostImage/$postUserId/$postIdx/"
                        }

                        var userProfileImage = sharedPreferences.getString("customerUserProfileImage", null)

                        if(userProfileImage?.isEmpty()!!){
                            profileImagePath = "null"
                        }
                        else{
                            profileImagePath = "CustomerUserProfile/$postUserId/1"
                        }

                        //객체 생성
                        val postModel = PostModel(
                            postIdx,
                            postUserId,
                            postType,
                            postSubject,
                            postText,
                            postLiked = 0 ,
                            postCommentCount = 0,
                            postWriteDate,
                            postImagePath,
                            profileImagePath
                        )
                        //수정본 저장
                        PostRepository.modifyPost(postModel){
                            if (postImagePath != "null") {
                                val newBundle = Bundle()
                                newBundle.putLong("PostIdx", postIdx)
                                mainActivity.removeFragment(MainActivity.MODIFY_MY_POST_FRAGMENT)
                                mainActivity.replaceFragment(MainActivity.POST_READ_FRAGMENT,true, true, newBundle)

                            }
                            else{
                                val newBundle = Bundle()
                                newBundle.putLong("PostIdx", postIdx)
                                mainActivity.removeFragment(MainActivity.MODIFY_MY_POST_FRAGMENT)
                                mainActivity.replaceFragment(MainActivity.POST_READ_FRAGMENT, true, true, newBundle)
                            }
                        }
                    }
                }
            }
            recyclerViewPostModifyImage.run {
                adapter = PhotoAdapter()
                layoutManager = LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false)
            }
        }

        return fragmentModifyMyPostBinding.root
    }

    //이미지 리사이클러뷰
    inner class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
        inner class PhotoViewHolder(rowPostImageBinding: RowReadPostImageListBinding) :
            RecyclerView.ViewHolder(rowPostImageBinding.root) {
            val imageViewRowPostImage: ImageView // 게시판 사진

            init {
                imageViewRowPostImage = rowPostImageBinding.imageViewReadPostImage
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            val rowPostImageBinding = RowReadPostImageListBinding.inflate(layoutInflater)

            return PhotoViewHolder(rowPostImageBinding)
        }

        override fun getItemCount(): Int {
            return imageList.size
        }

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            //글라이드 라이브러리로 recycler view에 이미지 출력
            Glide.with(mainActivity)
                .load(imageList[position])
                .override(500, 500)
                .into(holder.imageViewRowPostImage)
        }
    }
}