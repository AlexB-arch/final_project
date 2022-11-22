package com.example.afinal.data

import com.example.afinal.NEW_RECEIPT_ID
import java.util.*

class ReceiptEntity (
    var receiptId: Int,
    var date: Date,
    var contents : String,
        ){
    constructor() : this(NEW_RECEIPT_ID, Date(), "")
    constructor(date: Date, contents: String) : this(NEW_RECEIPT_ID, date, contents)
}
