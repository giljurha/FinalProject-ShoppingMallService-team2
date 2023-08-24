package com.test.campingusproject_customer.ui.myprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentMyQuestionListBinding
import com.test.campingusproject_customer.databinding.RowMyQuestionsBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class MyQuestionListFragment : Fragment() {

    lateinit var fragmentMyQuestionListBinding: FragmentMyQuestionListBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentMyQuestionListBinding = FragmentMyQuestionListBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentMyQuestionListBinding.run{
            materialToolbarMyQuestionList.run{
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MY_QUESTION_LIST_FRAGMENT)
                }
            }

            recyclerViewMyQuestionList.run {
                adapter = MyQuestionAdapter()
                layoutManager = LinearLayoutManager(mainActivity)

                val divider = MaterialDividerItemDecoration(mainActivity, LinearLayoutManager.VERTICAL)
                divider.run {
                    setDividerColorResource(mainActivity, R.color.subColor)
                    dividerInsetStart = 30
                    dividerInsetEnd = 30
                }

                addItemDecoration(divider)
            }
        }

        return fragmentMyQuestionListBinding.root
    }

    inner class MyQuestionAdapter: RecyclerView.Adapter<MyQuestionAdapter.MyQuestionViewHolder>(){

        inner class MyQuestionViewHolder(rowMyQuestionsBinding: RowMyQuestionsBinding):
            RecyclerView.ViewHolder(rowMyQuestionsBinding.root){

                val imageviewRowQuestionProductImage : ImageView
                val textViewRowQuestionTitle : TextView
                val textViewRowQuestionContent : TextView
                val textViewRowQuestionAnswerStatus : TextView
                val textViewRowQuestionUserId : TextView

                init {
                    imageviewRowQuestionProductImage = rowMyQuestionsBinding.imageViewRowQuestionProductImage
                    textViewRowQuestionTitle = rowMyQuestionsBinding.textViewRowQuestionTitle
                    textViewRowQuestionContent = rowMyQuestionsBinding.textViewRowQuestionContent
                    textViewRowQuestionAnswerStatus = rowMyQuestionsBinding.textViewRowQuestionAnswerStatus
                    textViewRowQuestionUserId = rowMyQuestionsBinding.textViewRowQuestionUserId

                    //recycler view 아이템 클릭시 상세 페이지로 전환
                    rowMyQuestionsBinding.root.setOnClickListener {
                        mainActivity.replaceFragment(MainActivity.MY_QUESTION_DETAIL_FRAGMENT, true, true, null)
                    }

                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyQuestionViewHolder {
            val rowMyQuestionsBinding = RowMyQuestionsBinding.inflate(layoutInflater)

            rowMyQuestionsBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return MyQuestionViewHolder(rowMyQuestionsBinding)
        }

        override fun getItemCount(): Int {
            return 2
        }

        override fun onBindViewHolder(holder: MyQuestionViewHolder, position: Int) {
            holder.textViewRowQuestionTitle.text = "현구님의 바람숭숭 텐트"
            holder.textViewRowQuestionContent.text = "바람이 너무 잘통해요 ㅜㅜㅜㅜㅜ"
            holder.textViewRowQuestionUserId.text = "차은우"
            holder.textViewRowQuestionAnswerStatus.text = "답변 완료"
        }
    }

}