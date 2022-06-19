package com.smpn29.repo.model;

import com.smpn29.repo.dao.PaymentDao;
import com.smpn29.repo.dao.PetugasDao;
import com.smpn29.repo.dao.SppDao;
import com.smpn29.repo.dao.StudentDao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Rahman S
 */
public class Payment implements BaseModel {


    private int paymentId;
    private int petugasId;
    private String nisn;
    private Date tglBayar;
    private String bulanBayar;
    private String tahunBayar;
    private int sppId;
    private int jumlahBayar;

    public int getPaymentId() {
        return paymentId;
    }

    public void setIdPembayaran(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getIdPetugas() {
        return petugasId;
    }

    public void setIdPetugas(int petugasId) {
        this.petugasId = petugasId;
    }

    public String getNisn() {
        return nisn;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public Date getTglBayar() {
        return tglBayar;
    }

    public void setTglBayar(Date tglBayar) {
        this.tglBayar = tglBayar;
    }

    public String getBulanBayar() {
        return bulanBayar;
    }

    public void setBulanBayar(String bulanBayar) {
        this.bulanBayar = bulanBayar;
    }

    public String getTahunBayar() {
        return tahunBayar;
    }

    public void setTahunBayar(String tahunBayar) {
        this.tahunBayar = tahunBayar;
    }

    public int getSppId() {
        return sppId;
    }

    public void setSppId(int sppId) {
        this.sppId = sppId;
    }

    public int getJumlahBayar() {
        return jumlahBayar;
    }

    public void setJumlahBayar(int jumlahBayar) {
        this.jumlahBayar = jumlahBayar;
    }


    @Override
    public void save() {
        if (this.paymentId == 0) {
            new PaymentDao().add(this);
        } else {
            new PaymentDao().update(this);
        }
    }

    @Override
    public void delete() {
        if (this.paymentId != 0) {
            new PaymentDao().delete(this);
        }
    }

    @Override
    public void fillFromResultSet(ResultSet result) throws SQLException {
        setIdPembayaran(result.getInt("payment_id"));
        setIdPetugas(result.getInt("petugas_id"));
        setNisn(result.getString("nisn"));
        setTglBayar(result.getDate("tgl_bayar"));
        setBulanBayar(result.getString("bulan_bayar"));
        setTahunBayar(result.getString("tahun_bayar"));
        setSppId(result.getInt("spp_id"));
        setJumlahBayar(result.getInt("jumlah_bayar"));

    }

    public Petugas getPetugas() {
        return new PetugasDao().whereOne("petugas_id = " + petugasId, Petugas.class);
    }

    public Student getStudent() {
        return new StudentDao().whereOne("nisn = " + nisn, Student.class);
    }

    public Spp getSpp() {
        return new SppDao().whereOne("spp_id = " + sppId, Spp.class);
    }

}
