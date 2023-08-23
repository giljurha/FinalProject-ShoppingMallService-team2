package com.test.campingusproject_customer.ui.campsite

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentCampsiteBinding
import com.test.campingusproject_customer.ui.main.MainActivity

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


        return fragmentCampsiteBinding.root
    }

    //지도객체가 callback되면 호출되는 메서드
    override fun onMapReady(p0: NaverMap) {
        naverMap = p0
        //내 위치 맵에 설정
        naverMap.locationSource = locationSource
        //확대 축소 버튼 안보이게
        naverMap.uiSettings.isZoomControlEnabled=false
        //내 위치 버튼 위치 커스텀
        val locationButton = fragmentCampsiteBinding.buttonMyLocation
        locationButton.map=naverMap

        //권한 확인 및 승인되지 않은 경우 요청
        if (ContextCompat.checkSelfPermission(mainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

}