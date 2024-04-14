package com.shruti.notealways

interface NotesInterface {
    fun notesUpdate(notesDataClass: NotesDataClass,position:Int)
    fun notesDelete(notesDataClass: NotesDataClass,position: Int)
}