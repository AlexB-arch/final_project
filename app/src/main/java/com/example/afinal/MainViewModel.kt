package com.example.afinal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.afinal.data.ReceiptEntity
import com.example.afinal.data.SampleData

class MainViewModel : ViewModel() {

    val receiptList = MutableLiveData<List<ReceiptEntity>>()

    init {
        receiptList.value = SampleData.getReceipts()
    }
}