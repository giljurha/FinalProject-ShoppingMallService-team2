package com.test.campingusproject_seller.ui.myinfo

import android.content.DialogInterface
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentMyInfoBinding
import com.test.campingusproject_seller.repository.UserInfoRepository
import com.test.campingusproject_seller.ui.main.MainActivity

class MyInfoFragment : Fragment() {
    lateinit var fragmentMyInfoBinding:FragmentMyInfoBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentMyInfoBinding= FragmentMyInfoBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentMyInfoBinding.run {
            buttonMyInfoLogout.paintFlags= Paint.UNDERLINE_TEXT_FLAG
            buttonMyInfoUpdate.paintFlags=Paint.UNDERLINE_TEXT_FLAG

            val builder = MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog)

            buttonMyInfoLogout.setOnClickListener {

                builder.run{
                    setTitle("로그아웃")
                    setMessage("정말 로그아웃 하시겠습니까?")
                    setPositiveButton("네"){ dialogInterface: DialogInterface, i: Int ->
                        UserInfoRepository.deletePre(mainActivity)
                        mainActivity.replaceFragment(MainActivity.LOGIN_FRAGMENT,false,false,null)
                        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

                    }
                    setNegativeButton("아니오"){dialogInterface: DialogInterface, i: Int ->

                    }
                }.show()
            }

            buttonMyInfoSignOut.setOnClickListener {
                builder.run{
                    setTitle("회원탈퇴")
                    setMessage("정말 탈퇴 하시겠습니까?")
                    setPositiveButton("네"){ dialogInterface: DialogInterface, i: Int ->
//                        val database = FirebaseDatabase.getInstance()
//                        val db = database.getReference("SellerUser")

//                        db.orderByChild("sellerUserId").equalTo().delete()


                    }
                    setNegativeButton("아니오"){ dialogInterface: DialogInterface, i: Int ->

                    }
                }.show()
            }

            buttonMyInfoUpdate.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.UPDATE_MY_INFO_FRAGMENT, true, true, null)

            }
        }
        return fragmentMyInfoBinding.root
    }
}




