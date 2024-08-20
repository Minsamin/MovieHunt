package com.rpimx.moviehunt.common.utils


sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resource<*>

        return when (this) {
            is Success -> other is Success<*> && data == other.data
            is Error -> other is Error<*> && message == other.message
            is Loading -> other is Loading<*>
        }
    }

    override fun hashCode(): Int {
        return when (this) {
            is Success -> data.hashCode()
            is Error -> message.hashCode()
            is Loading -> TODO()
        }
    }

}