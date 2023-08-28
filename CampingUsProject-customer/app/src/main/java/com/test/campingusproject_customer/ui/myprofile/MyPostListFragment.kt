package com.test.campingusproject_customer.ui.myprofile

import android.content.Context
import android.content.DialogInterface
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentMyPostListBinding
import com.test.campingusproject_customer.databinding.RowBoardBinding
import com.test.campingusproject_customer.repository.PostRepository
import com.test.campingusproject_customer.ui.main.MainActivity
import com.test.campingusproject_customer.viewmodel.PostViewModel

class MyPostListFragment : Fragment() {
    lateinit var fragmentMyPostListBinding: FragmentMyPostListBinding
    lateinit var mainActivity: MainActivity
    lateinit var postViewModel: PostViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentMyPostListBinding = FragmentMyPostListBinding.inflate(layoutInflater)

        postViewModel = ViewModelProvider(mainActivity)[PostViewModel::class.java]
        postViewModel.run {
            postDataList.observe(mainActivity) {
                fragmentMyPostListBinding.recyclerViewMytPostList.adapter?.notifyDataSetChanged()
            }
        }

        val userId = arguments?.getString("userId").toString()
        postViewModel.getMyPostList(userId)
        postViewModel.resetImageList()

        fragmentMyPostListBinding.run {
            materialToolbarMyPostList.run {
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MY_POST_LIST_FRAGMENT)
                }
            }
            floatingActionButtonMyPostList.run {
                // 플로팅 액션 버튼 아이콘의 색상을 변경
                val fab: FloatingActionButton = findViewById(R.id.floatingActionButtonMyPostList)
                fab.setColorFilter(ContextCompat.getColor(mainActivity, R.color.highLightColor), PorterDuff.Mode.SRC_IN)

                setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.POST_WRITE_FRAGMENT,true,true,null)
                }
            }
            recyclerViewMytPostList.run {
                adapter = MyPostListAdapter()
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
        }

        return fragmentMyPostListBinding.root
    }

    //게시판 리싸이클러뷰 어댑터
    inner class MyPostListAdapter : RecyclerView.Adapter<MyPostListAdapter.MyPostListViewHolder>(){
        inner class MyPostListViewHolder(rowMyPostListBinding: RowBoardBinding) : RecyclerView.ViewHolder(rowMyPostListBinding.root) {
            val imageViewRowMypostListWriterImage : ImageView // 작성자 프로필 사진
            val textViewRowMypostListTitle : TextView // 게시글 제목
            val textViewRowMypostListWriter : TextView // 게시글 작성자
            val textViewRowMypostListLike : TextView // 좋아요 수
            val textVewRowMypostListWriteDate : TextView // 글 작성 시간
            val textViewRowMypostListComment : TextView // 댓글 수

            init {
                imageViewRowMypostListWriterImage = rowMyPostListBinding.imageViewRowBoardWriterImage
                textViewRowMypostListTitle = rowMyPostListBinding.textViewRowBoardTitle
                textViewRowMypostListWriter = rowMyPostListBinding.textViewRowBoardWriter
                textViewRowMypostListLike = rowMyPostListBinding.textViewRowBoardLike
                textVewRowMypostListWriteDate = rowMyPostListBinding.textViewRowBoardWriteDate
                textViewRowMypostListComment = rowMyPostListBinding.textViewRowBoardComment

                rowMyPostListBinding.root.setOnClickListener {
                    val readPostIdx = postViewModel.postDataList.value?.get(adapterPosition)?.postIdx
                    val newBundle = Bundle()
                    newBundle.putLong("PostIdx", readPostIdx!!)
                    mainActivity.replaceFragment(MainActivity.POST_READ_FRAGMENT,true,true,newBundle)
                }

                //길게 눌렀을 때
                rowMyPostListBinding.root.setOnCreateContextMenuListener { contextMenu, view, contextMenuInfo ->
                    mainActivity.menuInflater.inflate(R.menu.menu_context_my_post_list, contextMenu)

                    //수정
                    contextMenu[0].setOnMenuItemClickListener {
                        val postIdx = postViewModel.postDataList.value?.get(adapterPosition)?.postIdx
                        val newBundle = Bundle()
                        newBundle.putLong("PostIdx", postIdx!!)
                        mainActivity.replaceFragment(MainActivity.MODIFY_MY_POST_FRAGMENT,true,true,newBundle)
                        false
                    }

                    //삭제
                    contextMenu[1].setOnMenuItemClickListener {
                        //다이얼로그 생성을 위한 객체를 생성한다
                        val builder = MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog)

                        builder.run {
                            setTitle("게시글 삭제")
                            setMessage("게시글을 삭제하시겠습니까?")
                            setPositiveButton("예") { dialogInterface: DialogInterface, i: Int ->
                                val postIdx = postViewModel.postDataList.value?.get(adapterPosition)?.postIdx
                                val imagePath = postViewModel.postDataList.value?.get(absoluteAdapterPosition)?.postImagePath
                                PostRepository.removePost(postIdx!!){
                                    if(imagePath != "null")
                                        PostRepository.removeImages(imagePath.toString()){

                                            val builder = MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog)
                                            builder.run{
                                                setTitle("게시글 삭제")
                                                setMessage("게시글이 삭제되었습니다.")
                                                setPositiveButton("닫기") { dialogInterface: DialogInterface, i: Int ->
                                                }
                                                show()
                                            }
                                            val sharedPreferences = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)
                                            val userId =  sharedPreferences.getString("customerUserId", null).toString()
                                            postViewModel.getMyPostList(userId)
                                        }
                                    else{
                                        val builder = MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog)
                                        builder.run{
                                            setTitle("게시글 삭제")
                                            setMessage("게시글이 삭제되었습니다.")
                                            setPositiveButton("닫기") { dialogInterface: DialogInterface, i: Int ->
                                            }
                                            show()
                                        }
                                        val sharedPreferences = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)
                                        val userId =  sharedPreferences.getString("customerUserId", null).toString()
                                        postViewModel.getMyPostList(userId)
                                    }
                                }
                            }
                            setNegativeButton("아니오") { dialogInterface: DialogInterface, i: Int ->
                            }
                            show()
                        }
                        false
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostListViewHolder {
            val rowPopularboardBinding = RowBoardBinding.inflate(layoutInflater)

            rowPopularboardBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return MyPostListViewHolder(rowPopularboardBinding)
        }

        override fun getItemCount(): Int {
            return postViewModel.postDataList.value?.size!!
        }

        override fun onBindViewHolder(holder: MyPostListViewHolder, position: Int) {
            // holder.imageViewRowMypostListWriterImage =
            holder.textViewRowMypostListTitle.text = postViewModel.postDataList.value?.get(position)?.postSubject
            holder.textViewRowMypostListWriter.text = postViewModel.postDataList.value?.get(position)?.postUserId
            holder.textViewRowMypostListLike.text = postViewModel.postDataList.value?.get(position)?.postLiked.toString()
            holder.textVewRowMypostListWriteDate.text = postViewModel.postDataList.value?.get(position)?.postWriteDate
            holder.textViewRowMypostListComment.text = postViewModel.postDataList.value?.get(position)?.postCommentCount.toString()
        }
    }
}