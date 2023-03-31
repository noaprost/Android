package com.example.capstone

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class Review(
    val image:Int,
    val comment:String,
    val date:String,
    val score:Long,
    val keyword1:String,
    val keyword2:String,
    val keyword3:String,
    val userName:String
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

data class Restaurant(
    val restaurantImg: Int,
    val rating: Double,
    val commentNumber: Int,
    val name: String,
    val waiting: Int
)

data class Restaurants(
    val resImg: Int,
    val resRating: Double,
    val resCategory: String,
    val commentNumber: Int, //todo
    val resName: String,
    val waiting: Int, //todo
    val keyword1: String, //todo
    val keyword2: String, //todo
    val keyword3: String //todo
)

data class Res(
    val resIdx: Int,
    val resName : String,
    val resAddress: String,
    val resCategory: String,
    val resPhNum: String,
    val resSeat: String,
    val resOpen: String,
    val resClose: String,
    val resWaitOpen: String,
    val resWaitClose: String,
    val resRating: String,
    val resImg: Int
    //    val resMngNum : String,
    //    val resPwd: String,
)
data class resAddress(
    val resAddress:String
)
data class RestaurantList(
    val results: List<Restaurants>
)