package ait.hu.appointmentbooking.fragments.customer.data

import androidx.room.*

@Dao
interface CustomerDAO {
    @Query("SELECT * FROM customerItem")
    fun getAllCustomers(): List<Customer>

    @Insert
    fun insertNewCustomer(item: Customer) : Long

    @Delete
    fun deleteCustomer(item: Customer)

    @Update
    fun updateCustomer(item: Customer)
}