package ru.descend.gamekotlin

import android.util.Log
import java.util.*

fun log(text: String){
    Log.e("#MDE", text)
}

fun String.firstLower(): String {
    return when (this.length) {
        0 -> ""
        1 -> this.lowercase(Locale.getDefault())
        else -> this[0].lowercaseChar() + this.substring(1)
    }
}