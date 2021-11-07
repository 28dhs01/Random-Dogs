package com.dhruv.randomdogs

import com.dhruv.randomdogs.model.DogDataClass
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("api/breeds/image/random")
    fun getData() : Call<DogDataClass>
}