package com.line.fukuokabclient.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.line.fukuokabclient.Adapter.ChannelsRecyclerViewAdapter
import com.line.fukuokabclient.R
import com.line.fukuokabclient.dto.ChannelDTO
import kotlinx.android.synthetic.main.fragment_channels_list.view.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ChannelsFragment.OnListFragmentInteractionListener] interface.
 */
class ChannelsFragment : Fragment() {

    private var columnCount = 1
    private var items = emptyList<ChannelDTO>()

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            val item_array = it.getParcelableArray(ARG_ITEMS)
            items = item_array.toList() as List<ChannelDTO>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_channels_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = ChannelsRecyclerViewAdapter(items!!, listener)

                list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }

        activity!!.title = "CHANNELs"
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


    interface OnListFragmentInteractionListener {

        fun onChannelsFragmentInteraction(item: ChannelDTO?)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_ITEMS = "items"

        @JvmStatic
        fun newInstance(columnCount: Int, items: List<ChannelDTO>) =
                ChannelsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                        putParcelableArray(ARG_ITEMS, items.toTypedArray())
                    }
                }
    }
}
