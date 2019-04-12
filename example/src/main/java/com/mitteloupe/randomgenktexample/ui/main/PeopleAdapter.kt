package com.mitteloupe.randomgenktexample.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mitteloupe.randomgenktexample.data.model.person.Person
import com.mitteloupe.randomgenktexample.widget.PersonView

class PeopleAdapter : RecyclerView.Adapter<PersonViewHolder>() {
    private val people = mutableListOf<Person>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val personView = PersonView(parent.context)
        personView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return PersonViewHolder(personView)
    }

    override fun getItemCount() = people.size

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.setPerson(people.get(position))
    }

    fun setPeople(people: List<Person>) {
        val diffResult = DiffUtil.calculateDiff(PeopleDiffCallback(ArrayList(this.people), people))
        diffResult.dispatchUpdatesTo(this)

        this.people.clear()
        this.people.addAll(people)
    }
}

class PeopleDiffCallback(
    private val oldPeople: List<Person>,
    private val newPeople: List<Person>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldPeople[oldItemPosition] === newPeople[newItemPosition]

    override fun getOldListSize() = oldPeople.size

    override fun getNewListSize() = newPeople.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = areItemsTheSame(oldItemPosition, newItemPosition)
}

class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setPerson(person: Person) {
        (itemView as PersonView).setPerson(person)
    }
}
