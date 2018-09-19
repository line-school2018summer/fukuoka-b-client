package com.line.fukuokabclient.Fragments

import android.annotation.SuppressLint
import android.app.usage.UsageEvents
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.line.fukuokabclient.R
import com.line.fukuokabclient.R.id.my_name
import com.line.fukuokabclient.R.id.toolbar_update_profile
import com.line.fukuokabclient.R.menu.main_settings_toolbar
import com.line.fukuokabclient.dto.UserDTO
import kotlinx.android.synthetic.main.fragment_settings.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SettingsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SettingsFragment : Fragment()  {
    // TODO: Rename and change types of parameters
    private var userName = ""
    private var hitokoto = ""
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString(ARG_USER_NAME)!!
            hitokoto = it.getString(ARG_USER_HITOKOTO) ?: ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity!!.title = getString(R.string.navigation_settings)

        val v = inflater.inflate(R.layout.fragment_settings, container, false)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        my_name.setText(userName)
        my_name.isEnabled = false
        my_name.setTextColor(Color.parseColor("#555555"))

        with (my_hitokoto) {
            setTextColor(Color.parseColor("#555555"))
            isEnabled = false
            setText(hitokoto)
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(user: UserDTO) {
        listener?.onSettingsFragmentInteraction(user)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onSettingsFragmentInteraction(user: UserDTO?)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters

        const val ARG_USER_NAME = "name"
        const val ARG_USER_HITOKOTO = "hitokoto"

        @JvmStatic
        fun newInstance(user: UserDTO) =
                SettingsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_USER_NAME, user.name)
                        putString(ARG_USER_HITOKOTO, user.hitokoto)
                    }
                }
    }
}
