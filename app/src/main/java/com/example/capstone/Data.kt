package com.example.capstone

import android.media.Image
import okhttp3.MultipartBody
import java.io.File
import java.io.Serializable
import java.time.format.DateTimeFormatter

data class Review(
    val RevIdx:Int,
    val UserID: String,
    val ResID: String,
    val Rating:Long,
    val RevTxt: String,
    val RevImgID:Int,
    val RevKeyWord: String,
    val RevSatis:Int,
    val RevRecom:Int,
    val RevTime:String
): Serializable

data class MyReview(
    val image:Int,
    val comment:String,
    val date:String,
    val score:Long,
    val keyword1:String,
    val keyword2:String,
    val keyword3:String,
    val restaurant:String
): Serializable

data class WriteReview(
    val myFile:String,
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
    val category:String
)
data class resName(
    val resName:String
)
data class ResID(
    val ResID:Int
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

data class UserIdPassword(
    val userID: String,
    val userPassword:String
)