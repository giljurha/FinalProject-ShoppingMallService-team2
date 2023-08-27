package com.test.campingusproject_customer.ui.campsite

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.animate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.search.SearchView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.CameraUpdate.zoomTo
import com.naver.maps.map.CameraUpdateParams
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.DialogMarkerBinding
import com.test.campingusproject_customer.databinding.FragmentCampsiteBinding
import com.test.campingusproject_customer.databinding.RowSearchedCampsiteBinding
import com.test.campingusproject_customer.dataclassmodel.CampsiteInfo
import com.test.campingusproject_customer.dataclassmodel.Response
import com.test.campingusproject_customer.ui.main.MainActivity
import com.test.campingusproject_customer.viewmodel.CampsiteViewModel

class CampsiteFragment : Fragment(), OnMapReadyCallback, Overlay.OnClickListener {
    lateinit var fragmentCampsiteBinding: FragmentCampsiteBinding
    lateinit var mainActivity: MainActivity
    lateinit var callback: OnBackPressedCallback
    lateinit var naverMap: NaverMap
    lateinit var locationSource: FusedLocationSource
    lateinit var campsiteViewModel: CampsiteViewModel
    lateinit var myLatitude: String
    lateinit var myLongitude: String

    //캠핑장 데이터 리스트
    var campList = mutableListOf<CampsiteInfo>()

    //불러온 데이터의 정보
    lateinit var dataState: Response

    //현재 내가 갖고있는 데이터 갯수
    var mydataNum = 0

    //서버에 있는 데이터 총 갯수
    var cloudDataNum: Int = -1

    //서버에 명령할 데이터 페이지번호
    var page: Int = 1
    var markerList = mutableListOf<Marker>()
    var searchTask = false

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

        campsiteViewModel = ViewModelProvider(mainActivity)[CampsiteViewModel::class.java]
        campsiteViewModel.run {
            campSites.observe(mainActivity) {
                try {
                    campList.addAll(it.item)
                    Log.d("testt", campList.toString())
                    if (campList.size == cloudDataNum) {
                        val adapter =
                            fragmentCampsiteBinding.recyclerViewCampListResult.adapter as SearchedCampsiteAdapter
                        adapter.notifyDataSetChanged()
                        //통신이 끝났으므로 변수들 복구
                        mydataNum = 0
                        cloudDataNum = -1
                        page = 1
                        searchTask = false
                        markerList.clear()
                        //마커 찍기
                        addMaker()
                    }
                } catch (e: java.lang.Exception) {
                    Toast.makeText(mainActivity, "텍스트를 입력하세요!", Toast.LENGTH_SHORT).show()
                    Log.d("testt", "에러 방지중2")
                }
            }
            dataInfo.observe(mainActivity) {
                try {
                    dataState = it.response

                    //페이지를 넘길지 말지 구분하기 위해 구하는 검색된 데이터의 총 갯수
                    cloudDataNum = dataState.body.totalCount

                    //검색된 데이터들을 모두 가져오기 위해서 통신 할때마다 가져온 데이터의 갯수를 누적해서 더한다
                    mydataNum += dataState.body.numOfRows

                    //만약 서버데이터가 내가 갖고있는 데이터보다 크면 반복
                    Log.d("testt", "검색된 데이터 총 갯수:${cloudDataNum}")
                    Log.d("testt", "지금 내가 갖고있는 데이터 수:${mydataNum}")

                    page += 1
                    if (cloudDataNum != mydataNum) {
                        //이 작업이 검색 작업이 아니라면
                        if (!searchTask) {
                            //서버에서 데이터 불러오기
                            campsiteViewModel.fetchCampsites(page, myLatitude, myLongitude)
                            Log.d("testt", "if문 동작 page:${page}")
                            Thread.sleep(3000) // 5초 딜레이}
                        } else {//이작업이 검색작업이라면
                            campsiteViewModel.fetchSearchedCampsites(page, "가평")
                        }
                    }

                } catch (e: Exception) {
                    Log.d("testt", "에러방지중")
                }
            }
            campsiteLoadError.observe(mainActivity) {
                try {
                    val adapter =
                        fragmentCampsiteBinding.recyclerViewCampListResult.adapter as SearchedCampsiteAdapter
                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    //앱 터지는 것 받지
                }
            }

        }


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
                mainActivity.replaceFragment(
                    MainActivity.CONTRACT_CAMPSITE_FRAGMENT,
                    false,
                    false,
                    null
                )
            }

            //서치바 엔터 클릭시
            searchViewCampList.run {
                hint = "검색할 캠핑장 지역을 입력해주세요"
                addTransitionListener { searchView, previousState, newState ->
                    if (newState == SearchView.TransitionState.SHOWING) {
                        Log.d("testt", "열림")
                    } else if (newState == SearchView.TransitionState.HIDING) {
                        Log.d("testt", "닫힘")
                        this.backgroundTintMode
                        Log.d("testt", campList.size.toString())
                    }
                    editText.setOnEditorActionListener { v, actionId, event ->
                        searchTask = true
                        deleteMarker()
                        campList.clear()
                        campsiteViewModel.fetchSearchedCampsites(1, text.toString())
                        true
                    }
                }
            }

            //서치바 리사이클러뷰 설정
            recyclerViewCampListResult.run {
                adapter = SearchedCampsiteAdapter()
                layoutManager = LinearLayoutManager(mainActivity)

                //구분선 추가
                val divider =
                    MaterialDividerItemDecoration(mainActivity, LinearLayoutManager.VERTICAL)
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
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(mainActivity)
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

            //네이버 맵에 위치가 찍히면 최초로 나의 위도 경도를 얻어온다.
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                //location nullable 대응
                if (location != null) {
                    myLatitude = location.latitude.toString()
                    myLongitude = location.longitude.toString()
                    //데이터 1차 호출
                    campsiteViewModel.fetchCampsites(page, myLatitude, myLongitude)
                }
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        naverMap.addOnLocationChangeListener { location ->
            naverMap.locationTrackingMode
        }
    }

    //마커 추가 메서드
    fun addMaker() {
        for (info in campList) {
            val marker = Marker()
            marker.position = LatLng(info.위도.toDouble(), info.경도.toDouble())
            marker.tag = info
            marker.map = naverMap
            marker.zIndex = 1
            markerList.add(marker)
            marker.setOnClickListener(this)
            Log.d("testt", "마커 추가 중")
        }
    }

    //마커들을 전부 삭제하는 메서드
    fun deleteMarker() {
        for (marker in markerList) {
            marker.map = null
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

                campsiteViewModel.resetData()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
        //프래그 먼트 종료 시 위치 업데이트 중단
        locationSource.deactivate()
        //프래그먼트 종료 시 뷰모델 데이터 초기화
        campsiteViewModel.resetData()

        //프래그먼트 종료시 내부 맵 프래그먼트의 getMapAsync도 종료시키기 위해 맵 프래그먼트 수동 종료
        val mapFragment =
            mainActivity.supportFragmentManager.findFragmentById(R.id.map) as MapFragment?
        mapFragment?.let {
            mainActivity.supportFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    inner class SearchedCampsiteAdapter :
        RecyclerView.Adapter<SearchedCampsiteAdapter.SearchedCampsiteViewHolder>() {
        inner class SearchedCampsiteViewHolder(rowSearchedCampsiteBinding: RowSearchedCampsiteBinding) :
            RecyclerView.ViewHolder(rowSearchedCampsiteBinding.root) {
            val imageViewCampsite: ImageView
            val textViewCampsiteName: TextView
            val textViewCampsiteAdress: TextView

            init {
                imageViewCampsite = rowSearchedCampsiteBinding.imageViewSearchedCampsite
                textViewCampsiteName = rowSearchedCampsiteBinding.textViewSearchedCampsiteName
                textViewCampsiteAdress = rowSearchedCampsiteBinding.textViewSearchedCampsiteAdress

                rowSearchedCampsiteBinding.root.setOnClickListener {
                    val searchView = fragmentCampsiteBinding.searchViewCampList

                    Log.d("testt", "클릭:")
                    //마커 색깔 변경
                    markerList[adapterPosition].icon = MarkerIcons.BLACK
                    markerList[adapterPosition].iconTintColor = Color.RED
                    markerList[adapterPosition].zIndex = 2
                    val tagInfo = markerList[adapterPosition].tag as CampsiteInfo
                    //카메라 이동
                    val cameraUpdate =
                        CameraUpdate.scrollTo(LatLng(tagInfo.위도.toDouble(), tagInfo.경도.toDouble()))
                    naverMap.moveCamera(cameraUpdate)

                    naverMap.run {
                        // 현재 줌 레벨 가져오기
                        val currentZoom = cameraPosition.zoom

                        // 원하는 줌 레벨로 이동하기
                        val newZoom = currentZoom + 3.0f // 예시로 1단계씩 증가시킴
                        val cameraUpdate2 = CameraUpdate.scrollTo(cameraPosition.target)
                        zoomTo(newZoom)
                        moveCamera(cameraUpdate2)
                    }

                    fragmentCampsiteBinding.root2.requestFocus()
                    searchView.hide()
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): SearchedCampsiteViewHolder {
            val rowSearchedCampsiteBinding = RowSearchedCampsiteBinding.inflate(layoutInflater)
            val searchedCampsiteViewHolder = SearchedCampsiteViewHolder(rowSearchedCampsiteBinding)

            rowSearchedCampsiteBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return searchedCampsiteViewHolder
        }

        override fun getItemCount(): Int {
            return campList.size
        }

        override fun onBindViewHolder(holder: SearchedCampsiteViewHolder, position: Int) {
            try {
                holder.imageViewCampsite.setImageResource(R.drawable.camping_24px)
                holder.textViewCampsiteName.text = campList[position].캠핑장이름
                holder.textViewCampsiteAdress.text = campList[position].주소
            } catch (e: java.lang.Exception) {
                Log.d("testt", "오류방지")
            }

        }
    }

    override fun onClick(p0: Overlay): Boolean {
        val dialogBinding = DialogMarkerBinding.inflate(layoutInflater)
        if (p0 is Marker) {
            val marker = p0.tag as CampsiteInfo
            val builder = MaterialAlertDialogBuilder(
                mainActivity,
                R.style.ThemeOverlay_App_MaterialAlertDialog
            ).apply {
                setView(dialogBinding.root)
                setTitle("캠핑장 정보")
                Glide.with(mainActivity).load(marker.사진).error(R.drawable.ic_launcher_background).into(dialogBinding.imageViewCampsite)
                dialogBinding.textViewCampsiteName.text = marker.캠핑장이름
                dialogBinding.textViewCampsiteAddress.text = marker.주소
                dialogBinding.textViewCampsiteNumber.text = marker.전화번호
                dialogBinding.textViewCampsiteDetail.text = marker.시설소개
                dialogBinding.textViewCampsiteEnv.text = marker.주변환경
                dialogBinding.textViewCampsiteForm.text = marker.형태
                dialogBinding.textViewCampsiteFacilities.text = marker.편의시설
                dialogBinding.textViewCampsiteFun.text = marker.놀거리
                dialogBinding.textViewCampsiteAnimal.text = marker.애완동물여부
                dialogBinding.textViewCampsiteUrl.run {
                    text = marker.홈페이지
                    setTextColor(Color.BLUE)
                    setOnClickListener {
                        val intent= Intent(Intent.ACTION_VIEW, Uri.parse(marker.홈페이지))
                        startActivity(intent)
                    }
                }
                setPositiveButton("닫기", null)
            }
            builder.show()
        }
        return false
    }
}