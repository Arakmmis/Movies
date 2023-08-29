package yassir.moviesapp.data.helpers

import java.net.HttpURLConnection

data class ResWrapper<out T>(
    val httpCode: Int = HttpURLConnection.HTTP_OK,
    val data: T? = null,
    val error: Throwable? = null
)