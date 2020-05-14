package ait.hu.appointmentbooking.fragments.customer

import ait.hu.appointmentbooking.MainActivity
import ait.hu.appointmentbooking.R
import ait.hu.appointmentbooking.fragments.calendar.FragmentCalendar
import ait.hu.appointmentbooking.fragments.customer.data.Customer
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.customer_details.view.*


class CustomerDetailsDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Customer")

        val dialogView = requireActivity().layoutInflater.inflate(
            R.layout.customer_details, null
        )

        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(MainActivity.KEY_DETAILS)) {
            initViews(arguments, dialogView)
        }

        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton("Ok") {
                _, _ ->
        }
        dialogBuilder.setNegativeButton("Cancel") {
                _, _ ->
        }

        return dialogBuilder.create()
    }

    private fun initViews(arguments: Bundle, dialogView: View) {
        val customer = arguments.getSerializable(MainActivity.KEY_DETAILS) as Customer

        dialogView.tvName.text = getString(R.string.name_s, customer.name)
        dialogView.tvPhone.text = getString(R.string.phone_s, customer.phoneNumber)
        dialogView.tvEmail.text = getString(R.string.email_s, customer.emailAddress)
        dialogView.tvNote.text = "Note: " + customer.note

        dialogView.ivPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + customer.phoneNumber)
            context!!.startActivity(intent)
        }

        dialogView.ivMessage.setOnClickListener {
            val uri = Uri.parse("smsto:" + customer.phoneNumber)
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", "Here goes your message...")
            startActivity(intent)
        }

        dialogView.ivNewAppointment.setOnClickListener {
            (context as MainActivity).showFragmentByTag(FragmentCalendar.TAG, true)
            dialog!!.dismiss()
        }
    }
}