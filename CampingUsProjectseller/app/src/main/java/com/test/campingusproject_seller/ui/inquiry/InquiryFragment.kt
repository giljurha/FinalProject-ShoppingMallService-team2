package com.test.campingusproject_seller.ui.inquiry

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
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
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentInquiryBinding
import com.test.campingusproject_seller.databinding.RowInquiryBinding
import com.test.campingusproject_seller.repository.InquiryRepository
import com.test.campingusproject_seller.ui.main.MainActivity
import com.test.campingusproject_seller.viewmodel.InquiryViewModel

class InquiryFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentInquiryBinding: FragmentInquiryBinding

    lateinit var inquiryViewModel: InquiryViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mainActivity = activity as MainActivity
        fragmentInquiryBinding = FragmentInquiryBinding.inflate(layoutInflater)

        val pref = mainActivity.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val userId = pref.getString("userId", null)

        inquiryViewModel = ViewModelProvider(mainActivity)[InquiryViewModel::class.java]
        inquiryViewModel.run {
            inquiryDataList.observe(mainActivity) {
                fragmentInquiryBinding.recyclerViewInquiry.adapter?.notifyDataSetChanged()
            }
            inquiryProductList.observe(mainActivity) {
                fragmentInquiryBinding.recyclerViewInquiry.adapter?.notifyDataSetChanged()
            }
        }

        //하단 nav bar 보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE

        fragmentInquiryBinding.run {

            // 뒤로가기 아이콘
            topAppBarInquiry.setNavigationOnClickListener {
                // Handle arrow back icon press
            }

            // 알림 아이콘
            topAppBarInquiry.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.notification_item -> {
                        // Handle notification icon press
                        true
                    }

                    else -> false
                }
            }

            // 리사이클러 뷰
            recyclerViewInquiry.run {
                adapter = InquiryRecyclerAdapterClass()
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(
                    MaterialDividerItemDecoration(
                        context,
                        MaterialDividerItemDecoration.VERTICAL
                    )
                )
            }
        }

        if (userId != null) {
            inquiryViewModel.setInquiryData(userId)
        }

        return fragmentInquiryBinding.root
    }

    // 문의 목록을 보여주는 리사이클러 뷰 어댑터
    inner class InquiryRecyclerAdapterClass :
        RecyclerView.Adapter<InquiryRecyclerAdapterClass.InquiryViewHolderClass>() {

        inner class InquiryViewHolderClass(rowInquiryBinding: RowInquiryBinding) :
            RecyclerView.ViewHolder(rowInquiryBinding.root) {

            var imageViewRowInquiry: ImageView
            var textViewInquiryTitle: TextView
            var textViewInquiryContent: TextView
            var textViewInquiryState: TextView
            var textViewInquiryName: TextView
            val progressBarRow: ProgressBar

            init {
                imageViewRowInquiry = rowInquiryBinding.imageViewRowInquiry
                textViewInquiryTitle = rowInquiryBinding.textViewInquiryTitle
                textViewInquiryContent = rowInquiryBinding.textViewInquiryContent
                textViewInquiryState = rowInquiryBinding.textViewInquiryState
                textViewInquiryName = rowInquiryBinding.textViewInquiryName
                progressBarRow = rowInquiryBinding.progressBarRow

                rowInquiryBinding.root.setOnClickListener {
//                    val inquiryIdx = inquiryViewModel.inquiryDataList.value?.get(adapterPosition)?.inquiryIdx
//                    val newBundle = Bundle()
//                    newBundle.putLong("inquiryIdx", inquiryIdx!!)
//                    mainActivity.replaceFragment(MainActivity.INQUIRY_DETAIL_FRAGMENT, true, false, newBundle)
                    mainActivity.replaceFragment(
                        MainActivity.INQUIRY_DETAIL_FRAGMENT,
                        true,
                        false,
                        null
                    )
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiryViewHolderClass {
            val rowInquiryBinding = RowInquiryBinding.inflate(layoutInflater)
            val inquiryViewHolderClass = InquiryViewHolderClass(rowInquiryBinding)

            rowInquiryBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return inquiryViewHolderClass
        }

        override fun getItemCount(): Int {
//            return 10
            return inquiryViewModel.inquiryDataList.value?.size!!
        }

        override fun onBindViewHolder(holder: InquiryViewHolderClass, position: Int) {


            Log.d(
                "test",
                "${inquiryViewModel.inquiryDataList.value?.get(position)?.inquiryProductName}"
            )
            Log.d(
                "test",
                "${inquiryViewModel.inquiryDataList.value?.get(position)?.inquiryUserName}"
            )

            holder.textViewInquiryTitle.text =
                inquiryViewModel.inquiryDataList.value?.get(position)?.inquiryProductName
            holder.textViewInquiryName.text =
                inquiryViewModel.inquiryDataList.value?.get(position)?.inquiryUserName.toString()
            holder.textViewInquiryContent.text =
                inquiryViewModel.inquiryDataList.value?.get(position)?.inquiryContent
//
            if (inquiryViewModel.inquiryDataList.value?.get(position)?.inquiryResult == true) {
                holder.textViewInquiryState.text = "답변 완료"
            } else {
                holder.textViewInquiryState.text = "미답변"
            }

            // 상품에 등록된 이미지 경로로 첫 번째 이미지만 불러와 표시
            InquiryRepository.getProductFirstImage(
                inquiryViewModel.inquiryDataList.value?.get(
                    position
                )?.inquiryImage!!
            ) { uri ->
                // 글라이드 라이브러리로 이미지 표시
                // 이미지 로딩 완료되거나 실패하기 전까지 프로그래스바 활성화
                Glide.with(mainActivity).load(uri.result)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            holder.progressBarRow.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            holder.progressBarRow.visibility = View.GONE
                            return false
                        }

                    })
                    .override(200, 200)
                    .into(holder.imageViewRowInquiry)
            }

        }
    }

}