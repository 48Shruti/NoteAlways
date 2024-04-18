package com.shruti.notealways

import android.icu.text.Transliterator.Position

interface TodoInterface {
    fun delete (todoDataClass: TodoDataClass,position: Position)
    fun getCollectionTodo()
}