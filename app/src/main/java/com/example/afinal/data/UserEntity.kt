package com.example.afinal.data

// User class to store user information in Firebase Database
class UserEntity {
    var firstName: String? = null
    var middleName: String? = null
    var lastName: String? = null
    var email: String? = null
    var phone: String? = null
    var address_line_1: String? = null
    var address_line_2: String? = null
    var city: String? = null
    var state: String? = null
    var zip: String? = null

    // Empty constructor
    constructor()

    // Constructor with parameters
    constructor(
        firstName: String?,
        middleName: String?,
        lastName: String?,
        email: String?,
        phone: String?,
        address_line_1: String?,
        address_line_2: String?,
        city: String?,
        state: String?,
        zip: String?
    ) {
        this.firstName = firstName
        this.middleName = middleName
        this.lastName = lastName
        this.email = email
        this.phone = phone
        this.address_line_1 = address_line_1
        this.address_line_2 = address_line_2
        this.city = city
        this.state = state
        this.zip = zip
    }
}