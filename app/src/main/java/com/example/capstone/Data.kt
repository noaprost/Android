package com.example.capstone

import android.net.Uri
import java.io.Serializable

data class Review(
    val image:Uri,
    val comment:String,
    val date:String,
    val score:Int,
    val keyword1:String,
    val keyword2:String,
    val keyword3:String,
    val userName:String
): Serializable