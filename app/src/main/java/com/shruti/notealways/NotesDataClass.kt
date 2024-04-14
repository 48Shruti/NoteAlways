package com.shruti.notealways

data class NotesDataClass(
    val title : String ?= "",
    val description : String ?= "",
    var id : String ?= ""
)
