package com.example.capstone

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
    val resIdx: Int,
    val resImg: Int,
    val resRating: Double,
    val resCategory: String,
    val revCnt: Int,
    val resName: String,
    val resPhNum: String,
    val resSeat: String,
    val resOpen: String,
    val resClose: String,
    val resWaitOpen: String,
    val resWaitClose: String,
    val resAddress: String,
    val currWaiting: Int, //todo
    val keyWord:String,
    //resMngNum:String
):Serializable

data class resAddress(
    val resAddress:String
)
data class resName(
    val resName:String
)
data class RestaurantList(
    val results: List<Restaurants>
)