package com.example.youtubeclone.core.network

data class Resource<T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }

        fun <T> error(msg: String?, data: T?): Resource<T> {
            return Resource(Status.ERROR,data,msg)
        }
    }
}