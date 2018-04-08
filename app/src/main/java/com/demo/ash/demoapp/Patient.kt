package com.demo.ash.demoapp

/**
 * Created by ashwin on 3/4/2018.
 */
class Patient(
        var breed: String,
        var gender: Int,
        var age: String,
        var key: String
) {
    constructor():this("",0, "", "")
}