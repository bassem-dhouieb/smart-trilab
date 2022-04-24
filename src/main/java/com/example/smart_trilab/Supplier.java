package com.example.smart_trilab;

public class Supplier {

    int id;
    String society,name,email,country,address,phone,fax,tax_id,trade_register,reference,bank,rib;

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getTax_id() {
        return tax_id;
    }

    public void setTax_id(String tax_id) {
        this.tax_id = tax_id;
    }

    public String getTrade_register() {
        return trade_register;
    }

    public void setTrade_register(String trade_register) {
        this.trade_register = trade_register;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public Supplier(int id, String society, String name, String email, String country, String address, String phone, String fax, String tax_id, String trade_register, String bank, String rib) {
        this.id = id;
        this.society = society;
        this.name = name;
        this.email = email;
        this.country = country;
        this.address = address;
        this.phone = phone;
        this.fax = fax;
        this.tax_id = tax_id;
        this.trade_register = trade_register;
        this.reference = String.format("SP%05d",id);
        this.bank = bank;
        this.rib = rib;
    }

}
