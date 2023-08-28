package com.test.campingusproject_customer.ui.comunity

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentPostWriteBinding
import com.test.campingusproject_customer.databinding.RowPostImageBinding
import com.test.campingusproject_customer.dataclassmodel.PostModel
import com.test.campingusproject_customer.repository.PostRepository
import com.test.campingusproject_customer.ui.main.MainActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostWriteFragment : Fragment() {
    lateinit var fragmentPostWriteBinding: FragmentPostWriteBinding
    lateinit var mainActivity: MainActivity
    lateinit var callback: OnBackPressedCallback
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    //게시판 종류
    val boardTypeList = arrayOf(
        "자유게시판", "캠핑게시판", "유머게시판"
    )
    var boardType:Long = 999L

    var postImageList = mutableListOf<Uri>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentPostWriteBinding = FragmentPostWriteBinding.inflate(layoutInflater)


        //앨범 런처 초기화
        albumLauncher = albumSetting()

        fragmentPostWriteBinding.run {
            materialToolbarPostWrite.run {
                textViewPostWriteToolbarTitle.text = "게시글 작성"

                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    // 키보드가 열려있으면 내림
                    val inputMethodManager = mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                    mainActivity.removeFragment(MainActivity.POST_WRITE_FRAGMENT)
                }
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_item_album ->{
                            //앨범 이동
                            val albumIntent = Intent(Intent.ACTION_PICK)
                            albumIntent.setType("image/*")

                            albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                            albumLauncher.launch(albumIntent)
                        }
                    }
                    true
                }
            }
            // 게시판 종류 버튼
            buttonPostWritePostType.run{
                setOnClickListener {
                    val builder = MaterialAlertDialogBuilder(mainActivity)
                    builder.setTitle("게시판 종류")
                    builder.setItems(boardTypeList){ dialogInterface: DialogInterface, i: Int ->
                        boardType = i.toLong() + 2
                        text = boardTypeList[boardType.toInt()-2]
                    }
                    builder.setNegativeButton("취소", null)
                    builder.show()
                }
            }

            //저장 눌렀을 떄
            buttonPostWriteSave.run {
                setOnClickListener {
                    var checkTitle = 0 // 제목 체크
                    var checkContents = 0 // 내용 체크
                    var checkBoardType = 0 // 게시판 종류 체크

                    //제목 안썼을 경우
                    if(editTextPostWriteInputTitle.text.length == 0){
                        checkTitle = 0
                        //다이얼로그 띄움
                        val builder = MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog)
                        builder.run {
                            setTitle("제목 입력")
                            setMessage("게시글의 제목을 입력해주세요.")
                            setNegativeButton("닫기") { dialogInterface: DialogInterface, i: Int ->
                            }
                            show()
                        }
                    }
                    else{
                        checkTitle = 1
                    }
                    //내용 안썼을 경우
                    if(editTextPostWriteInputContents.text.length == 0){
                        checkContents = 0
                        //다이얼로그 띄움
                        val builder = MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog)
                        builder.run {
                            setTitle("내용 입력")
                            setMessage("게시글의 내용을 입력해주세요.")
                            setNegativeButton("닫기") { dialogInterface: DialogInterface, i: Int ->
                            }
                            show()
                        }
                    }
                    else{
                        checkContents = 1
                    }

                    //게시판 선택 안했을 경우
                    if(boardType == 999L){
                        checkBoardType = 0
                        //다이얼로그 띄움
                        val builder = MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog)
                        builder.run {
                            setTitle("게시판 선택")
                            setMessage("게시판을 선택해주세요.")
                            setNegativeButton("닫기") { dialogInterface: DialogInterface, i: Int ->
                            }
                            show()
                        }
                    }
                    else{
                        checkBoardType = 1
                    }

                    //저장
                    if(checkTitle == 1 && checkContents == 1 && checkBoardType == 1){
                        //좋아요 수와 댓글 수는 처음엔 0이니까 초기화 필요 없음
                        var postIdx:Long = 0L // 게시글 인덱스 번호
                        var postUserId = "" // 게시글 작성자 ID
                        var postType = 0L // 게시판 종류
                        var postSubject = "" // 제목
                        var postText = "" // 내용
                        var postWriteDate = "" // 작성일
                        var postImagePath = "" // 첨부이미지 파일 이름

                        // 게시글 작성자 ID
                        val sharedPreferences = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)
                        postUserId =  sharedPreferences.getString("customerUserId", null).toString()

                        // 게시판 종류
                        postType = boardType

                        // 제목
                        postSubject = editTextPostWriteInputTitle.text.toString()

                        // 내용
                        postText = editTextPostWriteInputContents.text.toString()

                        // 게시글 인덱스 번호
                        PostRepository.getPostIdx {
                            postIdx = it.result.value as Long
                            //게시글 인덱스 증가
                            postIdx++

                            // 작성일
                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                            postWriteDate = sdf.format(Date(System.currentTimeMillis())).toString()

                            // 첨부이미지 파일 이름
                            if(postImageList.isEmpty()){
                                postImagePath = "null"
                            }else{
                                //이미지 저장될 파일 경로를 지정
                                postImagePath = "PostImage/$postUserId/$postIdx/"
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
                                postImagePath
                            )

                            //게시글 저장
                            PostRepository.addPostInfo(postModel){
                                PostRepository.setPostIdx(postIdx){
                                    if(postImagePath != "null") {
                                        PostRepository.uploadImages(postImageList, postImagePath) {
                                            mainActivity.removeFragment(MainActivity.POST_WRITE_FRAGMENT)
                                            val newBundle = Bundle()
                                            newBundle.putLong("PostIdx", postIdx)
                                            mainActivity.replaceFragment(MainActivity.POST_READ_FRAGMENT, true, true, newBundle)
                                        }
                                    }
                                    else{
                                        mainActivity.removeFragment(MainActivity.POST_WRITE_FRAGMENT)
                                        val newBundle = Bundle()
                                        newBundle.putLong("PostIdx", postIdx)
                                        mainActivity.replaceFragment(MainActivity.POST_READ_FRAGMENT, true, true, newBundle)
                                    }
                                }
                            }
                        }

                        //mainActivity.replaceFragment(MainActivity.POST_READ_FRAGMENT,true,true,null)
                    }
                }
            }

            //사진 리사이클러뷰
            recyclerViewPostWriteImage.run {
                adapter = PhotoAdapter()
                //가로로 배열
                layoutManager = LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false)
            }
        }

        return fragmentPostWriteBinding.root
    }

    //이미지 리사이클러뷰
    inner class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>(){
        inner class PhotoViewHolder(rowPostImageBinding: RowPostImageBinding) : RecyclerView.ViewHolder(rowPostImageBinding.root) {
            val imageViewRowPostImage : ImageView // 게시판 사진
            val imageButtonRowPostImageDelete : ImageButton // 삭제 버튼 사진

            init {
                imageViewRowPostImage = rowPostImageBinding.imageViewRowPostImage
                imageButtonRowPostImageDelete = rowPostImageBinding.imageButtonRowPostImageDelete

                imageButtonRowPostImageDelete.setOnClickListener {
                    postImageList.removeAt(adapterPosition)
                    fragmentPostWriteBinding.recyclerViewPostWriteImage.adapter?.notifyDataSetChanged()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            val rowPostImageBinding = RowPostImageBinding.inflate(layoutInflater)

            return PhotoViewHolder(rowPostImageBinding)
        }

        override fun getItemCount(): Int {
            return postImageList.size
        }

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            //bitmap factory option 사용해 비트맵 크기 줄임
            val option = BitmapFactory.Options()
            option.inSampleSize = 4

            val inputStream = mainActivity.contentResolver.openInputStream(postImageList[position])
            val bitmap = BitmapFactory.decodeStream(inputStream, null, option)

            //회전 각도값을 가져옴
            val degree = getDegree(postImageList[position])

            //회전 이미지를 생성한다
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            val rotateBitmap = Bitmap.createBitmap(bitmap!!, 0, 0, bitmap.width, bitmap.height, matrix, false)

            //글라이드 라이브러리로 recycler view에 이미지 출력
            Glide.with(mainActivity).load(rotateBitmap)
                .override(500, 500)
                .into(holder.imageViewRowPostImage)
            }
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
                        postImageList.add(imageUri!!)
                    }
                }
                //한장 선택한 경우
                else{
                    it.data?.data?.let { uri ->
                        val imageUri = uri
                        if(imageUri != null){
                            postImageList.add(imageUri)
                        }
                    }
                }
                //recycler view 갱신
                fragmentPostWriteBinding.recyclerViewPostWriteImage.adapter?.notifyDataSetChanged()
            }
        }
        return albumLauncher
    }

    // 이미지 파일에 기록되어 있는 회전 정보를 가져온다.
    fun getDegree(uri:Uri) : Int{
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


    //뒤로가기 버튼 눌렀을 때 동작할 코드 onDetech까지
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.removeFragment(MainActivity.POST_WRITE_FRAGMENT)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}