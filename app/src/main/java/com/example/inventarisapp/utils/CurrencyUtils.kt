package com.example.inventarisapp.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    fun formatToRupiah(amount: Number): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(amount)
    }
}