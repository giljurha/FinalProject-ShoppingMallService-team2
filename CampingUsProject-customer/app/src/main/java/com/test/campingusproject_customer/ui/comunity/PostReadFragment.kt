package com.test.campingusproject_customer.ui.comunity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentPostReadBinding
import com.test.campingusproject_customer.databinding.RowBoardBinding
import com.test.campingusproject_customer.databinding.RowPostReadBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class PostReadFragment : Fragment() {
    lateinit var fragmentPostReadBinding: FragmentPostReadBinding
    lateinit var mainActivity: MainActivity
    lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentPostReadBinding = FragmentPostReadBinding.inflate(layoutInflater)

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
            textViewPostReadUserName.text = "강현구"
            textViewPostReadWriteDate.text = "2023-08-23 14:50"
            textViewPostReadPostTitle.text = "나 강현구 차은우보다 잘생김 ㅇㅈ?"
            textViewPostReadPostContents.text = "솔직히 차은우 별거 있냐? 내가 훨씬 낫지ㅋㅋ"
            textViewPostReadPostLike.text = "0"
            textViewPostReadPostComent.text = "10"

            textViewPostReadCommentsSave.run {
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
        }


        return fragmentPostReadBinding.root
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