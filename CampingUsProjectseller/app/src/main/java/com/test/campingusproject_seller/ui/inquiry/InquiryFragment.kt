package com.test.campingusproject_seller.ui.inquiry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentInquiryBinding
import com.test.campingusproject_seller.databinding.RowInquiryBinding
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

//        inquiryViewModel = ViewModelProvider(mainActivity)[InquiryViewModel::class.java]
//        inquiryViewModel.run {
//            inquiryDataList.observe(mainActivity) {
//                fragmentInquiryBinding.recyclerViewInquiry.adapter?.notifyDataSetChanged()
//            }
//        }

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

        return fragmentInquiryBinding.root
    }

    // 문의 목록을 보여주는 리사이클러 뷰 어댑터
    inner class InquiryRecyclerAdapterClass :
        RecyclerView.Adapter<InquiryRecyclerAdapterClass.InquiryViewHolderClass>() {

        inner class InquiryViewHolderClass(rowInquiryBinding: RowInquiryBinding) :
            RecyclerView.ViewHolder(rowInquiryBinding.root) {

            var imageViewInquiryProfile: ImageView
            var textViewInquiryTitle: TextView
            var textViewInquiryContent: TextView
            var textViewInquiryState: TextView
            var textViewInquiryName: TextView

            init {
                imageViewInquiryProfile = rowInquiryBinding.imageViewInquiryProfile
                textViewInquiryTitle = rowInquiryBinding.textViewInquiryTitle
                textViewInquiryContent = rowInquiryBinding.textViewInquiryContent
                textViewInquiryState = rowInquiryBinding.textViewInquiryState
                textViewInquiryName = rowInquiryBinding.textViewInquiryName

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
            return 10
//            return inquiryViewModel.inquiryDataList.value?.size!!
        }

        override fun onBindViewHolder(holder: InquiryViewHolderClass, position: Int) {
            holder.textViewInquiryName.text = "test $position"
//            holder.imageViewInquiryProfile.setImageBitmap()
//            holder.textViewInquiryTitle.text = inquiryViewModel.inquiryDataList.value?.get(position)?.inquiryItemTitle
//            holder.textViewInquiryName.text = inquiryViewModel.inquiryDataList.value?.get(position)?.inquiryUserName
//            holder.textViewInquiryContent.text = inquiryViewModel.inquiryDataList.value?.get(position)?.inquiryContent
//
//            if(inquiryViewModel.inquiryDataList.value?.get(position)?.inquiryResult == true) {
//                holder.textViewInquiryContent.text = "답변 완료"
//            } else {
//                holder.textViewInquiryContent.text = "미답변"
//            }

        }
    }

}