package com.example.afinal.data

// User class to store user information in Firebase Database
class UserEntity {
    var email: String? = null
    var username: String? = null

    // Empty constructor
    constructor()

    // Constructor with parameters
    constructor(email: String, username: String) {
        this.email = email
        this.username = username
    }
}