package com.kanch786.musicapp.extensions

/**
 * Created by faraz on 08/01/18.
 */
import android.util.Log

fun Any.v(message: String) {
    v(this.javaClass.simpleName, message)
}

fun Any.d(message: String?) {
    d(this.javaClass.simpleName, message)
}

fun Any.i(message: String) {
    i(this.javaClass.name, message)
}

fun Any.w(message: String) {
    w(this.javaClass.name, message)
}

fun Any.e(message: String) {
    e(this.javaClass.name, message)
}

fun Any.wtf(message: String) {
    wtf(this.javaClass.name, message)
}

fun Any.v(tag: String, message: String) {
    Log.v(tag, message)
}


fun Any.i(tag: String, message: String) {
    Log.i(tag, message)
}

fun Any.w(tag: String, message: String) {
    Log.w(tag, message)
}

fun Any.e(tag: String, message: String) {
    Log.e(tag, message)
}

fun Any.wtf(tag: String, message: String) {
    Log.wtf(tag, message)
}

fun Any.d(tag: String, message: String?) {
    Log.d(tag, message)
}

