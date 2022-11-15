package com.example.afinal

class UserModel {
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var password: String? = null
    var phone: String? = null
    var address: String? = null
    var city: String? = null
    var state: String? = null
    var zip: String? = null
    var country: String? = null
    var perDiem: Long = 0
    var totalExpenses: Long = 0
    var totalMiles: Long = 0
    var totalDays: Long = 0
    var totalPerDiem: Long = 0
    var totalReimbursement: Long = 0

    constructor() {}
    constructor(
        firstName: String?,
        lastName: String?,
        email: String?,
        password: String?,
        phone: String?,
        address: String?,
        city: String?,
        state: String?,
        zip: String?,
        country: String?,
        perDiem: Long,
        totalExpenses: Long,
        totalMiles: Long,
        totalDays: Long,
        totalPerDiem: Long,
        totalReimbursement: Long
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.password = password
        this.phone = phone
        this.address = address
        this.city = city
        this.state = state
        this.zip = zip
        this.country = country
        this.perDiem = perDiem
        this.totalExpenses = totalExpenses
        this.totalMiles = totalMiles
        this.totalDays = totalDays
        this.totalPerDiem = totalPerDiem
        this.totalReimbursement = totalReimbursement
    }

    override fun toString(): String {
        return "UserModel{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                ", perDiem=" + perDiem +
                ", totalExpenses=" + totalExpenses +
                ", totalMiles=" + totalMiles +
                ", totalDays=" + totalDays +
                ", totalPerDiem=" + totalPerDiem +
                ", totalReimbursement=" + totalReimbursement +
                '}'
    }
}