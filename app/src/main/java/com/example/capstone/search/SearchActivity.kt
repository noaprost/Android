package com.example.capstone.search

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import com.example.capstone.R

class SearchActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContentView(R.layout.item_previous_waiting_info);
    }

}