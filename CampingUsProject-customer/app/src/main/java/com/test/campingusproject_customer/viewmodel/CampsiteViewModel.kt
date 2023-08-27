package com.test.campingusproject_customer.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.campingusproject_customer.dataclassmodel.ApiResponse
import com.test.campingusproject_customer.dataclassmodel.Items
import com.test.campingusproject_customer.repository.CampingService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class CampsiteViewModel: ViewModel() {
    //레트로핏을 실행 메서드
    val campingService = CampingService.getCampingService()


    var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception: ${throwable.localizedMessage}")
    }


    var campSites = MutableLiveData<Items>()//불러온 캠핑장 정보들

    var dataInfo = MutableLiveData<ApiResponse>()//불러온 데이터 상태

    var campsiteLoadError= MutableLiveData<String>()//데이터 오류를 식별하기위한 변수

//    var loading= MutableLiveData<Boolean>()// 데이터 로딩중인지에 대한 정보(프로그래스바에 사용됨.)



//이거 나오면 좋은거임
    fun fetchCampsites(page:Int,latitude:String,longitude:String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                withContext(Dispatchers.Main) {
                    val call = campingService.getCampSiteList(100,page,"AND","CampingUs","hojdaAj28uKSvkkT5O01VLlmsMbVDxwWfk5norTQMAdtVK6+18evQogPO5ix63vdVPoPG6hGVUGv2iZ3nKzJvA==","json",longitude,latitude,"20000")
                    call.enqueue(object :retrofit2.Callback<ApiResponse>{
                        override fun onResponse(
                            call: Call<ApiResponse>,
                            response: Response<ApiResponse>,
                        ) {
                            if(response.isSuccessful){
                                dataInfo.value=response.body()
                                campSites.value=response.body()?.response?.body?.items

                            }else{
                                Log.d("testt","통신은 성공했지만 데이터를 불러오진 못했습니다")
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                            Log.d("testt","통신 자체를 실패했습니다")
                        }

                    })
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    Log.d("CoroutineException", "오류 내용 : ${t.message}\n")
                    onError("Exception: ${t.localizedMessage}")
                }
            }
        }

    }

    fun fetchSearchedCampsites(page:Int,inputKeyWord:String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                withContext(Dispatchers.Main) {
                    val call = campingService.getCampSiteSearch(3000,page,"AND","CampingUs","hojdaAj28uKSvkkT5O01VLlmsMbVDxwWfk5norTQMAdtVK6+18evQogPO5ix63vdVPoPG6hGVUGv2iZ3nKzJvA==","json",inputKeyWord)
                    call.enqueue(object :retrofit2.Callback<ApiResponse>{
                        override fun onResponse(
                            call: Call<ApiResponse>,
                            response: Response<ApiResponse>,
                        ) {
                            if(response.isSuccessful){
                                Log.d("testt","통신중")
                                dataInfo.value=response.body()
                                campSites.value=response.body()?.response?.body?.items
                            }else{
                                Log.d("testt","통신은 성공했지만 데이터를 불러오진 못했습니다")

                            }
                        }

                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                            Log.d("testt","통신 자체를 실패했습니다")
                            campsiteLoadError.value =""

                        }

                    })
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    Log.d("CoroutineException", "오류 내용 : ${t.message}\n")
                    onError("Exception: ${t.localizedMessage}")
                }
            }
        }

    }

    fun onError(message: String){

//        loading.value = false
    }
    fun resetData(){
        job=null
        dataInfo = MutableLiveData<ApiResponse>()
        campSites = MutableLiveData<Items>()
    }
    override fun onCleared() {
        super.onCleared()
        Log.d("testt","뷰모델 삭제")
    }
}