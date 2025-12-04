package com.example.app_kotlin.remote


import okhttp3.ResponseBody
import retrofit2.http.*

interface HospitalApiService {

    @FormUrlEncoded
    @POST("interpreter")
    suspend fun getHospitales(
        @Field("data") query: String
    ): ResponseBody
}
