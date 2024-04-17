package com.shruti.notealways

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar

data class TodoDataClass(var title : String ?= "",
                         var id :String ?= "",
    var time : String = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time))
