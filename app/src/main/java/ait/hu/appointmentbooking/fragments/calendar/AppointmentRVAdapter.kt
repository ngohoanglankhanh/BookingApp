package ait.hu.appointmentbooking.fragments.calendar

import ait.hu.appointmentbooking.MainActivity
import ait.hu.appointmentbooking.R
import ait.hu.appointmentbooking.fragments.calendar.data.Appointment
import ait.hu.appointmentbooking.fragments.calendar.data.AppointmentDatabase
import ait.hu.appointmentbooking.fragments.touch.TouchHelperCallback
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.appointment_row.view.*
import java.util.*

class AppointmentRVAdapter : RecyclerView.Adapter<AppointmentRVAdapter.ViewHolder>, TouchHelperCallback {

    private var appointmentList = mutableListOf<Appointment>()
    private lateinit var context: Context

    constructor(context: Context, appointmentList: List<Appointment>) {
        this.context = context
        this.appointmentList.addAll(appointmentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.appointment_row, parent, false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAppointment = appointmentList[position]

        holder.tvCustomerName.text = currentAppointment.customerName
        holder.tvService.text = currentAppointment.service
        holder.tvPrice.text = (context as MainActivity).resources.getString(R.string.price_with_value, currentAppointment.price)
        holder.cbCompleted.isChecked = currentAppointment.completed
        holder.tvDate.text = currentAppointment.date.split("Date: ")[1].trim()
        holder.tvTimeStart.text = currentAppointment.timeStart.split("Time: ")[1].trim()

        holder.btnDelete.setOnClickListener {
            deleteAppointment(holder.adapterPosition)
        }

        holder.cbCompleted.setOnClickListener {
            appointmentList[holder.adapterPosition].completed = holder.cbCompleted.isChecked

            Thread {
                AppointmentDatabase.getInstance(context).appointmentDao().updateAppointment(appointmentList[holder.adapterPosition])
            }.start()
        }
    }

    private fun deleteAppointment(position: Int) {
        Thread {
            AppointmentDatabase.getInstance(context).appointmentDao().deleteAppointment(appointmentList[position])

            (context as MainActivity).runOnUiThread {
                appointmentList.removeAt(position)
                notifyItemRemoved(position)
            }
        }.start()
    }

    fun addAppointment(appointment: Appointment) {
        appointmentList.add(appointment)
        notifyItemInserted(appointmentList.lastIndex)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAppointmentIcon : ImageView = itemView.ivAppointmentIcon
        val tvCustomerName : TextView = itemView.tvCustomerName
        val tvService : TextView = itemView.tvService
        val tvPrice : TextView = itemView.tvPrice
        val cbCompleted: CheckBox = itemView.cbCompleted
        val tvDate : TextView = itemView.tvDate
        val tvTimeStart : TextView = itemView.tvTimeStart
        val btnDelete : Button = itemView.btnDelete
    }

    override fun onDismissed(position: Int) {
        deleteAppointment(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(appointmentList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}