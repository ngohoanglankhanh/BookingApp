package ait.hu.appointmentbooking.fragments.calendar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "appointmentItem")
data class Appointment(
    @PrimaryKey(autoGenerate = true) var appointmentItemId : Long?,
    @ColumnInfo(name = "customerName")var customerName: String,
    @ColumnInfo(name = "service") var service: String,
    @ColumnInfo(name = "price") var price: String,
    @ColumnInfo(name = "completed") var completed: Boolean,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "timeStart") var timeStart: String
) : Serializable