//package com.udepardo.bicicoru.domain.interactor
//
//import android.arch.lifecycle.LiveData
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.LinkedBlockingQueue
//import java.util.concurrent.ThreadPoolExecutor
//import java.util.concurrent.TimeUnit
//
///**
// * Created by fernando.ude on 19/01/2018.
// */
//
//
//abstract class CustomExecutor<in U, T> : LiveData<T>() {
//
//    abstract fun start(params: U? = null)
//
//    fun runOnExecutor(function: () -> Unit) {
//        MyThreadPoolExecutor.execute(Runnable {
//            function()
//        })
//    }
//
//    object MyThreadPoolExecutor : InteractorExecutor {
//        /**
//         * Time to keep an idle thread if the size has expired.
//         */
//        private const val KEEP_ALIVE_TIME = 1L
//
//        /**
//         * The number unit in seconds to wait.
//         */
//        private val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS
//
//        /**
//         * The number of cores available.
//         */
//        private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()
//
//        /**
//         * The number of cores.
//         */
//        private val CORE_POOL_SIZE = NUMBER_OF_CORES + 1
//
//        /**
//         * The maximum number of processes running in the pool.
//         */
//        private val MAXIMUM_POOL_SIZE = NUMBER_OF_CORES * 2 + 1
//
//        /**
//         * The pool threadPolicy.compile
//         */
//        private val poolQueue: ExecutorService = ThreadPoolExecutor(
//                CORE_POOL_SIZE,
//                MAXIMUM_POOL_SIZE,
//                KEEP_ALIVE_TIME,
//                KEEP_ALIVE_TIME_UNIT, LinkedBlockingQueue())
//
//        override fun execute(runnable: Runnable) {
//            poolQueue.submit(runnable)
//
//        }
//    }
//}
