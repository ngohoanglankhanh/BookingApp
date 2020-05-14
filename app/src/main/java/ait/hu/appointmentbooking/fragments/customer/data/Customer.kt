package ait.hu.appointmentbooking.fragments.customer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "customerItem")
data class Customer(
    @PrimaryKey(autoGenerate = true) var shoppingItemId : Long?,
    @ColumnInfo(name = "name")var name: String,
    @ColumnInfo(name = "phoneNumber") var phoneNumber: String,
    @ColumnInfo(name = "emailAddress") var emailAddress: String,
    @ColumnInfo(name = "note") var note: String
) : Serializable