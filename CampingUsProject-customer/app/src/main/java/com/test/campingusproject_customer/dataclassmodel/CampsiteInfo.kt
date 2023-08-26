package com.test.campingusproject_customer.dataclassmodel

import com.google.gson.annotations.SerializedName

data class CampsiteInfo(
    @SerializedName("facltNm") var 캠핑장이름: String,//캠핑장 이름
    @SerializedName("mapX") var 경도: String,//경도
    @SerializedName("mapY") var 위도: String,//위도
    @SerializedName("tel") var 전화번호: String,//번호
    @SerializedName("homepage") var 홈페이지: String,//홈페이지
    @SerializedName("intro") var 시설소개: String,//캠핑장 설명
    @SerializedName("lctCl") var 주변환경: String,//주변 환경(산,계곡 등)
    @SerializedName("induty") var 형태: String,//캠핑장 형태(야영장 카라반 등)
    @SerializedName("sbrsEtc") var 편의시설: String,//캠핑장 시설(전기,온수 등)
    @SerializedName("posblFcltyCl") var 놀거리: String,//캠핑장 시설(전기,온수 등)
    @SerializedName("animalCmgCl") var 애완동물여부: String,//애완동물가능여부(가능(소형견))
    @SerializedName("firstImageUrl") var 사진: String,//사진URI
    @SerializedName("addr1") var 주소: String//사진URI
)

data class ApiResponse(
    @SerializedName("response") val response: Response
)

data class Response(
    @SerializedName("header") val header: Header,
    @SerializedName("body") val body: Body
)

data class Header(
    @SerializedName("resultCode") val resultCode: String,
    @SerializedName("resultMsg") val resultMsg: String
)

data class Body(
    @SerializedName("items") val items: Items,
    @SerializedName("numOfRows") val numOfRows: Int,
    @SerializedName("pageNo") val pageNo: Int,
    @SerializedName("totalCount") val totalCount: Int
)

data class Items(
    @SerializedName("item") val item: MutableList<CampsiteInfo>
)