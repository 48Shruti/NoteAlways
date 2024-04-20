package com.shruti.notealways

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.text.Transliterator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shruti.notealways.databinding.BottomsheetTodoBinding
import com.shruti.notealways.databinding.FragmentBottomsheetBinding
import com.shruti.notealways.databinding.FragmentMainBinding
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(),NotesInterface, TodoInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding : FragmentMainBinding
    lateinit var mainActivity: MainActivity
    lateinit var adapter: NotesAdapter
    var firebase = Firebase.firestore
    var itemNote  = arrayListOf<NotesDataClass>()
    var itemTodo  = arrayListOf<TodoDataClass>()
    lateinit var linearLayout : LinearLayoutManager
    private val TAG = "MainFragment"
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
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.show = true
        binding.mainFragment = this
        adapter = NotesAdapter(itemNote,this)
        binding.recylerlist.adapter = adapter
        linearLayout = LinearLayoutManager(mainActivity)
        binding.recylerlist.layoutManager = linearLayout
        getCollectionNote()
        binding.btntodo.setOnClickListener{
            mainActivity.navController.navigate(R.id.todoFragment)
        }

    }
    fun getCollectionNote(){
        firebase.collection("note").get()
            .addOnSuccessListener {
                for(items in it.documents){
                    val firestore = items.toObject(NotesDataClass::class.java)?: NotesDataClass()
                    firestore.id=  items.id
                    itemNote.add(firestore)
                }
            }
        adapter.notifyDataSetChanged()
    }
    fun fabButton() {
        val dialogBinding = FragmentBottomsheetBinding.inflate(layoutInflater)
        var bottomSheet = BottomSheetDialog(mainActivity).apply {
            setContentView(dialogBinding.root)
            show()
        }

        dialogBinding.btnNoteClick.setOnClickListener {
            bottomSheet.dismiss()
            mainActivity.navController.navigate(R.id.addNotesFragment)
        }
        dialogBinding.btnTodoClick.setOnClickListener {
            bottomSheet.dismiss()
            val dialogBindingTodo = BottomsheetTodoBinding.inflate(layoutInflater)
            var bottomSheettodo = BottomSheetDialog(mainActivity).apply {
                setContentView(dialogBindingTodo.root)
                show()
            }
            dialogBindingTodo.btnsetdata.setOnClickListener{
                var dialog = DatePickerDialog(mainActivity)
                dialog.setOnDateSetListener{view,year,month,dayOfMonth ->
                    var simpleDateFormat = SimpleDateFormat("dd,MMM,yyyy")
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

                    firebase.collection("todo").add(
                        TodoDataClass(title = dialogBindingTodo.ettodo.text.toString(),
                            time = dialogBindingTodo.btnsetdata.text.toString()),

                    )
                        .addOnSuccessListener {
                            Toast.makeText(mainActivity, "Data Added",Toast.LENGTH_SHORT).show()
                                getCollectionTodo()

                        }
                        .addOnCanceledListener{
                            Toast.makeText(mainActivity, "Data cancel",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(mainActivity, "Data failure",Toast.LENGTH_SHORT).show()
                        }
                    adapter.notifyDataSetChanged()
                    bottomSheettodo.dismiss()
                    //mainActivity.navController.navigate(R.id.mainFragment)
                }
               // Toast.makeText(mainActivity,"yes its working", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    fun getCollectionTodo(){
//        firebase.collection("todo").get()
//            .addOnSuccessListener {
//                for(items in it.documents){
//                    val firestore = items.toObject(TodoDataClass::class.java)?: TodoDataClass()
//                    firestore.id=  items.id
//                    itemTodo.add(firestore)
//                }
//            }
//        adapter.notifyDataSetChanged()
//    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun notesUpdate(notesDataClass: NotesDataClass, position: Int) {
        TODO("Not yet implemented")
    }

    override fun notesClick(notesDataClass: NotesDataClass) {
//        mainActivity.navController.navigate(R.id.addNotesFragment)
//        getCollectionNote()
    }

    override fun notesDelete(notesDataClass: NotesDataClass, position: Int) {
        TODO("Not yet implemented")
    }



    override fun delete(todoDataClass: TodoDataClass, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getCollectionTodo() {
        firebase.collection("todo").get()
            .addOnSuccessListener {
                for(items in it.documents){
                    val firestore = items.toObject(TodoDataClass::class.java)?: TodoDataClass()
                    firestore.id=  items.id
                    itemTodo.add(firestore)
                }
                Toast.makeText(mainActivity,"Todo is added",Toast.LENGTH_SHORT).show()

            }
        adapter.notifyDataSetChanged()
    }

    override fun todoMark(todoDataClass: TodoDataClass, position: Int) {
        firebase.c
    }


}