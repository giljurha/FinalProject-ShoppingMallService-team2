package com.test.campingusproject_customer.ui.myprofile

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentModifyMyprofileBinding
import com.test.campingusproject_customer.dataclassmodel.CustomerUserModel
import com.test.campingusproject_customer.repository.CustomerUserRepository
import com.test.campingusproject_customer.ui.main.MainActivity

class ModifyMyprofileFragment : Fragment() {

    lateinit var fragmentModifyMyprofileBinding: FragmentModifyMyprofileBinding
    lateinit var mainActivity: MainActivity

    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    var profileImage : Uri? = null

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
            val userId = sharedPreference.getString("customerUserId", null)
            val userName =  sharedPreference.getString("customerUserName", null)
            val userPw = sharedPreference.getString("customerUserPw", null)
            val userPhoneNumber = sharedPreference.getString("customerUserPhoneNumber", null)
            val userShipRecipient = sharedPreference.getString("customerUserShipRecipient", null)
            val userShipContact = sharedPreference.getString("customerUserShipContact", null)
            val userShipAddress = sharedPreference.getString("customerUserShipAddress", null)
            var userProfileImage = sharedPreference.getString("customerUserProfileImage", null)

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
                userProfileImage = "CustomerUserProfile/$userId/1"
            }else{
                CustomerUserRepository.getUserProfileImage(userProfileImage){
                    Glide.with(mainActivity).load(it.result)
                        .override(500, 500)
                        .into(imageViewModifyMyProfileImage)
                }
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
                //수정 내용 등록
                setOnMenuItemClickListener {
                    if(it.itemId == R.id.menuItemSave){
                        val profileName = textInputEditTextModifyMyProfileName.text.toString()
                        val profilePw = textInputEditTextModifyMyProfilePw.text.toString()
                        val profilePw2 = textInputEditTextModifyMyProfilePw2.text.toString()
                        val profileReceiver = editTextModifyMyprofileInputReceiverName.text.toString()
                        val profilePhoneNumber = editTextModifyMyprofileInputPhoneNumber.text.toString()
                        val profileAddress = editTextModifyMyprofileInputDestinationAddress.text.toString()

                        //요소 입력 검사
                        if(textInputLayoutisEmptyCheck(textInputLayoutModifyMyProfileName, textInputEditTextModifyMyProfileName, "이름을 입력해주세요")||
                           textInputLayoutisEmptyCheck(textInputLayoutModifyMyProfilePw, textInputEditTextModifyMyProfilePw, "비밀번호를 입력해주세요")||
                           textInputLayoutisEmptyCheck(textInputLayoutModifyMyProfilePw2, textInputEditTextModifyMyProfilePw2, "비밀번호 확인을 입력해주세요"))
                        {
                            return@setOnMenuItemClickListener true
                        }

                        if(profileAddress.isEmpty() || profileReceiver.isEmpty() || profilePhoneNumber.isEmpty()){
                            return@setOnMenuItemClickListener true
                        }

                        //비밀번호 일치 검사
                        if(profilePw != profilePw2){
                            textInputEditTextModifyMyProfilePw2.setText("")
                            textInputEditTextModifyMyProfilePw.setText("")

                            createDialog("비밀번호 오류", "비밀번호가 일치하지 않습니다"){
                                mainActivity.focusOnView(textInputLayoutModifyMyProfilePw)
                            }
                            return@setOnMenuItemClickListener true
                        }

                        //변경된 값으로 유저 객체 생성
                        val customerUserModel = CustomerUserModel(profileName, userId!!, profilePw,
                        profileReceiver, profilePhoneNumber, profileAddress, userPhoneNumber!!, userProfileImage!!)

                        //수정된 유저 객체를 DB와 SharedPreference에 저장하고 화면 전환
                        CustomerUserRepository.modifyUserInfo(customerUserModel.customerUserId, customerUserModel){
                            //이미지 등록 검사
                            if(profileImage != null){
                                CustomerUserRepository.uploadProfileImage(userProfileImage, profileImage!!){
                                    CustomerUserRepository.saveUserInfo(sharedPreference, customerUserModel)
                                    Snackbar.make(fragmentModifyMyprofileBinding.root, "유저 정보 수정이 완료되었습니다.", Snackbar.LENGTH_SHORT).show()
                                    mainActivity.removeFragment(MainActivity.MODIFY_MYPROFILE_FRAGMENT)
                                }
                            }else{
                                CustomerUserRepository.saveUserInfo(sharedPreference, customerUserModel)
                                Snackbar.make(fragmentModifyMyprofileBinding.root, "유저 정보 수정이 완료되었습니다.", Snackbar.LENGTH_SHORT).show()
                                mainActivity.removeFragment(MainActivity.MODIFY_MYPROFILE_FRAGMENT)
                            }
                        }

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

                        setImage(uri, imageView)
                    }
                }
            }
        }
        return albumLauncher
    }

    //uri를 이미지뷰에 셋팅하는 함수
    fun setImage(image: Uri, imageView: ImageView){
        val inputStream = mainActivity.contentResolver.openInputStream(image)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        //회전 각도값을 가져옴
        val degree = getDegree(image)

        //회전 이미지를 생성한다
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotateBitmap = Bitmap.createBitmap(bitmap!!, 0, 0, bitmap.width, bitmap.height, matrix, false)

        //글라이드 라이브러리로 view에 이미지 출력
        Glide.with(mainActivity).load(rotateBitmap)
            .into(imageView)
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

    //textInputLayout 오류 표시 함수
    fun textInputLayoutEmptyError(textInputLayout: TextInputLayout, errorMessage : String){
        textInputLayout.run {
            error = errorMessage
            setErrorIconDrawable(R.drawable.error_24px)
            requestFocus()
        }
    }

    //textInputLayout 입력 검사 함수
    fun textInputLayoutisEmptyCheck(
        textInputLayout: TextInputLayout,
        textInputEditText: TextInputEditText,
        errorMessage: String) : Boolean
    {
        if(textInputEditText.text.toString().isEmpty()){
            //입력되지 않았으면 오류 표시
            textInputLayoutEmptyError(textInputLayout, errorMessage)
            mainActivity.focusOnView(textInputEditText)
            return true
        }
        else{
            textInputLayout.error = null
            return false
        }
    }

    fun createDialog(title : String, message : String, callback: () -> Unit){
        MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog).run {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                callback()
            }
            show()
        }
    }
}