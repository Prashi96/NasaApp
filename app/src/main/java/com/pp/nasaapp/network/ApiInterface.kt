package com.pp.nasaapp.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiInterface {
    @GET
    suspend fun getAstronomyPicture (@Url baseUrl: String,@Query("date") date: String?): Response<APODResponse>
    @GET
    suspend fun getPictures(@Url baseUrl: String, @Query("start_date") startDate: String?, @Query("end_date") endDate: String?):Response<ArrayList<APODResponse>>

    companion object {
        var retrofitService: ApiInterface? = null
        fun getInstance() : ApiInterface {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.nasa.gov/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(ApiInterface::class.java)
            }
            return retrofitService!!
        }

    }
}