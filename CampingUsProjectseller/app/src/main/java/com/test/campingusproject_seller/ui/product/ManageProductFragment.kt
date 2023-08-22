package com.test.campingusproject_seller.ui.product

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentManageProductBinding
import com.test.campingusproject_seller.databinding.RowProductItemBinding
import com.test.campingusproject_seller.repository.ProductRepository
import com.test.campingusproject_seller.ui.main.MainActivity
import com.test.campingusproject_seller.viewmodel.ProductViewModel

class ManageProductFragment : Fragment() {

    lateinit var fragmentManageProductBinding: FragmentManageProductBinding
    lateinit var mainActivity: MainActivity

    lateinit var productViewModel : ProductViewModel

    var productCheckedList = mutableListOf<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentManageProductBinding = FragmentManageProductBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        //하단 nav bar 보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE

        //제품 뷰 모델 감시자 설정
        productViewModel = ViewModelProvider(mainActivity)[ProductViewModel::class.java]
        productViewModel.run {
            productList.observe(mainActivity){
                //recycler view 갱신
                fragmentManageProductBinding.recyclerViewManageProduct.adapter?.notifyDataSetChanged()

                productCheckedList.clear()
                for(i in 0 until productList.value?.size!!){
                    productCheckedList.add(false)
                }
            }
        }

        fragmentManageProductBinding.run {

            //툴바
            materialToolbarManageProduct.run {

                title = "제품 관리"

                inflateMenu(R.menu.menu_delete)

                //삭제 아이콘 클릭 이벤트
                setOnMenuItemClickListener {
                    if(it.itemId == R.id.menuItemDelete){
                        for(idx in productCheckedList.size-1 downTo 0){
                            if(productCheckedList[idx]){
                                //Log.e("deleteList", productViewModel.productList.value?.get(idx)?.productName!!)
                                val product = productViewModel.productList.value?.get(idx)

                                val productId = product?.productId!!
                                val productImage = product?.productImage!!

                                ProductRepository.removeProduct(productId){
                                    ProductRepository.removeImages(productImage){
                                        productViewModel.getAllProductData(ProductRepository.getSellerId())
                                        Snackbar.make(fragmentManageProductBinding.root, "삭제되었습니다.", Snackbar.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                    false
                }
            }

            //recycler view
            recyclerViewManageProduct.run {
                //상품 뷰 모델을 통해 등록된 모든 제품 목록 불러오기
                productViewModel.getAllProductData(ProductRepository.getSellerId())

                adapter = ManageProductAdapter()
                layoutManager = LinearLayoutManager(mainActivity)

                // recyclerview vertical divider
                //recycler view마다 재사용한다면 main으로 빼도 될듯
                val divider = MaterialDividerItemDecoration(mainActivity, LinearLayoutManager.VERTICAL)
                divider.run {
                    setDividerColorResource(mainActivity, R.color.subColor)
                    dividerInsetStart = 30
                    dividerInsetEnd = 30
                }

                addItemDecoration(divider)
            }

            //floating action button 클릭 이벤트
            floatingActionButtonManageProduct.setOnClickListener {
                //제품 등록 화면으로 전환
                mainActivity.replaceFragment(MainActivity.REGISTER_PRODUCT_FRAGMENT, true, true, null)
            }

            checkBoxManageProductSelectAll.setOnCheckedChangeListener { compoundButton: CompoundButton, checked: Boolean ->
                for(idx in 0 until recyclerViewManageProduct.childCount){
                    val itemView = recyclerViewManageProduct.getChildAt(idx)
                    val itemCheckBox = itemView.findViewById<CheckBox>(R.id.checkBoxRowProduct)

                    itemCheckBox.isChecked = checked
                }
            }

        }

        return fragmentManageProductBinding.root
    }

    //상품 어댑터
    inner class ManageProductAdapter : RecyclerView.Adapter<ManageProductAdapter.ManageProductViewHolder>(){
        inner class ManageProductViewHolder(rowProductItemBinding: RowProductItemBinding) : RecyclerView.ViewHolder(rowProductItemBinding.root) {
            val progressBarRow : ProgressBar
            val imageViewRowProduct : ImageView
            val textViewRowProductName : TextView
            val textViewRowProductCount : TextView
            val textViewRowProductSellingStatus : TextView
            val checkBoxRowProduct : CheckBox

            init {
                progressBarRow = rowProductItemBinding.progressBarRow
                imageViewRowProduct = rowProductItemBinding.imageViewRowProduct
                textViewRowProductName = rowProductItemBinding.textViewRowProductName
                textViewRowProductCount = rowProductItemBinding.textViewRowProductCount
                textViewRowProductSellingStatus = rowProductItemBinding.textViewRowProductSellingStatus
                checkBoxRowProduct = rowProductItemBinding.checkBoxRowProduct

                //아이템 클릭 시 수정 화면으로 전환
                rowProductItemBinding.root.setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.MODIFY_PRODUCT_FRAGMENT, true, true, null)
                }

                checkBoxRowProduct.setOnCheckedChangeListener { compoundButton, b ->
                    setParentCheckBoxState()
                    if(b){
                        productCheckedList[adapterPosition] = b
                    }
                    else{
                        productCheckedList[adapterPosition] = b
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageProductViewHolder {
            val rowProductItemBinding = RowProductItemBinding.inflate(layoutInflater)

            rowProductItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return ManageProductViewHolder(rowProductItemBinding)
        }

        override fun getItemCount(): Int {
            return productViewModel.productList.value?.size!!
        }

        override fun onBindViewHolder(holder: ManageProductViewHolder, position: Int) {
            holder.textViewRowProductName.text = productViewModel.productList.value?.get(position)?.productName
            holder.textViewRowProductCount.text = "재고 : ${productViewModel.productList.value?.get(position)?.productCount}"
            holder.textViewRowProductSellingStatus.text = printSellingStatus(productViewModel.productList.value?.get(position)?.productSellingStatus!!)

            //상품에 등록된 이미지 경로로 첫 번째 이미지만 불러와 표시
            ProductRepository.getProductFirstImage(productViewModel.productList.value?.get(position)?.productImage!!){ uri->
                //글라이드 라이브러리로 이미지 표시
                //이미지 로딩 완료되거나 실패하기 전까지 프로그래스바 활성화
                Glide.with(mainActivity).load(uri.result)
                    .listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            holder.progressBarRow.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            holder.progressBarRow.visibility = View.GONE
                            return false
                        }

                    })
                    .override(200, 200)
                    .into(holder.imageViewRowProduct)
            }
        }
    }

    //판매 상태 출력 함수
    fun printSellingStatus(sellingStatus : Boolean) : String{
        return if(sellingStatus) "판매중" else "매진"
    }

    fun setParentCheckBoxState(){
        fragmentManageProductBinding.run{

            // 체크박스의 개수를 구한다.
            val checkBoxCount = productViewModel.productList.value?.size!!
            // 체크되어 있는 체크박스의 개수
            var checkedCount = 0

            // 체크 되어 있는 체크박스의 개수를 구한다.
            for(idx in 0 until checkBoxCount){
                val itemView = recyclerViewManageProduct.getChildAt(idx)
                val itemCheckBox = itemView.findViewById<CheckBox>(R.id.checkBoxRowProduct)

                if(itemCheckBox.isChecked){
                    checkedCount++
                }
            }

            // 만약 체크되어 있는 것이 없다면
            if(checkedCount == 0){
                checkBoxManageProductSelectAll.checkedState = MaterialCheckBox.STATE_UNCHECKED
            }
            // 모두 체크되어 있다면
            else if(checkedCount == checkBoxCount){
                checkBoxManageProductSelectAll.checkedState = MaterialCheckBox.STATE_CHECKED
            }
            // 일부만 체크되어 있다면
            else {
                checkBoxManageProductSelectAll.checkedState = MaterialCheckBox.STATE_INDETERMINATE
            }
        }
    }

}