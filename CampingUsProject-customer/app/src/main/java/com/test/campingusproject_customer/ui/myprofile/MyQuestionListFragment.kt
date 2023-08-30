package com.test.campingusproject_customer.ui.myprofile

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentMyQuestionListBinding
import com.test.campingusproject_customer.databinding.RowMyQuestionsBinding
import com.test.campingusproject_customer.dataclassmodel.InquiryModel
import com.test.campingusproject_customer.repository.InquiryRepository
import com.test.campingusproject_customer.repository.ProductRepository
import com.test.campingusproject_customer.ui.main.MainActivity
import com.test.campingusproject_customer.viewmodel.InquiryViewModel

class MyQuestionListFragment : Fragment() {

    lateinit var fragmentMyQuestionListBinding: FragmentMyQuestionListBinding
    lateinit var mainActivity: MainActivity

    lateinit var inquiryViewModel: InquiryViewModel

    var questionList = mutableListOf<InquiryModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentMyQuestionListBinding = FragmentMyQuestionListBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        inquiryViewModel = ViewModelProvider(mainActivity)[InquiryViewModel::class.java]
        inquiryViewModel.inquiryList.observe(mainActivity){
            fragmentMyQuestionListBinding.recyclerViewMyQuestionList.adapter?.notifyDataSetChanged()
        }

        val sharedPreferences = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)
        val userId =  sharedPreferences.getString("customerUserId", null).toString()

        inquiryViewModel.getQuestionList(userId)

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
                val textViewRowQuestionDate : TextView

                init {
                    imageviewRowQuestionProductImage = rowMyQuestionsBinding.imageViewRowQuestionProductImage
                    textViewRowQuestionTitle = rowMyQuestionsBinding.textViewRowQuestionTitle
                    textViewRowQuestionContent = rowMyQuestionsBinding.textViewRowQuestionContent
                    textViewRowQuestionAnswerStatus = rowMyQuestionsBinding.textViewRowQuestionAnswerStatus
                    textViewRowQuestionDate = rowMyQuestionsBinding.textViewRowQuestionDate

                    //recycler view 아이템 클릭시 상세 페이지로 전환
                    rowMyQuestionsBinding.root.setOnClickListener {
                        val newBundle = Bundle()
                        newBundle.putString("inquiryProduct", inquiryViewModel.inquiryList.value?.get(adapterPosition)?.inquiryProductName)
                        newBundle.putString("inquiryContent", inquiryViewModel.inquiryList.value?.get(adapterPosition)?.inquiryContent)
                        newBundle.putString("inquiryAnswer", inquiryViewModel.inquiryList.value?.get(adapterPosition)?.inquiryAnswer)
                        newBundle.putString("inquiryDate", inquiryViewModel.inquiryList.value?.get(adapterPosition)?.inquiryWriteDate)

                        Log.d("test", "bundle ${inquiryViewModel.inquiryList.value?.get(adapterPosition)?.inquiryAnswer}")

                        mainActivity.replaceFragment(MainActivity.MY_QUESTION_DETAIL_FRAGMENT, true, true, newBundle)
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
            return inquiryViewModel.inquiryList.value?.size!!
        }

        override fun onBindViewHolder(holder: MyQuestionViewHolder, position: Int) {
            holder.textViewRowQuestionTitle.text = inquiryViewModel.inquiryList.value?.get(position)?.inquiryProductName
            holder.textViewRowQuestionContent.text = inquiryViewModel.inquiryList.value?.get(position)?.inquiryContent
            holder.textViewRowQuestionDate.text = inquiryViewModel.inquiryList.value?.get(position)?.inquiryWriteDate
            holder.textViewRowQuestionAnswerStatus.text = if(inquiryViewModel.inquiryList.value?.get(position)?.inquiryResult!!) "답변 완료" else "미답변"

            //이미지
            ProductRepository.getProductFirstImage(inquiryViewModel.inquiryList.value?.get(position)?.inquiryImage!!){
                Glide.with(mainActivity).load(it.result)
                    .override(300, 300)
                    .into(holder.imageviewRowQuestionProductImage)
            }
        }
    }

}