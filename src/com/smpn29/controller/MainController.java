/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smpn29.controller;

import com.smpn29.helper.Helpers;
import com.smpn29.repo.dao.PaymentDao;
import com.smpn29.repo.dao.SppDao;
import com.smpn29.repo.dao.StudentDao;
import com.smpn29.repo.database.Auth;
import com.smpn29.repo.database.DbConnections;
import com.smpn29.repo.model.Payment;
import com.smpn29.repo.model.Petugas;
import com.smpn29.repo.model.Spp;
import com.smpn29.repo.model.Student;
import com.smpn29.view.ui.MainView;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author ASTANEW2
 */
public class MainController {

    Petugas user = Auth.getUser();

    StudentDao studentDao = new StudentDao();
    PaymentDao pembayaranDao = new PaymentDao();
    SppDao sppDao = new SppDao();

    ArrayList<Spp> sppList = new ArrayList<>();

    String[] listTextSPP = new String[0];

    Student selectedStudent = null;

    public void setupVisibility(MainView view) {
        if (user.getLevel().equals("petugas")) {
            view.manageDataPanel.setVisible(false);

        }

        view.lblTitle.setText(user.getName() + "@" + user.getLevel());
    }

    public void searchUser(MainView view) {
        String nisn = view.searchField.getText();

        selectedStudent = studentDao.find(nisn);

        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(null, "Siswa tidak dapat ditemukan");

            return;
        }

        view.labelUserNISN.setText(selectedStudent.getNisn());
        view.labelUserNIS.setText(selectedStudent.getNis());
        view.labelUserNama.setText(selectedStudent.getNama());
        view.labelUserKelas.setText(selectedStudent.getKelas() != null ? concatString(selectedStudent.getKelas().getKelasName(), selectedStudent.getKelas().getKelasCode(), "") : "-");
        view.labelUserAlamat.setText(selectedStudent.getAlamat());
        view.labelUserTelepone.setText(selectedStudent.getNoTelp());
        view.labelUserSPP.setText(selectedStudent.getSpp() != null ? selectedStudent.getSpp().getNominal() + "" : "-");

        this.populateTabelSiswa(view);

        view.labelNameEntri.setText(selectedStudent.getNama() + " \n " + selectedStudent.getNisn());
        view.paymentDate.setDate(new Date());
        view.paymentAmount.setText(selectedStudent.getSpp() != null ? selectedStudent.getSpp().getNominal() + "" : "");

        view.rightPanel.setSelectedIndex(1);
    }

    public void populateTabelSiswa(MainView view) {
        DefaultTableModel tableModel = (DefaultTableModel) view.studentPaymentHistory.getModel();

        tableModel.setNumRows(0);

        if (selectedStudent == null) {
            return;
        }

        ArrayList<Payment> paymentStudentList = selectedStudent.getPayment();

        paymentStudentList.forEach((payment) -> {
            Object[] row = new Object[6];

            row[0] = numToString(payment.getPaymentId());
            row[1] = payment.getPetugas() != null ? payment.getPetugas().getName() : "-";
            row[2] = payment.getTglBayar();
            row[3] = payment.getBulanBayar();
            row[4] = payment.getTahunBayar();
            row[5] = payment.getJumlahBayar();

            tableModel.addRow(row);
        });
    }

    public void loadTabelPembayaran(MainView view) {
        ArrayList<Payment> paymentStudentList = pembayaranDao.all();

        DefaultTableModel tableModel = (DefaultTableModel) view.paymentTable.getModel();

        tableModel.setNumRows(0);

        paymentStudentList.forEach((payment) -> {
            Object[] row = new Object[8];

            row[0] = numToString(payment.getPaymentId());
            row[1] = payment.getPetugas() != null ? payment.getPetugas().getName() : "-";
            row[2] = payment.getStudent() != null ? concatString(payment.getStudent().getNama(), payment.getStudent().getKelas().getKelasName(), payment.getStudent().getKelas().getKelasCode()) : "-";
            row[3] = payment.getTglBayar();
            row[4] = payment.getBulanBayar();
            row[5] = payment.getTahunBayar();
            row[6] = payment.getSpp() != null ? payment.getSpp().getTahun() : "-";
            row[7] = payment.getJumlahBayar();

            tableModel.addRow(row);
        });
    }

    public void resetEntri(MainView view) {
        view.paymentDate.setDate(null);
        view.paymentAmount.setText("");
    }

    public void addEntry(MainView view) {
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(null, "Harap pilih siswa terlebih dahulu melalui panel di sebelah kiri");

            return;
        }

        Date payDate = view.paymentDate.getDate();
        int nominalPayment = Integer.parseInt("0" + view.paymentAmount.getText());

        if (payDate == null || nominalPayment == 0) {
            JOptionPane.showMessageDialog(null, "Harap lengkapi data pembayaran!");

            return;
        }

        java.sql.Date sqlDate = new java.sql.Date(payDate.getTime());

        Calendar cal = Calendar.getInstance();

        cal.setTime(payDate);

        String payMonth = Helpers.getTextBulan(view.paymentMonth.getMonth());
        String payYears = view.paymentYear.getYear() + "";

        Payment existingPembayaran = pembayaranDao.whereOne(String.format("nisn = '%s' AND bulan_bayar = '%s' AND tahun_bayar = '%s'", selectedStudent.getNisn(), payMonth, payYears), Payment.class);

        if (existingPembayaran != null) {
            JOptionPane.showMessageDialog(null, String.format("Siswa telah membayar SPP untuk bulan %s tahun %s!", payMonth, payYears));

            return;
        }

        Spp sppStudent = selectedStudent.getSpp();

        if (nominalPayment != sppStudent.getNominal()) {
            int response = JOptionPane.showConfirmDialog(null, "Jumlah bayar berbeda dari nominal SPP yang telah ditentukan (" + sppStudent.getNominal() + "), yakin ingin melanjutkan entri?");

            if (response != JOptionPane.YES_OPTION) {
                return;
            }
        } else {
            int response = JOptionPane.showConfirmDialog(null, "Pembayaran yang sudah tercatat tidak dapat diubah maupun dihapus, pastikan semua data transaksi pembayaran sudah benar, jika sudah yakin maka tekan \"Yes\".", "Konfirmasi Entri?", JOptionPane.WARNING_MESSAGE);

            if (response != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // everyting is clear
        Payment mPayment = new Payment();

        mPayment.setIdPetugas(user.getIdPetugas());
        mPayment.setNisn(selectedStudent.getNisn());
        mPayment.setTglBayar(sqlDate);
        mPayment.setBulanBayar(payMonth);
        mPayment.setTahunBayar(payYears);
        mPayment.setSppId(selectedStudent.getSppId());
        mPayment.setJumlahBayar(nominalPayment);

        mPayment.save();

        this.loadTabelPembayaran(view);
        this.populateTabelSiswa(view);
    }

    public void loadTabelSiswa(MainView view) {
        ArrayList<Student> listSiswa = studentDao.all();

        DefaultTableModel tableModel = (DefaultTableModel) view.studentTable.getModel();

        tableModel.setNumRows(0);

        listSiswa.forEach((siswa) -> {
            Object[] row = new Object[7];

            row[0] = siswa.getNisn();
            row[1] = siswa.getNis();
            row[2] = siswa.getNama();
            row[3] = siswa.getKelas() != null ? concatString(siswa.getKelas().getKelasName(), siswa.getKelas().getKelasCode(), null) : "-";
            row[4] = siswa.getAlamat();
            row[5] = siswa.getNoTelp();

            Spp spp = siswa.getSpp();

            row[6] = spp != null ? spp.getTahun() : "-";

            tableModel.addRow(row);
        });
    }

    public void onTableSiswaClick(MainView view) {
        int selectedRow = view.studentTable.getSelectedRow();

        selectedStudent = studentDao.find(view.studentTable.getValueAt(selectedRow, 0).toString());

        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(null, "Siswa tidak dapat ditemukan");

            return;
        }

        view.searchField.setText(selectedStudent.getNisn());

        view.labelUserNISN.setText(selectedStudent.getNisn());
        view.labelUserNIS.setText(selectedStudent.getNis());
        view.labelUserNama.setText(selectedStudent.getNama());
        view.labelUserKelas.setText(selectedStudent.getKelas() != null ? concatString(selectedStudent.getKelas().getKelasName(), selectedStudent.getKelas().getKelasCode(), "") : "-");
        view.labelUserAlamat.setText(selectedStudent.getAlamat());
        view.labelUserTelepone.setText(selectedStudent.getNoTelp());
        view.labelUserSPP.setText(selectedStudent.getSpp() != null ? selectedStudent.getSpp().getNominal() + "" : "-");

        this.populateTabelSiswa(view);

       
        view.labelNameEntri.setText(selectedStudent.getNama());
        view.labelNISNEntri.setText(selectedStudent.getNisn());
        view.paymentDate.setDate(new Date());
        view.paymentAmount.setText(selectedStudent.getSpp() != null ? selectedStudent.getSpp().getNominal() + "" : "");

        view.rightPanel.setSelectedIndex(2);
    }

    public void refresh(MainView view) {
        this.loadTabelPembayaran(view);
        this.loadTabelSiswa(view);

        this.setupDropdown(view);
    }

    public void setupDropdown(MainView view) {
        sppList = sppDao.all();

        listTextSPP = sppList.stream().map((Spp spp) -> {
            return spp.getTahun();
        }).toArray(String[]::new);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(listTextSPP);

        view.selectionYearReport.setModel(model);
    }

    public void generateLaporan(MainView view) {
        File dir1 = new File(".");
        String dirr = "";
        try {
            Map<String, Object> params = new HashMap();;
            params.put("tahun", view.selectionYearReport.getSelectedItem());

            Connection con = DbConnections.createConnection();
            Statement statement = con.createStatement();

            dirr = dir1.getCanonicalPath() + "/src/assets/";
            String query = String.format("SELECT p.payment_id, pt.name, sw.nisn, sw.nama, k.kelas_name, p.tgl_bayar, p.bulan_bayar, p.tahun_bayar, s.tahun, s.nominal, p.jumlah_bayar FROM payment AS p LEFT JOIN petugas AS pt ON p.petugas_id = pt.petugas_id LEFT JOIN student AS sw ON p.nisn = sw.nisn LEFT JOIN kelas AS k ON sw.kelas_id = k.kelas_id LEFT JOIN spp AS s ON p.spp_id = s.spp_id WHERE s.tahun = '%s' GROUP BY p.payment_id ORDER BY p.tgl_bayar DESC,p.payment_id DESC", params);
            
            System.out.print("DATA +query");
            JasperDesign disain = JRXmlLoader.load(dirr + "Leaf_Green.jrxml");
            JasperReport nilaiLaporan = JasperCompileManager.compileReport(disain);
            ResultSet rs = statement.executeQuery(query);
            JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(rs);
            JasperPrint cetak = JasperFillManager.fillReport(nilaiLaporan, new HashMap(), resultSetDataSource);
            JasperViewer.viewReport(cetak);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static String concatString(String T, String T2, String T3) {
        return T + " " + T + " ";
    }

    static String numToString(int num) {

        return String.format("INV/%06d/", num);
    }

    static int counting(ArrayList<Payment> listPaymentSiswa) {
        int count = 0;
        for (int i = 1; i < listPaymentSiswa.size(); i++) {
            count = i;
        }
        return count;
    }

}
