package com.programmerpro.cricketlivestreaming.data.api

import java.lang.reflect.Type
import com.google.gson.InstanceCreator
import com.programmerpro.cricketlivestreaming.data.models.MoviesResponseItem

class MoviesResponseItemInstanceCreator : InstanceCreator<MoviesResponseItem> {
    override fun createInstance(type: Type): MoviesResponseItem {
        return MoviesResponseItem(
            "", "", null, 0, "", "", "", 0, "", "", "", 0, "", "", "", 0.0f, 0, 0, "", "", "")
    }
}