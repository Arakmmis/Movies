package yassir.moviesapp.util

import retrofit2.HttpException
import retrofit2.Response
import yassir.moviesapp.data.helpers.ResWrapper

fun <T> Response<T>.wrap(): ResWrapper<T> {
    return try {
        ResWrapper(
            data = this.body()
        )
    } catch (e: HttpException) {
        ResWrapper(
            httpCode = e.code(),
            error = e
        )
    } catch (e: Exception) {
        ResWrapper(error = e)
    }
}