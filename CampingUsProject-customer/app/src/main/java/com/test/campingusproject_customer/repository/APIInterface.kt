package com.test.campingusproject_customer.repository

import com.test.campingusproject_customer.dataclassmodel.ApiResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("locationBasedList")
    fun getCampSiteList(
        @Query("numOfRows") perData: Int,//1페이지 당 읽을 데이터 수
        @Query("pageNo") pageNum: Int,//페이지 번호
        @Query("MobileOS") mobileOs: String,//OS구분
        @Query("MobileApp") appName: String,//서비스 이름
        @Query("serviceKey") apiKey: String,//api키
        @Query("_type") outputType: String,//출력 타입(json)
        @Query("mapX") x: String,//경도
        @Query("mapY") y: String,//위도
        @Query("radius") radius: String,//거리반경
    ): Call<ApiResponse>


    @GET("searchList")
    fun getCampSiteSearch(
        @Query("numOfRows") perData: Int,//1페이지 당 읽을 데이터 수
        @Query("pageNo") pageNum: Int,//페이지 번호
        @Query("MobileOS") mobileOs: String,//OS구분
        @Query("MobileApp") appName: String,//서비스 이름
        @Query("serviceKey") apiKey: String,//api키
        @Query("_type") outputType: String,//출력 타입(json)
        @Query("keyword") keyword: String//출력 타입(json)
    ): Call<ApiResponse>
}


object CampingService{
    val BASE_URL="https://apis.data.go.kr/B551011/GoCamping/"

    fun getCampingService() : APIInterface{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)
    }
}