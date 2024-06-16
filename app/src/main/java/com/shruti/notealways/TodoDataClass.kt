package com.shruti.notealways

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar

data class TodoDataClass(var title : String ?= "",
    var id :String ?= "",
    var time : String ?= "",
    var completed: Boolean = false)
