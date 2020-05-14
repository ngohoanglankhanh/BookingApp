package ait.hu.appointmentbooking.fragments.customer

import ait.hu.appointmentbooking.MainActivity
import ait.hu.appointmentbooking.R
import ait.hu.appointmentbooking.fragments.customer.data.Customer
import ait.hu.appointmentbooking.fragments.customer.data.CustomerDatabase
import ait.hu.appointmentbooking.fragments.touch.TouchHelperCallback
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.customer_row.view.*
import java.util.*

class CustomerRVAdapter : RecyclerView.Adapter<CustomerRVAdapter.ViewHolder>, TouchHelperCallback {

    private var customerList = mutableListOf<Customer>()
    private val context: Context

    constructor(context : Context, customerList: List<Customer>) {
        this.context = context
        this.customerList.addAll(customerList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.customer_row, parent, false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCustomer = customerList[position]

        holder.ivCustomerIcon.setImageResource(R.drawable.icon_customer)
        holder.tvCustomerName.text = currentCustomer.name
        holder.btnDelete.setOnClickListener {
            deleteCustomer(holder.adapterPosition)
        }

        holder.customerCard.setOnClickListener {
            (context as MainActivity).showCustomerDetails(customerList[holder.adapterPosition])
        }
    }

    private fun deleteCustomer(position: Int) {
        Thread {
            CustomerDatabase.getInstance(context).customerDao().deleteCustomer(customerList[position])

            (context as MainActivity).runOnUiThread {
                customerList.removeAt(position)
                notifyItemRemoved(position)
            }
        }.start()
    }

    fun addCustomer(customer: Customer) {
        customerList.add(customer)
        notifyItemInserted(customerList.lastIndex)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCustomerIcon : ImageView = itemView.ivCustomerIcon
        val tvCustomerName : TextView = itemView.tvCustomerName
        val btnDelete : Button = itemView.btnDelete
        val customerCard : CardView = itemView.customerCard
    }

    override fun onDismissed(position: Int) {
        deleteCustomer(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(customerList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }


}