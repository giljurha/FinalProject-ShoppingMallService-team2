package com.test.campingusproject_customer.ui.campsite

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentCampsiteBinding
import com.test.campingusproject_customer.databinding.RowPostReadBinding
import com.test.campingusproject_customer.databinding.RowSearchedCampsiteBinding
import com.test.campingusproject_customer.ui.comunity.PostReadFragment
import com.test.campingusproject_customer.ui.main.MainActivity
import org.w3c.dom.Text

class CampsiteFragment : Fragment(), OnMapReadyCallback {
    lateinit var fragmentCampsiteBinding: FragmentCampsiteBinding
    lateinit var mainActivity: MainActivity
    lateinit var callback: OnBackPressedCallback
    lateinit var naverMap: NaverMap
    lateinit var locationSource: FusedLocationSource

    //권한 코드
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // 권한이 허용되었으면
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
            } else {
                // 권한이 거부 되었으면
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentCampsiteBinding = FragmentCampsiteBinding.inflate(layoutInflater)

        mainActivity = activity as MainActivity

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        //비동기로 네이버 객체를 얻어온다
        val fm = mainActivity.supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        //OnMapReadyCallback등록
        mapFragment.getMapAsync(this)

        fragmentCampsiteBinding.run {
            //제휴 캠핑장 플로팅 바 클릭시
            buttonContractCampsite.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.CONTRACT_CAMPSITE_FRAGMENT, false, false, null)
            }
            //서치바 리사이클러뷰 설정
            recyclerViewCampListResult.run {
                adapter=SearchedCampsiteAdapter()
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



        return fragmentCampsiteBinding.root
    }

    //지도객체가 callback되면 호출되는 메서드
    override fun onMapReady(p0: NaverMap) {
        naverMap = p0
        //내 위치 맵에 설정
        naverMap.locationSource = locationSource
        //확대 축소 버튼 안보이게
        naverMap.uiSettings.isZoomControlEnabled = false
        //나침반 안보이게 삭제
        naverMap.uiSettings.isCompassEnabled = false
        //내 위치 버튼 위치 커스텀
        val locationButton = fragmentCampsiteBinding.buttonMyLocation
        locationButton.map = naverMap

        //권한 확인 및 승인되지 않은 경우 요청
        if (ContextCompat.checkSelfPermission(
                mainActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

        naverMap.addOnLocationChangeListener { location ->
            naverMap.locationTrackingMode
        }
    }

    //뒤로가기 버튼 눌렀을 때 동작할 코드 onDetech까지
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.removeFragment(MainActivity.CAMPSITE_FRAGMENT)
                mainActivity.activityMainBinding.bottomNavigationViewMain.selectedItemId =
                    R.id.menuItemHome
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
        locationSource.deactivate()

        //프래그먼트 종료시 내부 맵 프래그먼트의 getMapAsync도 종료시키기 위해 맵 프래그먼트 수동 종료
        val mapFragment =
            mainActivity.supportFragmentManager.findFragmentById(R.id.map) as MapFragment?
        mapFragment?.let {
            mainActivity.supportFragmentManager.beginTransaction().remove(it).commit()
        }

    }

    inner class SearchedCampsiteAdapter : RecyclerView.Adapter<SearchedCampsiteAdapter.SearchedCampsiteViewHolder>(){
        inner class SearchedCampsiteViewHolder(rowSearchedCampsiteBinding: RowSearchedCampsiteBinding) : RecyclerView.ViewHolder(rowSearchedCampsiteBinding.root) {
            val imageViewCampsite:ImageView
            val textViewCampsiteName:TextView
            val textViewCampsiteAdress:TextView
            init {
                imageViewCampsite=rowSearchedCampsiteBinding.imageViewSearchedCampsite
                textViewCampsiteName=rowSearchedCampsiteBinding.textViewSearchedCampsiteName
                textViewCampsiteAdress=rowSearchedCampsiteBinding.textViewSearchedCampsiteAdress

            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): SearchedCampsiteViewHolder {
            val rowSearchedCampsiteBinding=RowSearchedCampsiteBinding.inflate(layoutInflater)
            val searchedCampsiteViewHolder=SearchedCampsiteViewHolder(rowSearchedCampsiteBinding)

            rowSearchedCampsiteBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return searchedCampsiteViewHolder
        }

        override fun getItemCount(): Int {
            return 33
        }

        override fun onBindViewHolder(holder: SearchedCampsiteViewHolder, position: Int) {
            holder.imageViewCampsite.setImageResource(R.drawable.camping_24px)
            holder.textViewCampsiteName.text="강현구의 캠핑장"
            holder.textViewCampsiteAdress.text="인천 서구 명가골로 34번길 7-2 1층"
        }


    }
}