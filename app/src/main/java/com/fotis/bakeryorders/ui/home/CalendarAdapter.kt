package com.fotis.bakeryorders.ui.home

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fotis.bakeryorders.R
import com.fotis.bakeryorders.ui.gallery.CakeData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarAdapter(
    private val days: List<Calendar?>,
    private val ordersMap: Map<String, List<CakeData>>,
    private val onDayClick: (Calendar, List<CakeData>) -> Unit
): RecyclerView.Adapter<CalendarAdapter.DayViewHolder>(){


    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayNumber: TextView = view.findViewById(R.id.text_day_number)
        val cakesNumber: TextView = view.findViewById(R.id.cakes_number)
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_cell, parent, false)
            return DayViewHolder(view)
        }

           override fun getItemCount(): Int = days.size

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        if (day != null) {
            val dateFormat = SimpleDateFormat("d-M-yyyy", Locale.getDefault())
            val dateKey = dateFormat.format(day.time)

            val orders = ordersMap[dateKey] ?: emptyList()
            holder.dayNumber.text = day.get(Calendar.DAY_OF_MONTH).toString()

            if (orders.isNotEmpty())

                holder.cakesNumber.text =   orders.size.toString()+"\uD83C\uDF82"
            else
                holder.cakesNumber.text = ""


            val outline = GradientDrawable()
            outline.setShape(GradientDrawable.RECTANGLE)
            outline.setStroke(1, holder.itemView.resources.getColor(android.R.color.black))  // Μαύρο περίγραμμα με πάχος 2dp
            outline.setColor(holder.itemView.resources.getColor(android.R.color.white))  // Χρώμα φόντου λευκό
            outline.cornerRadius = 8f  // Στρογγυλές γωνίες με ακτίνα 8dp
            holder.itemView.background = outline

            // ➡️ ΕΛΕΓΧΟΣ αν είναι σήμερα
            val today = Calendar.getInstance()
            if (day.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                day.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                day.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {

                // Αν είναι σήμερα, αλλάζουμε το background ή το text color
                outline.setColor(holder.itemView.resources.getColor(R.color.Calendar))
               // holder.itemView.setBackgroundResource(R.color.light_green) // θα φτιάξεις ένα drawable
            } else {
                // αλλιώς το default χρώμα
                outline.setColor(holder.itemView.resources.getColor(R.color.white))
                //holder.itemView.setBackgroundResource(R.color.white)
            }

            holder.itemView.setOnClickListener {
                onDayClick(day, orders)
            }
        } else {
            holder.dayNumber.text = ""
            holder.cakesNumber.text = ""
            holder.itemView.setOnClickListener(null)
        }
    }




}





