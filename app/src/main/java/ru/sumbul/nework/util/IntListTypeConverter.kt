package ru.sumbul.nework.util


import androidx.room.TypeConverter

class IntListTypeConverter {

    @TypeConverter
    fun fromString(string: String?): List<Int> {
        val result = ArrayList<Int>()
        val split =string?.replace("[","")?.replace("]","")?.replace(" ","")?.split(",")
        if (split != null) {
            for (n in split) {
                try {
                    result.add(n.toInt())
                } catch (e: Exception) {

                }
            }
        }
        return result
    //    return string?.split(",")
    }

    @TypeConverter
    fun toString(list: List<Int>?): String? {
        return list?.joinToString(",")
    }
    // fun fromListIntToString(intList: List<Int>): String = intList.toString()
}