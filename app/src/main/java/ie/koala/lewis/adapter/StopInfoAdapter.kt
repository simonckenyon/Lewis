/**
 * Copyright © 2020, The Koala Computer Company Limited.
 * All Rights Reserved
 */
package ie.koala.lewis.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ie.koala.lewis.R
import ie.koala.lewis.model.Direction
import ie.koala.lewis.util.Debug

class StopInfoAdapter(private val direction: Direction) :
    RecyclerView.Adapter<StopInfoAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val cardTitleTextView: TextView
        val cardDescriptionTextView: TextView

        init {
            v.setOnClickListener {
                Debug.debug("tram $adapterPosition clicked.")
            }
            cardTitleTextView = v.findViewById(R.id.cardTitleTextView) as TextView
            cardDescriptionTextView = v.findViewById(R.id.cardDescriptionTextView) as TextView
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_tram, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (position < getItemCount()) {
            val destination = direction.tram[position].destination
            if (!destination?.startsWith("No ", ignoreCase = true)!!) {
                val due: String? = direction.tram[position].dueMins
                when (due) {
                    "" -> {
                        viewHolder.cardTitleTextView.text = ""
                    }
                    "DUE" -> {
                        viewHolder.cardTitleTextView.text = context.cardTitleTextView.resources.getString(R.string.now)
                    }
                    else -> {
                        try {
                            val d: Int? = due?.toInt()

                                viewHolder.cardTitleTextView.text =
                                    viewHolder.cardTitleTextView.resources.getQuantityString(
                                        R.plurals.arrival_time,
                                        d!!,
                                        d
                                    )
                        } catch (e: Exception) {
                            Debug.exception("error converting due time to a number", e)
                        }
                    }
                }
                viewHolder.cardDescriptionTextView.text = viewHolder.cardTitleTextView.resources.getString(R.string.destination, destination)
            } else {
                viewHolder.cardTitleTextView.text = destination
                viewHolder.cardDescriptionTextView.text = ""
            }
        }
    }

    override fun getItemCount(): Int {
        return direction.tram.size
    }
}