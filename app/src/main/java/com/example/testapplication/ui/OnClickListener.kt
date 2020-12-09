package com.example.testapplication.ui

class OnClickListener<T>(private val block: (T) -> Unit) {

    fun onClick(input: T) = block(input)
}