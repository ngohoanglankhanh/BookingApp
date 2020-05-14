package ait.hu.appointmentbooking.fragments.calendar.data

import androidx.room.*

@Dao
interface AppointmentDAO {
    @Query("SELECT * FROM appointmentItem")
    fun getAllAppointments(): List<Appointment>

    @Insert
    fun insertNewAppointment(item: Appointment) : Long // returns id

    @Delete
    fun deleteAppointment(item: Appointment)

    @Update
    fun updateAppointment(item: Appointment)
}