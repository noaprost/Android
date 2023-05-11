package com.example.capstone

import java.io.File
import java.io.Serializable

data class Review(
    val RevIdx:Int,
    val UserID: String,
    val UserName:String,
    val ResID: String,
    val Rating:Double,
    val RevTxt: String,
    val RevImg:String,
    val RevKeyWord: String,
    val RevSatis:Int,
    val RevRecom:Int,
    val RevTime:String
): Serializable

data class WriteReview(
    val myFile:File,
    val UserID: String,
    val ResID: String,
    val Rating: Double,
    val RevTxt: String,
    val RevKeyWord: String,
    val RevSatis: Boolean,
    val RevRecom: Boolean,
    val RevTime: String

)

data class Restaurant(
    val restaurantImg: Int,
    val rating: Double,
    val commentNumber: Int,
    val name: String,
    val waiting: Int
)
data class HotRestaurant(
    val title : String,
    val info : String,
    val rating: Double,
    val commentNumber: Int,
    val address : String
)

data class Restaurants(
    val resIdx: Int,
    val resImg: String,
    val resRating: Double,
    val resCategory: String,
    val revCnt: Int,
    val resName: String,
    val resPhNum: String,
    val resSeat: String,
    val resSeatCnt:String,
    val resOpen: String,
    val resClose: String,
    val resWaitOpen: String,
    val resWaitClose: String,
    val resAddress: String,
    val currWaiting: Int,
    val keyWord:String,
    val ResComment:String
    //resMngNum:String

):Serializable

data class resAddress(
    val resAddress:String
)
data class resAddressCategory(
    val resAddress:String,
    val resCategory:String
)
data class resName(
    val resName:String
)
data class ResID(
    val ResID:String
)
data class resPhNum(
    val resPhNum:String
)
data class RestaurantList(
    val results: List<Restaurants>
)
data class RestaurantReviewList(
    val result: List<Review>
)

data class AddWaiting(
    val userID : String,
    val ResID : String,
    val Waitheadcount : Int,
    val WaitTime : String,
    val WaitSeat : String,
    val WaitisAccepted : Boolean
):Serializable

data class WaitingInfo(
    val WaitIndex:Int,
    val userID : String,
    val ResID : String,
    val Waitheadcount : Int,
    val WaitTime : String,
    val WaitSeat : String,
    val WaitisAccepted : Boolean
):Serializable

data class UserIdPassword(
    val userID: String,
    val userPassword:String
)
// 리뷰이미지
data class RevIdx(
    val RevIdx:String
)
data class RevImg(
    val RevImg:String
)
data class ReturnRevImg(
    val RevIdx:String,
    val result:List<RevImg>
)

//내가 작성한 리뷰
data class MyReviewData(
    val RevIdx:Int,
    val UserID:String,
    val ResID:String,
    val Rating:Double,
    val RevTxt:String,
    val RevKeyWord:String,
    val RevSatis:Int,
    val RevRecom:Int,
    val RevTime:String,
    val RevImg:String,
    val resIdx:String,
    val resName:String,
): Serializable

data class MyReview(
    val code:String,
    val message:List<MyReviewData>
)
data class userId(
    val userId:String
)

//통계
data class PreferenceByAge(
    val ResID : String,
    val cnt10 : Int,
    val cnt20 : Int,
    val cnt30 : Int,
    val cnt40 : Int,
    val cnt50 : Int
)

data class PreferenceByGender(
    val ResID : String,
    val cntM : Int,
    val cntF : Int,
)

data class complexityByDate(
    val resPhNum: String,
    val Mon : Int,
    val Tue: Int,
    val Wed: Int,
    val Thu: Int,
    val Fri: Int,
    val Sat: Int,
    val Sun: Int
)

data class complexityByHour(
    val resPhNum: String,
    val _0 : Int,
    val _2 : Int,
    val _4 : Int,
    val _6 : Int,
    val _8 : Int,
    val _10 : Int,
    val _12 : Int,
    val _14 : Int,
    val _16 : Int,
    val _18 : Int,
    val _20 : Int,
    val _22 : Int
)

//홈화면
data class WaitingInfoCheck(
    val massage : String
)

data class RecommendRestaurants(
    val code:Int,
    val message:List<RestaurantInfo>
)

data class RestaurantInfo(
    val keyWord: String,
    val resIdx : String,
    val resName : String,
    val resImg : String,
    val resRating : Double,
    val revCnt : Int
)