package com.shruti.notealways

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
    var firebase =  Firebase.firestore
    lateinit var adapter: NotesAdapter
     lateinit var  mainActivity : MainActivity
     var item = arrayListOf<NotesDataClass>()
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
        binding = FragmentAddNotesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NotesAdapter(item,this)
        binding.addnotesFragment = this
    }
    fun BookmarkClick(){
    }
    fun NotesClick(){
        if(binding.ettitle.text.isNullOrEmpty()){
            binding.ettitle.error = "Enter title"
        }
        else if(binding.etdescription.text.isNullOrEmpty()){
            binding.etdescription.error = "Enter description"
        }
        else{
            firebase.collection("note").add(
                NotesDataClass(title = binding.ettitle.text.toString(),
                    description = binding.etdescription.text.toString())
            )
                .addOnSuccessListener {
                    Toast.makeText(mainActivity, "Data Added",Toast.LENGTH_SHORT).show()
                    getCollectionNote()
                }
                .addOnCanceledListener{
                    Toast.makeText(mainActivity, "Data cancel",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(mainActivity, "Data failure",Toast.LENGTH_SHORT).show()
                }
            adapter.notifyDataSetChanged()
                mainActivity.navController.navigate(R.id.mainFragment)

        }
    }
    fun getCollectionNote(){
        item.clear()
        firebase.collection("note").get()
            .addOnSuccessListener {
                for(items in it.documents){
                    val firestore = items.toObject(NotesDataClass::class.java)?: NotesDataClass()
                    firestore.id=  items.id
                    item.add(firestore)
                }
            }
        adapter.notifyDataSetChanged()
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
        TODO("Not yet implemented")
    }

    override fun notesClick(notesDataClass: NotesDataClass) {
        TODO("Not yet implemented")
    }

    override fun notesDelete(notesDataClass: NotesDataClass, position: Int) {
        TODO("Not yet implemented")
    }
}