package com.shruti.notealways

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.shruti.notealways.databinding.FragmentBookmarkBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookmarkFragment : Fragment(), NotesInterface {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentBookmarkBinding
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adapter: NotesAdapter
    lateinit var mainActivity: MainActivity
    var bookmarkId = ""
    var firestore = FirebaseFirestore.getInstance()
    var item = arrayListOf<NotesDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            bookmarkId = it.getString("bookmarkId", "")
            Log.d(TAG, "bookmark id: $bookmarkId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NotesAdapter(item, this)
        binding.recycler.adapter = adapter
        linearLayoutManager = LinearLayoutManager(mainActivity)
        binding.recycler.layoutManager = linearLayoutManager
        if (bookmarkId.isNotEmpty()) {
            getBookmarkedNote()
        }
    }

    private fun getBookmarkedNote() {
        item.clear()
        firestore.collection("note").document(bookmarkId)
            .get()
            .addOnSuccessListener {
                Toast.makeText(mainActivity, "Data update", Toast.LENGTH_SHORT)
                    .show()
                getCollectionBookmark()
            }
            .addOnCanceledListener {
                Toast.makeText(
                    mainActivity,
                    "Data  update cancel",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    mainActivity,
                    "Data update failure",
                    Toast.LENGTH_SHORT
                ).show()
            }
        adapter.notifyDataSetChanged()
    }

    fun getCollectionBookmark() {
        item.clear()
        firestore.collection("note")
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    for (dc in snapshot.documents) {
                        val firestoreClass = dc.toObject<NotesDataClass>()
                        firestoreClass?.id = dc.id
                        if (firestoreClass != null) {
                            item.add(firestoreClass)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting bookmarks", e)
                Toast.makeText(mainActivity, "Error getting bookmarks", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookmarkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun notesUpdate(notesDataClass: NotesDataClass, position: Int) {
        // Not implemented yet
    }

    override fun notesDelete(notesDataClass: NotesDataClass, position: Int) {
        // Not implemented yet
    }

    override fun bookmark(notesDataClass: NotesDataClass, position: Int) {
        // Not implemented yet
    }
}
