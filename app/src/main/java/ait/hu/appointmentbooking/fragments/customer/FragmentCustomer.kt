package ait.hu.appointmentbooking.fragments.customer

import ait.hu.appointmentbooking.MainActivity
import ait.hu.appointmentbooking.R
import ait.hu.appointmentbooking.fragments.customer.data.Customer
import ait.hu.appointmentbooking.fragments.customer.data.CustomerDatabase
import ait.hu.appointmentbooking.fragments.touch.RecyclerListTouchCallback
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_customer.view.*

class FragmentCustomer : Fragment() {
    companion object {
        const val TAG = "FRAGMENT_CUSTOMER"
    }

    private lateinit var customerListAdapter: CustomerRVAdapter
    private lateinit var btnAddNewCustomer : Button
    private lateinit var rvCustomerList : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_customer, container,false)

        btnAddNewCustomer = rootView.btnAddCustomer
        rvCustomerList = rootView.rvCustomer

        initRecyclerView()

        btnAddNewCustomer.setOnClickListener {
            CustomerDialog().show(childFragmentManager, CustomerDialog.TAG)
        }

        activity!!.title = "Customer"

        return rootView
    }

    private fun initRecyclerView() {
        Thread {
            val customerList = CustomerDatabase.getInstance(context as MainActivity).customerDao().getAllCustomers()

            (context as MainActivity).runOnUiThread {
                customerListAdapter = CustomerRVAdapter(context as MainActivity, customerList)
                rvCustomerList.adapter = customerListAdapter
                addItemTouchHelper()
                addItemDivider()
            }
        }.start()
    }

    private fun addItemTouchHelper() {
        val touchCallbackList = RecyclerListTouchCallback(customerListAdapter)
        val itemTouchHelper = ItemTouchHelper(touchCallbackList)
        itemTouchHelper.attachToRecyclerView(rvCustomerList)
    }

    private fun addItemDivider() {
        val itemDecoration = DividerItemDecoration(
            activity, DividerItemDecoration.VERTICAL
        )
        rvCustomerList.addItemDecoration(itemDecoration)
    }

    public fun addNewCustomer(item: Customer) {
        customerListAdapter.addCustomer(item)
    }
}