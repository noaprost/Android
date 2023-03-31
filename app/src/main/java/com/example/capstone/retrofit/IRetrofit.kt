package com.example.capstone.retrofit

import com.example.capstone.*
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {

    @POST("/restaurants")
    fun showRestaurants(@Body resAddress: resAddress):Call<RestaurantList>

    @POST("/restaurant/name")
    fun searchRestaurants(@Body resName: String):Call<List<RestaurantList>>
}
