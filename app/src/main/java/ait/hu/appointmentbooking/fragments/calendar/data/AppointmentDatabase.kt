package ait.hu.appointmentbooking.fragments.calendar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Appointment::class), version = 1)
abstract class AppointmentDatabase : RoomDatabase() {

    abstract fun appointmentDao(): AppointmentDAO

    companion object {
        private var INSTANCE: AppointmentDatabase? = null

        fun getInstance(context: Context): AppointmentDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppointmentDatabase::class.java, "appointment.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}