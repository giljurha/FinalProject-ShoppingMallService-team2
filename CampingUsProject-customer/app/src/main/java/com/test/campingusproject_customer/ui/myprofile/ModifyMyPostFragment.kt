package com.test.campingusproject_customer.ui.myprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentModifyMyPostBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class ModifyMyPostFragment : Fragment() {
    lateinit var fragmentModifyMyPostBinding: FragmentModifyMyPostBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentModifyMyPostBinding = FragmentModifyMyPostBinding.inflate(layoutInflater)

        fragmentModifyMyPostBinding.run {
            materialToolbarModifyMyPost.run {
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MODIFY_MY_POST_FRAGMENT)
                }

                //카메라, 앨벙
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_item_camera ->{

                        }
                        R.id.menu_item_album ->{

                        }
                    }
                    true
                }
            }

            textViewModifyMyPostSave.run {
                isClickable = true // 클릭 가능하게
                //누르면
                setOnClickListener {
                    //저장....
                    mainActivity.removeFragment(MainActivity.MODIFY_MY_POST_FRAGMENT)
                    mainActivity.replaceFragment(MainActivity.POST_READ_FRAGMENT,true,true,null)
                }
            }
        }

        return fragmentModifyMyPostBinding.root
    }
}