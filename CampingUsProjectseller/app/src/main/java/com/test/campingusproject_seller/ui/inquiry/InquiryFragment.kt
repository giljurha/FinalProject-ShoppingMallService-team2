package com.test.campingusproject_seller.ui.inquiry

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
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentInquiryBinding
import com.test.campingusproject_seller.databinding.RowInquiryBinding
import com.test.campingusproject_seller.ui.main.MainActivity

class InquiryFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentInquiryBinding: FragmentInquiryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mainActivity = activity as MainActivity
        fragmentInquiryBinding = FragmentInquiryBinding.inflate(layoutInflater)

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
        }

        override fun onBindViewHolder(holder: InquiryViewHolderClass, position: Int) {
            holder.textViewInquiryName.text = "test $position"
        }
    }

}