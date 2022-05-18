package com.example.kltnstudyword.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kltnstudyword.R
import com.example.kltnstudyword.roomdatabase.AppDatabase
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentHome.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val KEY_VERSION_COUNT_DATE = "count_date"
class FragmentHome : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            FragmentHome().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayData()
    }

    private fun displayData() {
        val mDb = AppDatabase.getInMemoryDatabase(requireActivity().applicationContext)
        mDb?.roomDao()?.countWordLearned().let {
            txtNumWordLearnedHome.text="$it"
        }
        mDb?.roomDao()?.countNumLevel(4).let{
            txtNumWordLevel4.text="$it"
        }
        sharedPreferences = requireActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE)
        sharedPreferences.getInt(KEY_VERSION_COUNT_DATE,0).let{
            txtCountDate.text = "$it"

        }


    }
}