package com.shruti.notealways

import android.icu.text.Transliterator.Position

interface TodoInterface {
    fun delete (notesDataClass: NotesDataClass,position: Position)
}