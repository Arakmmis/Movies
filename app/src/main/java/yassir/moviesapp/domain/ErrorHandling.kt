package yassir.moviesapp.domain

import okhttp3.internal.http.HTTP_INTERNAL_SERVER_ERROR
import okhttp3.internal.http.HTTP_NOT_FOUND
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import yassir.moviesapp.data.helpers.ResWrapper
import java.io.IOException

fun <T> Response<T>.wrap(): ResWrapper<T> {
    return if (!this.isSuccessful)
        getErrorWrapped(this)
    else {
        try {
            ResWrapper(
                httpCode = this.code(),
                data = this.body()
            )
        } catch (e: HttpException) {
            Timber.e(e.message)
            ResWrapper(
                httpCode = e.code(),
                error = e
            )
        } catch (e: IOException) {
            Timber.e(e.message)
            ResWrapper(
                httpCode = this.code(),
                error = e
            )
        } catch (e: Exception) {
            Timber.e(e.message)
            ResWrapper(error = e)
        }
    }
}

fun <T> getErrorWrapped(response: Response<T>): ResWrapper<T> {
    return when (response.code()) {
        HTTP_INTERNAL_SERVER_ERROR -> ResWrapper(
            httpCode = response.code(),
            error = HttpException(response)
        )

        HTTP_NOT_FOUND -> ResWrapper(
            httpCode = response.code(),
            error = IOException(response.errorBody()?.string())
        )

        else -> ResWrapper(
            httpCode = response.code(),
            error = Exception(response.message())
        )
    }
}
