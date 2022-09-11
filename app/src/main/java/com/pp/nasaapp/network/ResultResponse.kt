package com.pp.nasaapp.network

sealed class ResultResponse<T>(val data: T? = null, val message: String? = null
) {
    class Success<T>(data: T) : ResultResponse<T>(data)

    class Error<T>(message: String?, data: T? = null) : ResultResponse<T>(data, message)

    class Loading<T> : ResultResponse<T>()
}