package com.example.organizzeclone.helper

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateUtil {
    companion object{
        fun dataAtual(): String{
            val current = LocalDate.now()
            val dataFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val data = current.format(dataFormat)
            return data.toString()
        }

        fun mesAnoData(data: String): String{
            val retornoData = data.split("/")
            return retornoData[1]+retornoData[2]
        }
    }
}