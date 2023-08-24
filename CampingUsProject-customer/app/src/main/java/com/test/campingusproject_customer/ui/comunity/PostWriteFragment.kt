package com.test.campingusproject_customer.ui.comunity

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentPostWriteBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class PostWriteFragment : Fragment() {
    lateinit var fragmentPostWriteBinding: FragmentPostWriteBinding
    lateinit var mainActivity: MainActivity
    lateinit var callback: OnBackPressedCallback

    //게시판 종류
    val boardTypeList = arrayOf(
        "전체게시판","인기게시판", "자유게시판", "캠핑게시판", "유머게시판"
    )
    var boardType = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentPostWriteBinding = FragmentPostWriteBinding.inflate(layoutInflater)

        fragmentPostWriteBinding.run {
            materialToolbarPostWrite.run {
                title = "게시글 작성"

                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    // 키보드가 열려있으면 내림
                    val inputMethodManager = mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                    mainActivity.removeFragment(MainActivity.POST_WRITE_FRAGMENT)
                }
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_item_camera ->{
                            title = "카메라"
                        }
                        R.id.menu_item_album ->{
                            title = "앨범"
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
                        boardType = i + 1
                        text = boardTypeList[i]
                    }
                    builder.setNegativeButton("취소", null)
                    builder.show()
                }
            }
            textViewPostWriteSave.run {
                this.isClickable = true
                setOnClickListener {
                    materialToolbarPostWrite.title = "저장"
                    mainActivity.removeFragment(MainActivity.POST_WRITE_FRAGMENT)
                    mainActivity.replaceFragment(MainActivity.POST_READ_FRAGMENT,true,true,null)
                }
            }
        }

        return fragmentPostWriteBinding.root
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