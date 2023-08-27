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
import com.naver.maps.map.util.FusedLocationSource
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentContractCampsiteBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class ContractCampsiteFragment : Fragment(), OnMapReadyCallback {
    lateinit var fragmentContractCampsiteBinding: FragmentContractCampsiteBinding
    lateinit var mainActivity: MainActivity
    lateinit var contractNaverMap: NaverMap
    lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentContractCampsiteBinding = FragmentContractCampsiteBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        
        fragmentContractCampsiteBinding.toolbarContractCampsite.run {
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {

                //프래그먼트 종료시 내부 맵 프래그먼트의 getMapAsync도 종료시키기 위해 제휴 맵 프래그먼트 수동 종료
                val mapFragment =
                    mainActivity.supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
                mapFragment?.let {
                    mainActivity.supportFragmentManager.beginTransaction().remove(it).commit()
                }
                mainActivity.replaceFragment(MainActivity.CAMPSITE_FRAGMENT,false,false,null)
            }
        }

        //비동기로 네이버 객체를 얻어온다
        val fm = mainActivity.supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        //OnMapReadyCallback등록
        mapFragment.getMapAsync(this)

        return fragmentContractCampsiteBinding.root
    }

    override fun onMapReady(p0: NaverMap) {
        contractNaverMap = p0
        Log.d("testt", "제휴캠핑장")
    }

    //해당 프래그먼트가 액티비티에 붙을 때 바텀 네비 삭제
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val main=activity as MainActivity
        main.activityMainBinding.bottomNavigationViewMain.visibility=View.GONE

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //프래그먼트 종료시 내부 맵 프래그먼트의 getMapAsync도 종료시키기 위해 제휴 맵 프래그먼트 수동 종료
                val mapFragment =
                    main.supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
                mapFragment?.let {
                    main.supportFragmentManager.beginTransaction().remove(it).commit()
                }
                main.replaceFragment(MainActivity.CAMPSITE_FRAGMENT,false,false,null)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
        //해당 프래그먼트가 액티비티에서 떨어져 나갈 때 바텀 네비 다시 생성
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility=View.VISIBLE

//        //프래그먼트 종료시 내부 맵 프래그먼트의 getMapAsync도 종료시키기 위해 제휴 맵 프래그먼트 수동 종료
//        val mapFragment =
//            mainActivity.supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
//        mapFragment?.let {
//            mainActivity.supportFragmentManager.beginTransaction().remove(it).commit()
//        }

    }
}