package com.line.fukuokabclient.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.line.fukuokabclient.Adapter.FriendsRecyclerViewAdapter
import com.line.fukuokabclient.R
import com.line.fukuokabclient.dto.UserDTO

import com.line.fukuokabclient.dummy.DummyContent

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [FriendsFragment.OnListFragmentInteractionListener] interface.
 */
class FriendsFragment : Fragment() {

    private var columnCount = 1
    private var items = emptyList<UserDTO>()

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            val item_array = it.getParcelableArray(ARG_ITEMS)
            items = item_array.toList() as List<UserDTO>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_friends_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = FriendsRecyclerViewAdapter(items, listener)
            }
        }

        activity!!.title = "Friends"
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
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
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onFriendFragmentInteraction(item: UserDTO?)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_ITEMS = "items"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int, items: List<UserDTO>) =
                FriendsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                        putParcelableArray(ARG_ITEMS, items.toTypedArray())
                    }
                }
    }
}
