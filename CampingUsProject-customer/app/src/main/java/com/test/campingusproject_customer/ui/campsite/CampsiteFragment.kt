package com.test.campingusproject_customer.ui.campsite

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentCampsiteBinding
import com.test.campingusproject_customer.ui.main.MainActivity
class CampsiteFragment : Fragment(), OnMapReadyCallback {
    lateinit var fragmentCampsiteBinding: FragmentCampsiteBinding
    lateinit var mainActivity:MainActivity
    lateinit var callback: OnBackPressedCallback
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentCampsiteBinding = FragmentCampsiteBinding.inflate(layoutInflater)
        mainActivity=activity as MainActivity

        //비동기로 네이버 객체를 얻어온다
        val fm = mainActivity.supportFragmentManager
        val mapFragment= fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map,it).commit()
            }
        //OnMapReadyCallback등록
        mapFragment.getMapAsync(this)


        return fragmentCampsiteBinding.root
    }

    //지도객체가 callback되면 호출되는 메서드
    override fun onMapReady(p0: NaverMap) {
        Log.d("testt","지도 띄워짐")
    }

    //뒤로가기 버튼 눌렀을 때 동작할 코드 onDetech까지
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.removeFragment(MainActivity.CAMPSITE_FRAGMENT)
                mainActivity.activityMainBinding.bottomNavigationViewMain.selectedItemId = R.id.menuItemHome
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}