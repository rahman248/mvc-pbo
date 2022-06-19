/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.repo.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Rahman S
 */
public class Report implements BaseModel {

    private int paymentId;
    private String employee;
    private String studentNisn;
    private String studentName;
    private String kelasName;
    private Date paymentDate;
    private String paymentMonth;
    private String paymentYears;
    private String years;
    private String nominal;
    private String totalAmount;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getStudentNisn() {
        return studentNisn;
    }

    public void setStudentNisn(String studentNisn) {
        this.studentNisn = studentNisn;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getKelasName() {
        return kelasName;
    }

    public void setKelasName(String kelasName) {
        this.kelasName = kelasName;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(String paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public String getPaymentYears() {
        return paymentYears;
    }

    public void setPaymentYears(String paymentYears) {
        this.paymentYears = paymentYears;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public void save() {

    }

    @Override
    public void delete() {
    }

    @Override
    public void fillFromResultSet(ResultSet result) throws SQLException {
        setPaymentId(result.getInt("petugas_id"));
        setEmployee(result.getString("name"));
        setStudentNisn(result.getString("nisn"));
        setStudentName(result.getString("nama"));
        setKelasName(result.getString("kelas_name"));
        setPaymentDate(result.getDate("tgl_bayar"));
        setPaymentMonth(result.getString("bulan_bayar"));
        setPaymentYears(result.getString("tahun_bayar"));
        setYears(result.getString("tahun"));
        setNominal(result.getString("nominal"));
        setTotalAmount(result.getString("jumlah_bayar"));
    }

}
