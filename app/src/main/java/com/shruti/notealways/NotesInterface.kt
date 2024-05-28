package com.shruti.notealways

interface NotesInterface {
    fun notesUpdate(notesDataClass: NotesDataClass,position:Int)
    fun notesClick(notesDataClass: NotesDataClass)
    fun notesId(notesDataClass: NotesDataClass,id:Int)
    fun notesDelete(notesDataClass: NotesDataClass,position: Int)
}