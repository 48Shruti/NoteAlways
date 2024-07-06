package com.shruti.notealways

import android.app.DatePickerDialog
import android.content.ContentValues
import android.graphics.Canvas
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.text.Transliterator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shruti.notealways.databinding.BottomsheetTodoBinding
import com.shruti.notealways.databinding.FragmentBottomsheetBinding
import com.shruti.notealways.databinding.FragmentMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale
import java.util.Locale.filter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(),NotesInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding : FragmentMainBinding
    lateinit var mainActivity: MainActivity
     var notesDataClass: NotesDataClass ?= null
    lateinit var adapter: NotesAdapter
    val firestore = FirebaseFirestore.getInstance()
    lateinit var searchView: SearchView
    var itemNote  = arrayListOf<NotesDataClass>()
    var itemNoteFiltered = arrayListOf<NotesDataClass>()
    var itemTodo  = arrayListOf<TodoDataClass>()
    lateinit var linearLayout : LinearLayoutManager
    val TAG = "MainFragment"
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
        searchView = view.findViewById(R.id.searchView)
        setSearchView()
        getCollectionNote()
        delete()
        binding.btntodo.setOnClickListener{
            mainActivity.navController.navigate(R.id.todoFragment)
        }
        binding.btnbookmark.setOnClickListener {
            mainActivity.navController.navigate(R.id.bookmarkFragment)
        }
    }
    private fun setSearchView(){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }
        })
    }

    private fun filter(query: String?) {
        val filterList = ArrayList<NotesDataClass>()
      if (query != null){
          for (i in itemNote){
              if (i.title.lowercase(Locale.ROOT).contains(query)){
                  filterList.add(i)
              }
          }
          if (filterList.isEmpty()){
              Toast.makeText(mainActivity,"No data found",Toast.LENGTH_SHORT).show()
          }
          else{
              adapter.setFilteredList(filterList)
          }
      }
    }

    private fun getCollectionNote(){
        itemNote.clear()
        firestore.collection("note").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed", e)
                return@addSnapshotListener
            }
            for (dc in snapshot!!) {
                val firestoreClass = dc.toObject(NotesDataClass::class.java)
                firestoreClass.id = dc.id
                itemNote.add(firestoreClass)
            }


            adapter.notifyDataSetChanged()
        }
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
                    var simpleDateFormat = SimpleDateFormat("dd/MMM/yyyy")
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
                    firestore.collection("todo").add(
                        TodoDataClass(title = dialogBindingTodo.ettodo.text.toString(),
                            time = dialogBindingTodo.btnsetdata.text.toString()),
                    )
                        .addOnSuccessListener {
                            Toast.makeText(mainActivity, "Data Added",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(mainActivity, "Data failure",Toast.LENGTH_SHORT).show()
                        }
                        .addOnCanceledListener{
                            Toast.makeText(mainActivity, "Data cancel",Toast.LENGTH_SHORT).show()
                        }
                    adapter.notifyDataSetChanged()
                    bottomSheettodo.dismiss()
                }
            }
        }
    }



    fun delete() {

        val deleteIcon: Drawable? = ContextCompat.getDrawable(mainActivity, R.drawable.icon_delete)
        val intrinsicWidth = deleteIcon?.intrinsicWidth
        val intrinsicHeight = deleteIcon?.intrinsicHeight
        val background = ColorDrawable()
        val backgroundColor = Color.parseColor("#f44336")
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Toast.makeText(mainActivity, "on move", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    val position = viewHolder.adapterPosition
                    val itemToRemove = itemNote[position]
                    val backup = itemToRemove
                    itemNote.clear()
                    firestore.collection("note").document(itemToRemove.id)
                        .delete()
                        .addOnSuccessListener {
                            getCollectionNote()

                            Snackbar.make(binding.root, "Item deleted", Snackbar.LENGTH_LONG)
                                .setAction("Undo") {
                                    itemNote.clear()
                                    firestore.collection("note").add(backup)
                                        .addOnSuccessListener {
                                            getCollectionNote()
                                            Toast.makeText(mainActivity, "Data restored", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener { execp ->
                                            Toast.makeText(mainActivity, "Failed to restore item: ${execp.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }.show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(mainActivity, "Data fail", Toast.LENGTH_SHORT).show()
                        }

                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top

                background.color = backgroundColor
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                background.draw(c)

                val iconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
                val iconMargin = (itemHeight - intrinsicHeight) / 2
                val iconLeft = itemView.right - iconMargin - intrinsicWidth!!
                val iconRight = itemView.right - iconMargin
                val iconBottom = iconTop + intrinsicHeight

                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                deleteIcon.draw(c)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recylerlist)
        adapter.notifyDataSetChanged()
    }



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
        val bundle = Bundle()
        bundle.putString("notesId",notesDataClass.id)
        findNavController().navigate(R.id.addNotesFragment,bundle)
    }
}