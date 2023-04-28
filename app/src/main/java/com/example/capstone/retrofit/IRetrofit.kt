package com.example.capstone.retrofit

import com.example.capstone.*
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {

    @POST("/restaurants")
    fun showRestaurants(@Body resAddress: resAddress):Call<RestaurantList>

    @POST("/restaurants/category")
    fun showRestaurantsByCategory(@Body resAddressCategory: resAddressCategory):Call<RestaurantList>

    @POST("/restaurant/name")
    fun searchRestaurants(@Body resName: resName):Call<RestaurantList>

    @POST("/user/waiting/insert")
    fun addWaiting(@Body addWaiting: AddWaiting):Call<AddWaiting>

    @POST("/restaurant/reviews")
    fun showRestaurantReview(@Body ResID: ResID):Call<RestaurantReviewList>

    @POST("/review")
    fun writeReview(@Body writeReview: WriteReview):Call<WriteReview>

    @DELETE("/mypage/leaveId")
    fun deleteAccount(@Body userIdPassword: UserIdPassword):Call<UserIdPassword>

    @POST("/review/image")
    fun getReviewImage(@Body RevIdx: RevIdx):Call<ReturnRevImg>

    @GET("/mypage/review")
    fun myReview(@Body userId: UserId):Call<MyReview>

}
