package com.shruti.notealways

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.icu.text.SimpleDateFormat
import android.icu.text.Transliterator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shruti.notealways.databinding.BottomsheetTodoBinding
import com.shruti.notealways.databinding.FragmentMainBinding
import com.shruti.notealways.databinding.FragmentTodoBinding
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TodoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodoFragment : Fragment(), TodoInterface {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentTodoBinding
    lateinit var mainActivity: MainActivity
    lateinit var adapter: TodoAdapter
    var firestore = FirebaseFirestore.getInstance()
    var item = arrayListOf<TodoDataClass>()
    lateinit var linearLayout: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TodoAdapter(item,this)
        binding.recycler.adapter = adapter
        linearLayout = LinearLayoutManager(mainActivity)
        binding.recycler.layoutManager = linearLayout
        getCollectionTodo()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TodoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun delete(todoDataClass: TodoDataClass, position: Int) {
        item.clear()
        firestore.collection("todo").document(todoDataClass.id ?: "")
            .delete()
            .addOnSuccessListener {
                Toast.makeText(mainActivity,"Data delete",Toast.LENGTH_SHORT).show()
                getCollectionTodo()
            }
            .addOnCanceledListener {
                Toast.makeText(mainActivity,"Data Cancel",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(mainActivity,"Data fail",Toast.LENGTH_SHORT).show()
            }
        adapter.notifyDataSetChanged()
    }

    override fun update(todoDataClass: TodoDataClass, position: Int) {
        val dialogBindingTodo = BottomsheetTodoBinding.inflate(layoutInflater)
        var bottomSheettodo = BottomSheetDialog(mainActivity).apply {
            setContentView(dialogBindingTodo.root)
            show()
        }
        dialogBindingTodo.ettodo.setText(todoDataClass.title)
        dialogBindingTodo.btnsetdata.setText(todoDataClass.time)
        dialogBindingTodo.btnsetdata.setOnClickListener{
            var dialog = DatePickerDialog(mainActivity)
            dialog.setOnDateSetListener{view,year,month,dayOfMonth ->
                var simpleDateFormat = SimpleDateFormat("dd/MMM/yy")
                var calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR,year)
                calendar.set(Calendar.MONTH,month)
                calendar.set(Calendar.DATE,dayOfMonth)
                var stringDate = simpleDateFormat.format(calendar.time)
                dialogBindingTodo.btnsetdata.setText(stringDate)
            }
            dialog.show()
        }
        dialogBindingTodo.tvdone.setOnClickListener{
            if(dialogBindingTodo.ettodo.text.isNullOrEmpty()){
                dialogBindingTodo.ettodo.error = "Enter task"
            }
            else{
                item.clear()
                var updateTodo =  TodoDataClass(title = dialogBindingTodo.ettodo.text.toString(),
                    time = dialogBindingTodo.btnsetdata.text.toString())
                firestore.collection("todo").document(todoDataClass.id ?:"")
                    .set(updateTodo)
                    .addOnSuccessListener {
                        getCollectionTodo()
                        Toast.makeText(mainActivity, "Data Added", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(mainActivity, "Data failure", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCanceledListener{
                        Toast.makeText(mainActivity, "Data cancel", Toast.LENGTH_SHORT).show()
                    }
                adapter.notifyDataSetChanged()
                bottomSheettodo.dismiss()
            }
        }
    }

    override fun getCollectionTodo() {
        item.clear()
        firestore.collection("todo").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed", e)
                return@addSnapshotListener
            }
            for (dc in snapshot!!) {
                val firestoreClass = dc.toObject(TodoDataClass::class.java)
                firestoreClass.id = dc.id
                item.add(firestoreClass)
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun todoMark(todoDataClass: TodoDataClass, position: Int) {
        item.clear()
        firestore.collection("todo").document(todoDataClass.id ?: "")
            .update(mapOf("completed" to todoDataClass.completed))
            .addOnSuccessListener {
                item[position].completed = todoDataClass.completed
                adapter.notifyItemChanged(position)
                Toast.makeText(mainActivity, "Todo successfully updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error occurred", e)
            }
    }
}

