package com.udepardo.bicicoru.domain.interactor

import kotlinx.coroutines.*

/**
 * Created by fernando.ude on 19/01/2018.
 */

class BCResult<T>(val data: T?= null, val exception: Exception? = null){
    fun isSuccess() = (data!= null)
}


interface KtInteractor<in U, out T> {
    fun syncCall(params: U? = null): T

    fun execute(params: U? = null, callback: (T) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            callback(withContext(Dispatchers.IO) { syncCall(params) })
        }
    }
}
