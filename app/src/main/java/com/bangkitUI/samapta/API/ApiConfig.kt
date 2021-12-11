package com.bangkitUI.samapta.API

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap

interface ApiConfig {
    @Multipart
    @POST("https://predroad-scahsyn5ya-et.a.run.app")
    fun upload(
        @Header("response") response: String?,
        @Part("namabencana") namabencana: String?,
        @Part("deskripsi") deskripsi: String?,
        @Part("provinsi") provinsi: String?,
        @Part("kota") kota: String?,
        @Part("kecamatan") kecamatan: String?,
        @PartMap map: HashMap<String, RequestBody>
    ): Call<ServerResponse?>?
}