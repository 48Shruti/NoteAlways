package com.shruti.notealways

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar

data class NotesDataClass(
    val title : String ?= "",
    val description : String ?= "",
    var id : String = "",
    var date :String = SimpleDateFormat("dd/MM/yyyy  hh:mm:a").format(Calendar.getInstance().time)
)
