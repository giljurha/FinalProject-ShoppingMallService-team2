package com.test.campingusproject_customer.ui.comunity

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentComunityBinding
import com.test.campingusproject_customer.databinding.HeaderNavigationBinding
import com.test.campingusproject_customer.databinding.RowBoardBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class ComunityFragment : Fragment() {
    lateinit var fragmentComunityBinding: FragmentComunityBinding
    lateinit var mainActivity: MainActivity
    lateinit var callback: OnBackPressedCallback

    //네비게이션 드로어에 나오는 아이템 메뉴
    val itemList = arrayOf(
        R.id.item_coumnity_all,
        R.id.item_coumnity_popular,
        R.id.item_coumnity_free,
        R.id.item_coumnity_camping,
        R.id.item_coumnity_Humor
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentComunityBinding = FragmentComunityBinding.inflate(layoutInflater)

    fragmentComunityBinding.run {
        materialToolbarComunityFragment.run {
            title = "전체 게시판"
            setNavigationIcon(R.drawable.menu_24px)
            setNavigationOnClickListener {
                // 키보드가 열려있으면 내림
                val inputMethodManager = mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)

                //검색창에 있는 포커스 지우기
                textInputEditTextComunitySearch.clearFocus()

                //네비게이션 드로어 오픈
                drawerLayoutComunity.open()
            }
        }

        floatingActionButtonComunityAddBoard.run {
            // 플로팅 액션 버튼 아이콘의 색상을 변경
            val fab: FloatingActionButton = findViewById(R.id.floatingActionButtonComunityAddBoard)
            fab.setColorFilter(ContextCompat.getColor(mainActivity, R.color.highLightColor), PorterDuff.Mode.SRC_IN)

            setOnClickListener {
                mainActivity.replaceFragment(MainActivity.POST_WRITE_FRAGMENT,true,false,null)
            }
        }

        navigationViewComunity.run {
            // 헤더설정
            val headerBinding = HeaderNavigationBinding.inflate(inflater)
            headerBinding.textViewHeadrerName.text = "청량 미남 강현구"
            addHeaderView(headerBinding.root)

            //커뮤니티 들어왔을 때는 전체 게시판을 보여줌
            menu.findItem(R.id.item_coumnity_all).setIcon(R.drawable.circle_20px)

            //아이템 클릭 시
            setNavigationItemSelectedListener {
                when(it.itemId){
                    //전체 게시판
                    R.id.item_coumnity_all ->{
                        materialToolbarComunityFragment.title = "전체 게시판"
                        setNavigationIcon(it)
                        drawerLayoutComunity.close()
                    }
                    //인기 게시판
                    R.id.item_coumnity_popular ->{
                        materialToolbarComunityFragment.title = "인기 게시판"
                        setNavigationIcon(it)
                        drawerLayoutComunity.close()
                    }
                    //자유 게시판
                    R.id.item_coumnity_free ->{
                        materialToolbarComunityFragment.title = "자유 게시판"
                        setNavigationIcon(it)
                        drawerLayoutComunity.close()
                    }
                    //캠핑 게시판
                    R.id.item_coumnity_camping ->{
                        materialToolbarComunityFragment.title = "캠핑 게시판"
                        setNavigationIcon(it)
                        drawerLayoutComunity.close()
                    }
                    //유머 게시판
                    R.id.item_coumnity_Humor ->{
                        materialToolbarComunityFragment.title = "유머 게시판"
                        setNavigationIcon(it)
                        drawerLayoutComunity.close()
                    }
                }
                true
            }
        }
        recyclerViewComunity.run {
            adapter = ComunitydAdapter()
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

        return fragmentComunityBinding.root
    }

    //게시판 리싸이클러뷰 어댑터
    inner class ComunitydAdapter : RecyclerView.Adapter<ComunitydAdapter.ComunityViewHolder>(){
        inner class ComunityViewHolder(rowComunityBinding: RowBoardBinding) : RecyclerView.ViewHolder(rowComunityBinding.root) {
            val imageViewRowBoardWriterImage : ImageView // 작성자 프로필 사진
            val textViewRowBoardTitle : TextView // 게시글 제목
            val textViewRowBoardWriter : TextView // 게시글 작성자
            val textViewRowBoardLike : TextView // 좋아요 수
            val textVewRowBoardWriteDate : TextView // 글 작성 시간
            val textViewRowBoardComment : TextView // 댓글 수

            init {
                imageViewRowBoardWriterImage = rowComunityBinding.imageViewRowBoardWriterImage
                textViewRowBoardTitle = rowComunityBinding.textViewRowBoardTitle
                textViewRowBoardWriter = rowComunityBinding.textViewRowBoardWriter
                textViewRowBoardLike = rowComunityBinding.textViewRowBoardLike
                textVewRowBoardWriteDate = rowComunityBinding.textViewRowBoardWriteDate
                textViewRowBoardComment = rowComunityBinding.textViewRowBoardComment

                rowComunityBinding.root.setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.POST_READ_FRAGMENT,true,false,null)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComunityViewHolder {
            val rowPopularboardBinding = RowBoardBinding.inflate(layoutInflater)

            rowPopularboardBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return ComunityViewHolder(rowPopularboardBinding)
        }

        override fun getItemCount(): Int {
            return 30
        }

        override fun onBindViewHolder(holder: ComunityViewHolder, position: Int) {
            // holder.imageViewRowPopularBoardWriterImage =
            holder.textViewRowBoardTitle.text = "강현구 = 차은우"
            holder.textViewRowBoardWriter.text = "강현구"
            holder.textViewRowBoardLike.text = "${100 - position}"
            holder.textVewRowBoardWriteDate.text = "2023-08-23 14:50"
            holder.textViewRowBoardComment.text = "${100 - position}"
        }
    }

    //뒤로가기 버튼 눌렀을 때 동작할 코드 onDetech까지
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.removeFragment(MainActivity.COMUNITY_FRAGMENT)
                mainActivity.activityMainBinding.bottomNavigationViewMain.selectedItemId = R.id.menuItemHome
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    //선택된 카테고리에 아이콘 추가
    fun setNavigationIcon(menuItem: MenuItem){
        for(item in itemList){
            if(item == menuItem.itemId){
                menuItem.setIcon(R.drawable.circle_20px)
            }
            else{
                fragmentComunityBinding.navigationViewComunity.menu.findItem(item).setIcon(null)
            }
        }
    }
}
