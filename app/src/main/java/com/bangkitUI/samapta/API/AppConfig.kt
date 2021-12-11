package com.bangkitUI.samapta.API

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

object AppConfig {
    val BASE_URL = "https://predroad-scahsyn5ya-et.a.run.app"
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
//https://uploadbucket9-scahsyn5ya-et.a.run.app/