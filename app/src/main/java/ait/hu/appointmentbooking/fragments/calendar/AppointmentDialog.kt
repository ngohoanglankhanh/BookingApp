package ait.hu.appointmentbooking.fragments.calendar

import ait.hu.appointmentbooking.MainActivity
import ait.hu.appointmentbooking.R
import ait.hu.appointmentbooking.fragments.calendar.data.Appointment
import ait.hu.appointmentbooking.fragments.customer.data.CustomerDatabase
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.appoinment_dialog.view.*
import kotlinx.android.synthetic.main.appointment_row.view.*
import java.text.DateFormat
import java.util.*

class AppointmentDialog : DialogFragment() {

    companion object {
        const val TAG = "APPOINTMENT_DIALOG"
    }

    interface AppointmentHandler{
        fun addNewAppointment(item: Appointment)
    }

    lateinit var appointmentHandler : AppointmentHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is AppointmentHandler){
            appointmentHandler = context
        } else {
            throw RuntimeException("The Activity is not implementing the AppointmentHandler interface.")
        }
    }

    private lateinit var spinnerCustomerName : Spinner
    private lateinit var spinnerService : Spinner
    private lateinit var etPrice : EditText
    private lateinit var cbCompleted: CheckBox
    private lateinit var tvDate : TextView
    private lateinit var tvTimeStart : TextView
    private lateinit var btnAddTime : Button

    private var names = mutableListOf<String>()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("New Appointment")

        val dialogView = requireActivity().layoutInflater.inflate(
            R.layout.appoinment_dialog, null
        )

        initDialogViews(dialogView)
        dialogBuilder.setView(dialogView)

        initSpinnerCustomer()
        initSpinnerService()
        initTimeAddButton()

        dialogBuilder.setPositiveButton("Ok") {
                _, _ ->
        }
        dialogBuilder.setNegativeButton("Cancel") {
                _, _ ->
        }

        return dialogBuilder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)

        positiveButton.setOnClickListener {
            try {
                // case: there's no customer yet
                if (names.size < 1 || names.contains("")) {
                    val view: TextView = spinnerCustomerName.selectedView as TextView
                    view.error = getString(R.string.empty_message_error)
                    view.setTextColor(ContextCompat.getColor(context as MainActivity, R.color.colorRed))
                    view.text = getString(R.string.empty_customer)
                } else if (TextUtils.isEmpty(etPrice.text)) { // case: price field is missing
                    etPrice.error = getString(R.string.empty_message_error)
                } else { // everything OK
                    handleAddNewAppointment()
                    dialog!!.dismiss()
                }
            } catch (e : Exception) {
                etPrice.error = e.message
            }
        }
    }

    private fun initDialogViews(dialogView: View) {
        spinnerCustomerName = dialogView.spinnerCustomer
        spinnerService = dialogView.spinnerService
        etPrice = dialogView.etPrice
        cbCompleted = dialogView.cbAlreadyCompleted
        tvDate = dialogView.tvAppDate
        tvTimeStart = dialogView.tvAppTimeStart
        btnAddTime = dialogView.btnAddTime

        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(FragmentCalendar.KEY_APPOINTMENT_DATE)) {
            val date = arguments.getSerializable(FragmentCalendar.KEY_APPOINTMENT_DATE) as String
            tvDate.text = date
        }
    }

    private fun initSpinnerCustomer() {
        Thread {
            val customerList = CustomerDatabase.getInstance(context!!).customerDao().getAllCustomers()

            names = mutableListOf()

            for (customer in customerList) {
                names.add(customer.name)
            }

            names.sort()

            (context!! as MainActivity).runOnUiThread {
                if (names.size == 0) names.add("")
                val customerCategoryAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, names)
                customerCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCustomerName.adapter = customerCategoryAdapter
            }
        }.start()
    }


    private fun initSpinnerService() {
        val serviceCategoryAdapter = ArrayAdapter.createFromResource(
            context!!,
            R.array.serviceArray,
            android.R.layout.simple_spinner_item
        )
        serviceCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerService.adapter = serviceCategoryAdapter
        spinnerService.setSelection(0)
    }

    private fun initTimeAddButton() {
        btnAddTime.setOnClickListener {
            // Use the current time as the default values for the picker
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(context, timePickerListener, hour, minute, true)
            timePickerDialog.show()
        }
    }

    private fun handleAddNewAppointment() {
        val serviceCategories = resources.getStringArray(R.array.serviceArray)
        var service : String = serviceCategories[spinnerService.selectedItemPosition]

        appointmentHandler.addNewAppointment(
            Appointment(
                null,
                names[spinnerCustomerName.selectedItemPosition],
                service,
                etPrice.text.toString(),
                cbCompleted.isChecked,
                tvDate.text.toString(),
                tvTimeStart.text.toString()
            )
        )
    }

    private val timePickerListener = TimePickerDialog.OnTimeSetListener{ timePicker: TimePicker, hourOfDay: Int, minuteOfHour: Int ->
        val hour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
        val minute = if (minuteOfHour < 10) "0$minuteOfHour" else minuteOfHour.toString()
        tvTimeStart.text = getString(R.string.time_start, hour, minute)
    }

}