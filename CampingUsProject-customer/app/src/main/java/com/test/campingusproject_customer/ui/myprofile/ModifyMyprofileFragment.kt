package com.test.campingusproject_customer.ui.myprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentModifyMyprofileBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class ModifyMyprofileFragment : Fragment() {
    lateinit var fragmentModifyMyprofileBinding: FragmentModifyMyprofileBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentModifyMyprofileBinding = FragmentModifyMyprofileBinding.inflate(layoutInflater)

fragmentModifyMyprofileBinding.run {
    materialToolbarModifyMyProfile.run {
        title = "회원 정보 수정"
        setNavigationIcon(R.drawable.arrow_back_24px)
        setNavigationOnClickListener {
            mainActivity.removeFragment(MainActivity.MODIFY_MYPROFILE_FRAGMENT)
        }
        setOnMenuItemClickListener {
            title = "저장"
            true
        }
        textInputEditTextModifyMyProfileName.setText("빅토르")
        editTextModifyMyprofileInputDestinationName.setText("우리집")
        editTextModifyMyprofileInputReceiverName.setText("빅토르")
        editTextModifyMyprofileInputPhoneNumber.setText("010-1234-5678")
        editTextModifyMyprofileInputDestinationAddress.setText("서울특별시 송파구 올림픽로 300 시그니엘 꼭대기층임ㅋ")
    }
}

        return fragmentModifyMyprofileBinding.root
    }
}