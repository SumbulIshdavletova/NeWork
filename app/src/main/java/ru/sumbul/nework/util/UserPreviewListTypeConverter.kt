package ru.sumbul.nework.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ru.sumbul.nework.events.domain.model.UserPreview

class UserPreviewListTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun toJson(userPreview: List<UserPreview?>?): String? {
        return gson.toJson(userPreview)
    }

    @TypeConverter
    fun fromJson(json: String?): List<UserPreview?> {
        return GsonBuilder().create().fromJson(json, Array<UserPreview>::class.java).toList() ?: emptyList()
    }

}