package ait.hu.appointmentbooking.fragments.customer

import ait.hu.appointmentbooking.R
import ait.hu.appointmentbooking.fragments.customer.data.Customer
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.customer_dialog.view.*

class CustomerDialog : DialogFragment() {

    companion object {
        const val TAG = "CUSTOMER_DIALOG"
    }

    interface CustomerHandler{
        fun addNewCustomer(item: Customer)
    }

    lateinit var customerHandler: CustomerHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CustomerHandler){
            customerHandler = context
        } else {
            throw RuntimeException("The Activity is not implementing the CustomerHandler interface.")
        }
    }

    private lateinit var etCustomerName : EditText
    private lateinit var etCustomerPhoneNumber : EditText
    private lateinit var etCustomerEmail : EditText
    private lateinit var etCustomerNote : EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("New Customer")

        val dialogView = requireActivity().layoutInflater.inflate(
            R.layout.customer_dialog, null
        )

        initDialogViews(dialogView)
        dialogBuilder.setView(dialogView)

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
                if (!TextUtils.isEmpty(etCustomerName.text)
                    && !TextUtils.isEmpty(etCustomerPhoneNumber.text)
                    && !TextUtils.isEmpty(etCustomerEmail.text)) {

                    if (isValidEmailAddress(etCustomerEmail.text.toString())) {
                        handleAddNewCustomer()
                        dialog!!.dismiss()
                    }else {
                        etCustomerEmail.error = getString(R.string.error_invalid_email)
                    }

                } else {
                    showEmptyTextInputError()
                }
            } catch (e : Exception) {
                etCustomerName.error = e.message
                etCustomerPhoneNumber.error = e.message
                etCustomerEmail.error = e.message
            }
        }
    }

    private fun handleAddNewCustomer() {
        val note = if (!TextUtils.isEmpty(etCustomerNote.text)) etCustomerNote.text.toString() else ""
        customerHandler.addNewCustomer(
            Customer(
                null,
                etCustomerName.text.toString(),
                etCustomerPhoneNumber.text.toString(),
                etCustomerEmail.text.toString(),
                note
            )
        )
    }

    private fun initDialogViews(dialogView: View) {
        etCustomerName = dialogView.etCustomerName
        etCustomerPhoneNumber = dialogView.etCustomerPhoneNumber
        etCustomerEmail = dialogView.etCustomerEmail
        etCustomerNote = dialogView.etCustomerNote
    }

    private fun showEmptyTextInputError() {
        when {
            TextUtils.isEmpty(etCustomerName.text) -> {
                etCustomerName.error = getString(R.string.empty_message_error)
            }
            TextUtils.isEmpty(etCustomerPhoneNumber.text) -> {
                etCustomerPhoneNumber.error = getString(R.string.empty_message_error)
            }
            TextUtils.isEmpty(etCustomerEmail.text) -> {
                etCustomerEmail.error = getString(R.string.empty_message_error)
            }
        }
    }

    private fun isValidEmailAddress(text: String) : Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
    }
}