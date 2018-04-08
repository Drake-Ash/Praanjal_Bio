package com.demo.ash.demoapp

/**
 * Created by ashwin on 3/4/2018.
 */
class User(
        var phonenum: String,
        var fname: String,
        var lname: String,
        var username: String,
        var address: String,
        var postal_code: String
){
    constructor():this("","","","","","")
}
