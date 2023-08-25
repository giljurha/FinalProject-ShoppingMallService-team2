package com.test.campingusproject_customer.ui.myprofile

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentMyPostListBinding
import com.test.campingusproject_customer.databinding.RowBoardBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class MyPostListFragment : Fragment() {
    lateinit var fragmentMyPostListBinding: FragmentMyPostListBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentMyPostListBinding = FragmentMyPostListBinding.inflate(layoutInflater)

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
                    mainActivity.replaceFragment(MainActivity.POST_READ_FRAGMENT,true,true,null)
                }

                //길게 눌렀을 때
                rowMyPostListBinding.root.setOnCreateContextMenuListener { contextMenu, view, contextMenuInfo ->
                    mainActivity.menuInflater.inflate(R.menu.menu_context_my_post_list, contextMenu)

                    //수정
                    contextMenu[0].setOnMenuItemClickListener {
                        mainActivity.replaceFragment(MainActivity.MODIFY_MY_POST_FRAGMENT,true,true,null)
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
                                fragmentMyPostListBinding.textViewMyPostListTitle.text = "게시글 삭제됨"
                            }
                            setNegativeButton("아니오") { dialogInterface: DialogInterface, i: Int ->
                                fragmentMyPostListBinding.textViewMyPostListTitle.text = "게시글 삭제안됨"
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
            return 30
        }

        override fun onBindViewHolder(holder: MyPostListViewHolder, position: Int) {
            // holder.imageViewRowMypostListWriterImage =
            holder.textViewRowMypostListTitle.text = "강현구 = 차은우"
            holder.textViewRowMypostListWriter.text = "강현구"
            holder.textViewRowMypostListLike.text = "${100 - position}"
            holder.textVewRowMypostListWriteDate.text = "2023-08-23 14:50"
            holder.textViewRowMypostListComment.text = "${100 - position}"
        }
    }
}