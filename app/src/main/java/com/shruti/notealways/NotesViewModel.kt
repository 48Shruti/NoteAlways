package com.shruti.notealways

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class NotesViewModel : ViewModel() {
    private val _updateTitle : MutableLiveData<String> = MutableLiveData()
    private var _updateDescription : MutableLiveData<String> = MutableLiveData()
    var updateTitle : LiveData<String> get() = _updateTitle
    var updateDescription : MutableLiveData<String> = MutableLiveData("")

    fun updateNote(id:String,updateTitle:String,updateDescription:String){
        updateTitle.
    }
}