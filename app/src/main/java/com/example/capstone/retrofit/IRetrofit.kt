package com.example.capstone.retrofit

import com.example.capstone.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {

    @POST("/restaurants")
    fun showRestaurants(@Body resAddress: resAddress):Call<RestaurantList>

    @POST("/restaurants/category")
    fun showRestaurantsByCategory(@Body resAddressCategory: resAddressCategory):Call<RestaurantList>

    @POST("/restaurant/name")
    fun searchRestaurants(@Body resName: resName):Call<RestaurantList>

    @POST("/user/waiting/insert2")
    fun addWaiting(@Body addWaiting: AddWaiting):Call<WaitingInfo>

    @POST("/restaurant/reviews")
    fun showRestaurantReview(@Body ResID: ResID):Call<RestaurantReviewList>

    /*@POST("/review")
    fun writeReview(@Body requestBody: RequestBody):Call<WriteReview>*/

    @DELETE("/mypage/leaveId")
    fun deleteAccount(@Body userIdPassword: UserIdPassword):Call<UserIdPassword>

    //@POST("/review/image")
    //fun getReviewImage(@Body RevIdx: RevIdx):Call<ReturnRevImg>

    @POST("/mypage/review")
    fun myReview(@Body userId: userId):Call<MyReview>

    @POST("/restaurant/age") //연령별 선호도
    fun preferenceByAge(@Body ResID: ResID):Call<PreferenceByAge>

    @POST("/restaurant/gender") //성별별 선호도
    fun preferenceByGender(@Body ResID: ResID):Call<PreferenceByGender>

    @POST("/restaurant/hour") //시간별 복잡도
    fun complexityByHour(@Body resPhNum: resPhNum):Call<List<complexityByHour>>

    @POST("/restaurant/date") //요일별 복잡도
    fun complexityByDate(@Body resPhNum: resPhNum):Call<List<complexityByDate>>

    @POST("/user/waiting/ordernumber") //대기 내역 확인
    fun waitingInfoCheck(@Body waitIndex: WaitIndex): Call<ResWaitInfo>

    @POST("/main/recommend")
    fun recommendRestaurant(@Body userId: userId):Call<RecommendRestaurants>

    @POST("/main/hot")
    fun hotRestaurant():Call<RecommendRestaurants>

    @POST("/restaurant/id")
    fun getResInfo(@Body ResID: ResID):Call<Restaurants>
}
