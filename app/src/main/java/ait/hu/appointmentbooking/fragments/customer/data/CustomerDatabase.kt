package ait.hu.appointmentbooking.fragments.customer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Customer::class), version = 1)
abstract class CustomerDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDAO

    companion object {
        private var INSTANCE: CustomerDatabase? = null

        fun getInstance(context: Context): CustomerDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    CustomerDatabase::class.java, "customer.db")
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