package ait.hu.appointmentbooking.fragments.touch

interface TouchHelperCallback {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}