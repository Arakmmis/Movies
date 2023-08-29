package yassir.moviesapp.domain.helpers

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import yassir.moviesapp.domain.Api

object RetrofitHelper {

    fun testApiInstance(baseUrl: String): Api {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(Api::class.java)
    }

}