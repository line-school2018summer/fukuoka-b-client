package com.line.fukuokabclient.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.line.fukuokabclient.R
import kotlinx.android.synthetic.main.fragment_channel_setting.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_CHANNEL_NAME = "channelName"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ChannelSettingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ChannelSettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ChannelSettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var channelName: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            channelName = it.getString(ARG_CHANNEL_NAME)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_channel_setting, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        channel_setting_item1.setOnClickListener {
            onButtonPressed("item1")
            Log.d("ONCLICK", "item1")
        }
        channel_setting_channel_name.text =channelName

        channel_setting_item2.setOnClickListener {
            onButtonPressed("item2")
            Log.d("ONCLICK", "item2")
        }
    }

    override fun onStart() {
        super.onStart()

    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(item: String) {
        listener?.onFragmentInteraction(item)
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
        fun onFragmentInteraction(item: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param channelName Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChannelSettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(channelName: String, param2: String) =
                ChannelSettingFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_CHANNEL_NAME, channelName)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
