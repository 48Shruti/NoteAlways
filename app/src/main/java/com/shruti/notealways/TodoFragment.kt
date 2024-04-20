package com.shruti.notealways

import android.icu.text.Transliterator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shruti.notealways.databinding.FragmentMainBinding
import com.shruti.notealways.databinding.FragmentTodoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TodoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
 class TodoFragment : Fragment() , TodoInterface{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding : FragmentTodoBinding
    lateinit var mainActivity: MainActivity
    lateinit var adapter: TodoAdapter
    var firebase = Firebase.firestore
    var item  = arrayListOf<TodoDataClass>()
    lateinit var linearLayout : LinearLayoutManager
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TodoFragment.
         */
        // TODO: Rename and change types and number of parameters
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
        TODO("Not yet implemented")
        
    }

    override fun getCollectionTodo() {
        firebase.collection("todo").get()
            .addOnSuccessListener {
                for(items in it.documents){
                    val firestore = items.toObject(TodoDataClass::class.java)?: TodoDataClass()
                    firestore.id=  items.id
                    item.add(firestore)
                }
                Toast.makeText(mainActivity,"Todo is added",Toast.LENGTH_SHORT).show()
            }
        adapter.notifyDataSetChanged()
    }

    override fun todoMark(todoDataClass: TodoDataClass, position: Int) {

    }


}