package com.test.campingusproject_customer.ui.myprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentModifyMyprofileBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class ModifyMyprofileFragment : Fragment() {

    lateinit var fragmentModifyMyprofileBinding: FragmentModifyMyprofileBinding
    lateinit var mainActivity: MainActivity

    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    lateinit var profileImage : Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentModifyMyprofileBinding = FragmentModifyMyprofileBinding.inflate(layoutInflater)

        //앨범 런처 초기화
        albumLauncher = albumSetting(fragmentModifyMyprofileBinding.imageViewModifyMyProfileImage)

        //현재 회원 정보를 가진 sharedPreference 객체
        val sharedPreference = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)

        fragmentModifyMyprofileBinding.run {
            //유저 정보 가져옴
            val userName =  sharedPreference.getString("customerUserName", null)
            val userPw = sharedPreference.getString("customerUserPw", null)
            val userShipRecipient = sharedPreference.getString("customerUserShipRecipient", null)
            val userShipContact = sharedPreference.getString("customerUserShipContact", null)
            val userShipAddress = sharedPreference.getString("customerUserShipAddress", null)
            val userProfileImage = sharedPreference.getString("customerUserProfileImage", null)

            //뷰 초기값 설정
            textInputEditTextModifyMyProfileName.setText(userName)
            textInputEditTextModifyMyProfilePw.setText(userPw)
            textInputEditTextModifyMyProfilePw2.setText(userPw)
            editTextModifyMyprofileInputDestinationAddress.setText(userShipAddress)
            editTextModifyMyprofileInputPhoneNumber.setText(userShipContact)
            editTextModifyMyprofileInputReceiverName.setText(userShipRecipient)

            //프로필 이미지 설정 여부에 따른 뷰 이미지 설정
            if(userProfileImage?.isEmpty()!!){
                imageViewModifyMyProfileImage.setImageResource(R.drawable.account_circle_24px)
                imageViewModifyMyProfileImage.setBackgroundResource(R.drawable.shape_myprofile)
            }else{
                Glide.with(mainActivity).load(userProfileImage)
                    .override(500, 500)
                    .into(imageViewModifyMyProfileImage)
            }

            imageButtonModifyMyProfileImage.setOnClickListener {
                //앨범
                //앨범 이동
                val albumIntent = Intent(Intent.ACTION_PICK)
                albumIntent.setType("image/*")

                val mimeType = arrayOf("image/*")
                albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                albumLauncher.launch(albumIntent)
            }

            materialToolbarModifyMyProfile.run {
                title = "회원 정보 수정"
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MODIFY_MYPROFILE_FRAGMENT)
                }
                setOnMenuItemClickListener {
                    if(it.itemId == R.id.menuItemSave){
                        //수정 내용 등록


                    }
                    true
                }

            }


        }

        return fragmentModifyMyprofileBinding.root
    }

    fun albumSetting(imageView : ImageView) : ActivityResultLauncher<Intent> {
        //앨범에서 이미지 가져오기
        val albumLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            //이미지 가져오기 성공
            if(it.resultCode == Activity.RESULT_OK){
                it.data?.data?.let { uri ->
                    if(uri != null){
                        profileImage = uri
                        imageView.setImageURI(uri)
                    }
                }
            }
        }
        return albumLauncher
    }

    // 이미지 파일에 기록되어 있는 회전 정보를 가져온다.
    fun getDegree(uri:Uri) : Int{
        var exifInterface: ExifInterface? = null

        // 사진 파일로 부터 tag 정보를 관리하는 객체를 추출한다.
        try {
            val inputStream = mainActivity.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                exifInterface = ExifInterface(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var degree = 0
        if(exifInterface != null){
            // 각도 값을 가지고온다.
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

            when(orientation){
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        }
        return degree
    }
}