package com.shruti.notealways

import android.content.ContentValues.TAG
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.shruti.notealways.databinding.FragmentBottomsheetBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BottomsheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class BottomsheetFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mainActivity: MainActivity
    lateinit var binding: FragmentBottomsheetBinding
    lateinit var navController: NavController
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
        binding = FragmentBottomsheetBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
       // Log.d(TAG, "onViewCreated: Button Click Listener is being set up")
     //   binding.bottomSheet = this
        binding.show = true
//        binding.btnbottomClick.setOnClickListener {
//            Log.e(TAG,"Button is click")
//            findNavController().navigate(R.id.action_bottomsheetFragment_to_addNotesFragment)
//        }

    }
    fun bottomClickNotes(view: View){
        Toast.makeText(mainActivity,"Click",Toast.LENGTH_SHORT).show()
        Log.e(TAG,"Button is click")
        navController.navigate(R.id.action_bottomsheetFragment_to_addNotesFragment)

    }
    fun bottomClickTodo(view: View){
       navController.navigate(R.id.action_bottomsheetFragment_to_addTodoFragment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BottomsheetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BottomsheetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}