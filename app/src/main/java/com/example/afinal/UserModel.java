package com.example.afinal;

public class UserModel {
    String firstName;
    String lastName;
    String email;
    String password;
    String phone;
    String address;
    String city;
    String state;
    String zip;
    String country;
    long perDiem;
    long totalExpenses;
    long totalMiles;
    long totalDays;
    long totalPerDiem;
    long totalReimbursement;

    public UserModel() {
    }

    public UserModel(String firstName, String lastName, String email, String password, String phone, String address, String city, String state, String zip, String country, long perDiem, long totalExpenses, long totalMiles, long totalDays, long totalPerDiem, long totalReimbursement) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.perDiem = perDiem;
        this.totalExpenses = totalExpenses;
        this.totalMiles = totalMiles;
        this.totalDays = totalDays;
        this.totalPerDiem = totalPerDiem;
        this.totalReimbursement = totalReimbursement;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getPerDiem() {
        return perDiem;
    }

    public void setPerDiem(long perDiem) {
        this.perDiem = perDiem;
    }

    public long getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(long totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public long getTotalMiles() {
        return totalMiles;
    }

    public void setTotalMiles(long totalMiles) {
        this.totalMiles = totalMiles;
    }

    public long getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(long totalDays) {
        this.totalDays = totalDays;
    }

    public long getTotalPerDiem() {
        return totalPerDiem;
    }

    public void setTotalPerDiem(long totalPerDiem) {
        this.totalPerDiem = totalPerDiem;
    }

    public long getTotalReimbursement() {
        return totalReimbursement;
    }

    public void setTotalReimbursement(long totalReimbursement) {
        this.totalReimbursement = totalReimbursement;
    }

    @Override
    public String toString() {
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
                '}';
    }
}
