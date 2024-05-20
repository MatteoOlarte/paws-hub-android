package com.software3.paws_hub_android.core.util

class PhoneFormat (private val prefix: Int = 57) {
    fun format(phoneNumber: String): String {
        val cd = phoneNumber.filter { it.isDigit() }
        if (cd.length != 10) {
            return cd
        }

        val formattedPhoneNumber = StringBuilder()
        formattedPhoneNumber.append("+$prefix")
        formattedPhoneNumber.append(" ")
        formattedPhoneNumber.append("(")
        formattedPhoneNumber.append(cd.substring(0, 3))
        formattedPhoneNumber.append(")")
        formattedPhoneNumber.append(" ")
        formattedPhoneNumber.append(cd.substring(3, 6))
        formattedPhoneNumber.append("-")
        formattedPhoneNumber.append(cd.substring(6))
        return formattedPhoneNumber.toString()
    }
}