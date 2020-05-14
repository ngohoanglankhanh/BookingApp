package ait.hu.appointmentbooking.fragments.calendar

import ait.hu.appointmentbooking.MainActivity
import ait.hu.appointmentbooking.R
import ait.hu.appointmentbooking.fragments.calendar.data.Appointment
import ait.hu.appointmentbooking.fragments.calendar.data.AppointmentDatabase
import ait.hu.appointmentbooking.fragments.touch.RecyclerListTouchCallback
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_calendar.view.*

class FragmentCalendar : Fragment() {
    companion object {
        const val TAG = "FRAGMENT_CALENDAR"
        const val KEY_APPOINTMENT_DATE = "APPOINTMENT_DATE"
    }

    private lateinit var appointmentListAdapter: AppointmentRVAdapter
    private lateinit var rvAppointmentList : RecyclerView

    private var showCalendar = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_calendar, container,false)

        rvAppointmentList = rootView.rvAppointment

        initRecyclerView()

        rootView.calendarView.visibility = View.GONE
        rootView.calendarView.setOnDateChangeListener { calendarView, year, month, date ->
            showNewAppointmentDialog(getString(R.string.date_format, year, month, date))
        }

        rootView.btnCalendar.setOnClickListener {
            if (!showCalendar) {
                rootView.calendarView.visibility = View.GONE
                showCalendar = true
                rootView.btnCalendar.text = getString(R.string.show_calendar)
            } else {
                rootView.calendarView.visibility = View.VISIBLE
                showCalendar = false
                rootView.btnCalendar.text = getString(R.string.hide_calendar)

            }
        }

        return rootView
    }

    private fun initRecyclerView() {
        Thread {
            val appointmentList = AppointmentDatabase.getInstance(context as MainActivity).appointmentDao().getAllAppointments()

            (context as MainActivity).runOnUiThread {
                appointmentListAdapter = AppointmentRVAdapter(context as MainActivity, appointmentList)
                rvAppointmentList.adapter = appointmentListAdapter
                addItemTouchHelper()
                addItemDivider()
            }
        }.start()
    }

    private fun addItemTouchHelper() {
        val touchCallbackList = RecyclerListTouchCallback(appointmentListAdapter)
        val itemTouchHelper = ItemTouchHelper(touchCallbackList)
        itemTouchHelper.attachToRecyclerView(rvAppointmentList)
    }

    private fun addItemDivider() {
        val itemDecoration = DividerItemDecoration(
            activity, DividerItemDecoration.VERTICAL
        )
        rvAppointmentList.addItemDecoration(itemDecoration)
    }

    private fun showNewAppointmentDialog(date: String) {
        val appointmentDialog = AppointmentDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_APPOINTMENT_DATE, date)
        appointmentDialog.arguments = bundle

        appointmentDialog.show(childFragmentManager, AppointmentDialog.TAG)
    }

    public fun addNewAppointment(item: Appointment) {
        appointmentListAdapter.addAppointment(item)
    }
}