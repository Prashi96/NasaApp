package com.pp.nasaapp.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class Repository (private val apiInterface: ApiInterface) {
    val baseURL = "https://api.nasa.gov/"
    val apiKey = "SM1N7aTvrfDSqMqae4hBqWcTBvqfLNokLe1h5NGd"
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    suspend fun getPictures(): ResultResponse<ArrayList<APODResponse>?>{
        return safeApiCall(dispatcher) {
            apiInterface.getPictures(baseURL+"planetary/apod?api_key=$apiKey","2022-09-1","2022-09-11")
        }
    }

    suspend fun getPictureOfTheDay(date:String): ResultResponse<APODResponse?>{
        return safeApiCall(dispatcher) {
            apiInterface.getAstronomyPicture(baseURL+"planetary/apod?api_key=$apiKey",date)
        }
    }

    private suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> Response<T>): ResultResponse<T?> {

        return withContext(dispatcher) {
            try {
                apiCall.invoke().let {
                    when {
                        it.isSuccessful-> {
                            ResultResponse.Success(it.body())
                        }
                        else -> {
                            ResultResponse.Error(it.message())
                        }
                    }
                }

            } catch (throwable: Throwable) {
                ResultResponse.Error("Something went wrong")
            }
        }
    }


}