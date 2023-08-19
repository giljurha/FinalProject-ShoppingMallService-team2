package com.test.campingusproject_seller.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentManageProductBinding
import com.test.campingusproject_seller.databinding.RowProductBinding
import com.test.campingusproject_seller.ui.main.MainActivity

class ManageProductFragment : Fragment() {

    lateinit var fragmentManageProductBinding: FragmentManageProductBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentManageProductBinding = FragmentManageProductBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentManageProductBinding.run {

            materialToolbarManageProduct.run {

                title = "제품 관리"

                inflateMenu(R.menu.menu_delete)

                //toolbar 삭제 메뉴 클릭 이벤트
                setOnMenuItemClickListener {
                    if(it.itemId == R.id.menuItemDelete){
                        //삭제 아이템 클릭 처리
                    }
                    false
                }
            }

            recyclerViewManageProduct.run {

                adapter = ManageProductAdapter()
                layoutManager = LinearLayoutManager(mainActivity)

                // recyclerview vertical divider
                //recycler view마다 재사용한다면 main으로 빼도 될듯
                val divider = MaterialDividerItemDecoration(mainActivity, LinearLayoutManager.VERTICAL)
                divider.run {
                    setDividerColorResource(mainActivity, R.color.subColor)
                    dividerInsetStart = 70
                    dividerInsetEnd = 70
                }

                addItemDecoration(divider)
            }

            floatingActionButtonManageProduct.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.REGISTER_PRODUCT_FRAGMENT, true, true, null)
            }
        }

        return fragmentManageProductBinding.root
    }

    inner class ManageProductAdapter : RecyclerView.Adapter<ManageProductAdapter.ManageProductViewHolder>(){
        inner class ManageProductViewHolder(rowProductBinding : RowProductBinding) : RecyclerView.ViewHolder(rowProductBinding.root) {
            val imageViewRowProduct : ImageView
            val textViewRowProductName : TextView
            val textViewRowProductCount : TextView
            val textViewRowProductSellingStatus : TextView
            val checkBoxRowProduct : CheckBox

            init {
                imageViewRowProduct = rowProductBinding.imageViewRowProduct
                textViewRowProductName = rowProductBinding.textViewRowProductName
                textViewRowProductCount = rowProductBinding.textViewRowProductCount
                textViewRowProductSellingStatus = rowProductBinding.textViewRowProductSellingStatus
                checkBoxRowProduct = rowProductBinding.checkBoxRowProduct

                rowProductBinding.root.setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.MODIFY_PRODUCT_FRAGMENT, true, true, null)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageProductViewHolder {
            val rowProductBinding = RowProductBinding.inflate(layoutInflater)

            rowProductBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return ManageProductViewHolder(rowProductBinding)
        }

        override fun getItemCount(): Int {
            return 5
        }

        override fun onBindViewHolder(holder: ManageProductViewHolder, position: Int) {
            holder.textViewRowProductName.text = "바람숭숭 텐트"
            holder.textViewRowProductCount.text = "재고 갯수 : 10개"
            holder.textViewRowProductSellingStatus.text = "판매중"
        }

    }

}