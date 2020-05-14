package ait.hu.appointmentbooking

import ait.hu.appointmentbooking.fragments.calendar.AppointmentDialog
import ait.hu.appointmentbooking.fragments.calendar.FragmentCalendar
import ait.hu.appointmentbooking.fragments.calendar.data.Appointment
import ait.hu.appointmentbooking.fragments.calendar.data.AppointmentDatabase
import ait.hu.appointmentbooking.fragments.customer.CustomerDetailsDialog
import ait.hu.appointmentbooking.fragments.customer.data.Customer
import ait.hu.appointmentbooking.fragments.customer.CustomerDialog
import ait.hu.appointmentbooking.fragments.customer.FragmentCustomer
import ait.hu.appointmentbooking.fragments.customer.data.CustomerDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CustomerDialog.CustomerHandler, AppointmentDialog.AppointmentHandler {

    companion object {
        const val KEY_DETAILS = "CUSTOMER_DETAILS"
    }

    private lateinit var fragmentCalendar: FragmentCalendar
    private lateinit var fragmentCustomer: FragmentCustomer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation_bar.setOnNavigationItemSelectedListener(myOnNavigationItemSelectedListener)
        showFragmentByTag(FragmentCalendar.TAG, false)
    }

    public fun showFragmentByTag(tag: String, toBackStack: Boolean) {
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(tag)

        if (fragment == null) {
            when (tag) {
                FragmentCalendar.TAG -> {
                    fragment = FragmentCalendar()
                    fragmentCalendar = fragment
                }
                FragmentCustomer.TAG -> {
                    fragment = FragmentCustomer()
                    fragmentCustomer = fragment
                }
            }
        }

        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragmentContainer, fragment, tag)
            if (toBackStack) {
                ft.addToBackStack(null)
            }
            ft.commit()
        }
    }

    private val myOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_calendar -> {
                showFragmentByTag(FragmentCalendar.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_customer -> {
                showFragmentByTag(FragmentCustomer.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    public fun showCustomerDetails(customer: Customer) {
        val customerDetailsDialog = CustomerDetailsDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_DETAILS, customer)
        customerDetailsDialog.arguments = bundle

        customerDetailsDialog.show(supportFragmentManager, "DETAILS_DIALOG")
    }

    override fun addNewCustomer(item: Customer) {
        Thread {
            item.shoppingItemId = CustomerDatabase.getInstance(this).customerDao().insertNewCustomer(item)

            runOnUiThread {
                fragmentCustomer.addNewCustomer(item)
            }
        }.start()
    }

    override fun addNewAppointment(item: Appointment) {
        Thread {
            item.appointmentItemId = AppointmentDatabase.getInstance(this).appointmentDao().insertNewAppointment(item)

            runOnUiThread {
                fragmentCalendar.addNewAppointment(item)
            }
        }.start()
    }

}

