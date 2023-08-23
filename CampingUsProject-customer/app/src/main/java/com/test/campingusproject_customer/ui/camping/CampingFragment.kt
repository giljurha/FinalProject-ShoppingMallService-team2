package com.test.campingusproject_customer.ui.camping

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentCampingBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class CampingFragment : Fragment() {
    lateinit var fragmentCampingBinding: FragmentCampingBinding
    lateinit var mainActivity: MainActivity
    lateinit var callback: OnBackPressedCallback
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentCampingBinding = FragmentCampingBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return fragmentCampingBinding.root
    }
    //뒤로가기 버튼 눌렀을 때 동작할 코드 onDetech까지
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.removeFragment(MainActivity.CAMPING_FRAGMENT)
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