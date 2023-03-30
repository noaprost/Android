package com.example.capstone.retrofit

import android.graphics.Paint
import com.example.capstone.*
import org.w3c.dom.Comment
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {

    @DELETE("/post/{postingId}") //글 삭제
    fun deletePosting(@Path("postingId") postingId:Long):Call<Long>

    @DELETE("/post/delete/{recruitingId}") //참여하기 취소
    fun cancelJoinGroup(@Path("recruitingId") recruitingId:Long):Call<Long>

    @DELETE("/comment/{commentId}")
    fun deleteComment(@Path("commentId") commentId:Long):Call<Long>

    @DELETE("/bookmark/{bookmarkId}")
    fun deleteBookMark(@Path("bookmarkId") bookmarkId:Long):Call<Long>

    @GET("/rooms/login/{memberId}") //로그인시 불러오는 리스트
    fun loadChatList(@Path("memberId") memberId:Long) :Call<List<Long>>

    @DELETE("/follow/delete/{friendId}/{memberId}")
    fun cancelFollow(@Path("friendId") friendId:Long, @Path("memberId") memberId:Long): Call<Long>
}
