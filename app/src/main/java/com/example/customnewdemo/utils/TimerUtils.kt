package com.example.customnewdemo.utils

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


fun AppCompatActivity.countDown(
    time: Int = 5,
    start: (scope: CoroutineScope) -> Unit,
    next: (timee: String) -> Unit,
    end: () -> Unit
) {

    lifecycleScope.launch {

        flow<Int> {
            (time downTo 0).forEach {

                delay(1000)
                emit(it)
            }
        }.onStart { start(this@launch) }.onCompletion {
            end()
        }.catch {

        }.collect{
            next(it.toString())
        }
    }


}