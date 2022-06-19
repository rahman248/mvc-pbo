package com.smpn29.repo.dao;

import com.smpn29.repo.model.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : Rahman S
 * @Project : smpn29jkt
 * @Create : Saturday, 18 June 2022 / 17:55
 * @Package : com.smpn29.repo.dao
 **/
public class PaymentDao extends BaseDao {

    public PaymentDao() {
        this.table = "payment";
        this.primaryKey = "payment_id";
    }

    public void delete(Payment Payment) {
        super.delete(String.valueOf(Payment.getPaymentId()));
    }

    public void update(Payment Payment) {
        Map<String, Object> data = new HashMap<>();

        data.put("petugas_id", Payment.getIdPetugas());
        data.put("nisn", Payment.getNisn());
        data.put("tgl_bayar", Payment.getTglBayar());
        data.put("bulan_bayar", Payment.getBulanBayar());
        data.put("tahun_bayar", Payment.getTahunBayar());
        data.put("spp_id", Payment.getSppId());
        data.put("jumlah_bayar", Payment.getJumlahBayar());

        super.update(data, Payment.getPaymentId());
    }

    public int add(Payment Payment) {
        Map<String, Object> data = new HashMap<>();

        data.put("petugas_id", Payment.getIdPetugas());
        data.put("nisn", Payment.getNisn());
        data.put("tgl_bayar", Payment.getTglBayar());
        data.put("bulan_bayar", Payment.getBulanBayar());
        data.put("tahun_bayar", Payment.getTahunBayar());
        data.put("spp_id", Payment.getSppId());
        data.put("jumlah_bayar", Payment.getJumlahBayar());

        String id = super.add(data);

        return Integer.parseInt(id);
    }

    public Payment find(int id) {
        return super.find(String.valueOf(id), Payment.class);
    }

    public ArrayList<Payment> all() {
        return super.all(Payment.class, "tgl_bayar DESC, payment_id DESC");
    }
}
