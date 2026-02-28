package com.example.flixsterplus

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PersonAdapter(
    private val context: Context,
    private val people: List<Person>
) : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val ivProfile: ImageView = itemView.findViewById(R.id.ivProfile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_person, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = people[position]

        holder.tvName.text = person.name

        // If profilePath is empty or null, show placeholder
        if (!person.profilePath.isNullOrEmpty()) {
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500${person.profilePath}")
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(holder.ivProfile)
        } else {
            holder.ivProfile.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    override fun getItemCount(): Int {
        return people.size
    }
}