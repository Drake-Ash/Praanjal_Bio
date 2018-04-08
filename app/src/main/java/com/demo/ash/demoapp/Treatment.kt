package com.demo.ash.demoapp

import android.graphics.Bitmap
import android.media.Image
import java.util.*

/**
 * Created by ashwin on 3/4/2018.
 */
class Treatment(
        var date: Date,
        var Domain: Int,
        var idT: Int,
        var tDoc: String,
        var tComment: String,
        var imgcount: Int
) {
    constructor():this(
            date = Date(),
            Domain = 0,
            idT = 0,
            tDoc = "",
            tComment = "",
            imgcount = 0){}
}