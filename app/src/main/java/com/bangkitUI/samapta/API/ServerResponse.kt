package com.bangkitUI.samapta.API

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.R.id




@Parcelize
class ServerResponse (
    @field:SerializedName("response")
    var response: String? = null,

    var error: String? = null,

    @SerializedName("provinsi")
    var provinsi: String? = null,

    @SerializedName("kota")
    var kota: String? = null,

    @SerializedName("kecamatan")
    var kecamatan: String? = null,

    @SerializedName("namabencana")
    var namabencana: String? = null,

    @SerializedName("deskripsi")
    var deskripsi: String? = null



//    val message: String?
//        get() {
//            return message
//        }
) : Parcelable