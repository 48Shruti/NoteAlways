package com.shruti.notealways

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.shruti.notealways.databinding.FragmentAddNotesBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddNotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
 class AddNotesFragment : Fragment(),NotesInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding : FragmentAddNotesBinding
    var firestore =  FirebaseFirestore.getInstance()
    lateinit var adapter: NotesAdapter
     lateinit var  mainActivity : MainActivity
     var notesDataClass: NotesDataClass ?= null
     var item = arrayListOf<NotesDataClass>()
    var noteId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            noteId = it.getString("notesId", "")
        }
        binding = FragmentAddNotesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NotesAdapter(item, this)
        binding.addnotesFragment = this
        if(noteId.isNotEmpty()) {
            firestore.collection("note").document(noteId)
                .get().addOnSuccessListener { data ->
                    if (data != null) {
                        val title = data.getString("title")
                        val description = data.getString("description")
                        binding.ettitle.setText(title)
                        binding.etdescription.setText(description)
                        NotesClick()
                    }
                }
                .addOnFailureListener { exep ->
                    Log.e(TAG, "Error getting document", exep)
                }

        }

        binding.imgbuttonbookmark.setOnClickListener {
            BookmarkClick()
        }
//        binding.imgbuttondone.setOnClickListener {
//            navigateToBookmarkFragment()
//        }
    }


    fun BookmarkClick(){
        binding.imgbuttonbookmark.setImageResource(R.drawable.baseline_bookmark_added_24)
        Toast.makeText(requireContext(), "Bookmark saved", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToBookmarkFragment() {
        val bundle = Bundle().apply {
            putString("bookmarkId", notesDataClass?.id)
        }
        findNavController().navigate(R.id.action_addNotesFragment_to_bookmarkFragment, bundle)
    }

    fun NotesClick(){
        if (noteId.isNotEmpty()) {
            var updateNotes =
                NotesDataClass(
                    title = binding.ettitle.text.toString(),
                    description = binding.etdescription.text.toString()
                )
            firestore.collection("note").document(noteId)
                .set(updateNotes)
                .addOnSuccessListener {
                    Toast.makeText(mainActivity, "Data update", Toast.LENGTH_SHORT).show()
                    getCollectionNote()
                }
                .addOnCanceledListener {
                    Toast.makeText(mainActivity, "Data  update cancel", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(mainActivity,"Data update failure", Toast.LENGTH_SHORT).show()
                }
            adapter.notifyDataSetChanged()
            findNavController().navigate(R.id.mainFragment)

        }
        else {
            if (binding.ettitle.text.isNullOrEmpty()) {
                binding.ettitle.error = "Enter title"
            } else if (binding.etdescription.text.isNullOrEmpty()) {
                binding.etdescription.error = "Enter description"
            } else {
                firestore.collection("note").add(
                    NotesDataClass(
                        title = binding.ettitle.text.toString(),
                        description = binding.etdescription.text.toString()
                    )
                )
                    .addOnSuccessListener {
                        Toast.makeText(mainActivity, "Data Added", Toast.LENGTH_SHORT).show()
                        getCollectionNote()
                    }
                    .addOnCanceledListener {
                        Toast.makeText(mainActivity, "Data cancel", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(mainActivity, "Data failure", Toast.LENGTH_SHORT).show()
                    }
                adapter.notifyDataSetChanged()
                mainActivity.navController.navigate(R.id.mainFragment)
            }
        }
    }
    fun getCollectionNote(){
        item.clear()
        firestore.collection("note").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(ContentValues.TAG, "Listen failed", e)
                return@addSnapshotListener
            }
            for (dc in snapshot!!) {
                val firestoreClass = dc.toObject(NotesDataClass::class.java)
                firestoreClass.id = dc.id
                item.add(firestoreClass)
            }
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddNotesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddNotesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun notesUpdate(notesDataClass: NotesDataClass, position: Int) {
    }



    override fun notesDelete(notesDataClass: NotesDataClass, position: Int) {
        TODO("Not yet implemented")
    }

    override fun bookmark(notesDataClass: NotesDataClass, position: Int) {
        TODO("Not yet implemented")
    }


}