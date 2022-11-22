package com.example.afinal.data

import java.util.*

class SampleData {

    companion object {
        private val test1 = "This is a test"
        private val test2 = "This is another test"
        private val test3 = "This is a third test but a little longer just to see how it looks"

        // Counts how long ago the receipt was created
        private fun getDate(daysAgo: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, daysAgo)
            return calendar.time
        }

        // Creates a list of receipts
        fun getReceipts(): List<ReceiptEntity> {
            return listOf(
                ReceiptEntity(getDate(0), test1),
                ReceiptEntity(getDate(-1), test2),
                ReceiptEntity(getDate(-2), test3)
            )
        }
    }
}