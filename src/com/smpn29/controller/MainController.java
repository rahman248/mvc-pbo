/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.controller;

import com.smpn29.helper.Helpers;
import com.smpn29.repo.dao.PaymentDao;
import com.smpn29.repo.dao.ReportsDao;
import com.smpn29.repo.dao.SppDao;
import com.smpn29.repo.dao.StudentDao;
import com.smpn29.repo.database.DbConnections;
import com.smpn29.repo.model.Payment;
import com.smpn29.repo.model.Petugas;
import com.smpn29.repo.model.Spp;
import com.smpn29.repo.model.Student;
import com.smpn29.view.main.MainView;
import java.io.File;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * @author Rahman S
 */
public class MainController {

    Petugas user = new Petugas();

    StudentDao studentDao = new StudentDao();
    ReportsDao reportDao = new ReportsDao();
    PaymentDao paymentDao = new PaymentDao();
    SppDao sppDao = new SppDao();

    ArrayList<Spp> listSpp = new ArrayList<>();

    String[] listTextSPP = new String[0];

    Student selectedStudent = null;

    

    public  void searchUser(MainView view) {
        String nisn = view.searchField.getText();

        selectedStudent = studentDao.find(nisn);

        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(null, "Siswa tidak dapat ditemukan");

            return;
        }

        view.labelUserNISN.setText(selectedStudent.getNisn());
        view.labelUserNIS.setText(selectedStudent.getNis());
        view.labelUserNama.setText(selectedStudent.getNama());
        view.labelUserKelas.setText(selectedStudent.getKelas() != null ? selectedStudent.getKelas().getKelasName() : "-");
        view.labelUserAlamat.setText(selectedStudent.getAlamat());
        view.labelUserTelepone.setText(selectedStudent.getNoTelp());
        view.labelUserSPP.setText(selectedStudent.getSpp() != null ? selectedStudent.getSpp().getNominal() + "" : "-");

        this.populateTabelSiswa(view);

        view.labelNameNISNEntri.setText(selectedStudent.getNama() + " - " + selectedStudent.getNisn());
        view.paymentDate.setDate(new Date());
        view.paymentAmount.setText(selectedStudent.getSpp() != null ? selectedStudent.getSpp().getNominal() + "" : "");

        view.rightPanel.setSelectedIndex(2);
    }

    public void populateTabelSiswa(MainView view) {
        DefaultTableModel tableModel = (DefaultTableModel) view.allHistoryPayment.getModel();

        tableModel.setNumRows(0);

        if (selectedStudent == null) {
            return;
        }

         ArrayList<Payment> listPaymentSiswa = selectedStudent.getPayment();

        listPaymentSiswa.forEach((payment) -> {
            Object[] row = new Object[6];

            row[0] = numToString( payment.getPaymentId());
            row[1] = payment.getPetugas() != null ? payment.getPetugas().getName() : "-";
            row[2] = payment.getTglBayar();
            row[3] = payment.getBulanBayar();
            row[4] = payment.getTahunBayar();
            row[5] = payment.getJumlahBayar();

            tableModel.addRow(row);
        });
    }

    public void loadTabelPembayaran(MainView view) {
        ArrayList<Payment>  listPaymentSiswa = paymentDao.all();

        DefaultTableModel tableModel = (DefaultTableModel) view.allHistoryPayment.getModel();

        tableModel.setNumRows(0);

        listPaymentSiswa.forEach((payment) -> {
            Object[] row = new Object[8];
            System.out.println("Data " + numToString( payment.getPaymentId()));
            row[0] = numToString( payment.getPaymentId());
            row[1] = payment.getPetugas() != null ? payment.getPetugas().getName() : "-";
            row[2] = payment.getStudent() != null ? payment.getStudent().getNama() : "-";
            row[3] = payment.getTglBayar();
            row[4] = payment.getBulanBayar();
            row[5] = payment.getTahunBayar();
            row[6] = payment.getSpp() != null ? payment.getSpp().getTahun() : "-";
            row[7] = payment.getJumlahBayar();

            tableModel.addRow(row);
        });
    }
    
    static String numToString(int num){
     
        return String.format("%06d", num);
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

        Date mPaymentDate = view.paymentDate.getDate();
        int mPaymentAmount = Integer.parseInt("0" + view.paymentAmount.getText());

        if (mPaymentDate == null || mPaymentAmount == 0) {
            JOptionPane.showMessageDialog(null, "Harap lengkapi data pembayaran!");

            return;
        }

        java.sql.Date sqlDate = new java.sql.Date(mPaymentDate.getTime());

        Calendar cal = Calendar.getInstance();

        cal.setTime(mPaymentDate);

        String bulanBayar = Helpers.getTextBulan(view.paymentMonth.getMonth());
        String tahunBayar = view.paymentYear.getYear() + "";

        Payment existingPayment = paymentDao.whereOne(String.format("nisn = '%s' AND bulan_bayar = '%s' AND tahun_bayar = '%s'", selectedStudent.getNisn(), bulanBayar, tahunBayar), Payment.class);

        if (existingPayment != null) {
            JOptionPane.showMessageDialog(null, String.format("Siswa telah membayar SPP untuk bulan %s tahun %s!", bulanBayar, tahunBayar));

            return;
        }

        Spp sppStudent = selectedStudent.getSpp();

        if (mPaymentAmount != sppStudent.getNominal()) {
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
        Payment payment = new Payment();

        payment.setIdPetugas(user.getIdPetugas());
        payment.setNisn(selectedStudent.getNisn());
        payment.setTglBayar(sqlDate);
        payment.setBulanBayar(bulanBayar);
        payment.setTahunBayar(tahunBayar);
        payment.setSppId(selectedStudent.getSppId());
        payment.setJumlahBayar(mPaymentAmount);

        payment.save();
        this.loadTabelPembayaran(view);
        this.populateTabelSiswa(view);
    }

    public void loadTabelSiswa(MainView view) {
        ArrayList<Student> listStudents = studentDao.all();

        DefaultTableModel tableModel = (DefaultTableModel) view.studentTable.getModel();

        tableModel.setNumRows(0);

        listStudents.forEach((student) -> {
            Object[] row = new Object[7];

            row[0] = student.getNisn();
            row[1] = student.getNis();
            row[2] = student.getNama();
            row[3] = student.getKelas() != null ? student.getKelas().getKelasName() : "-";
            row[4] = student.getAlamat();
            row[5] = student.getNoTelp();

            Spp spp = student.getSpp();

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
        view.labelUserKelas.setText(selectedStudent.getKelas() != null ? selectedStudent.getKelas().getKelasName() : "-");
        view.labelUserAlamat.setText(selectedStudent.getAlamat());
        view.labelUserTelepone.setText(selectedStudent.getNoTelp());
        view.labelUserSPP.setText(selectedStudent.getSpp() != null ? selectedStudent.getSpp().getNominal() + "" : "-");

        this.populateTabelSiswa(view);

        view.labelNameNISNEntri.setText(selectedStudent.getNama() + " - " + selectedStudent.getNisn());
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
        listSpp = sppDao.all();

        listTextSPP = listSpp.stream().map((Spp spp) -> {
            return spp.getTahun();
        }).toArray(String[]::new);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(listTextSPP);

        view.selectionYearReport.setModel(model);
    }

    public void generateLaporan(MainView view) {
        
        
         
        File dir1 = new File(".");
        String dirr = "";
        
        try {
           
            
            Connection con = DbConnections.createConnection();
            Statement statement = con.createStatement();
            
            dirr = dir1.getCanonicalPath()+"/src/assets/";
            String query = "SELECT p.payment_id, pt.name, sw.nisn, sw.nama, k.kelas_name, p.tgl_bayar, p.bulan_bayar, p.tahun_bayar, s.tahun, s.nominal, p.jumlah_bayar FROM payment AS p LEFT JOIN petugas AS pt ON p.petugas_id = pt.petugas_id LEFT JOIN student AS sw ON p.nisn = sw.nisn LEFT JOIN kelas AS k ON sw.kelas_id = k.kelas_id LEFT JOIN spp AS s ON p.spp_id = s.spp_id GROUP BY p.payment_id ORDER BY p.tgl_bayar DESC,p.payment_id DESC";
                    
                  
            JasperDesign disain = JRXmlLoader.load(dirr+"Leaf_Green.jrxml");
            JasperReport nilaiLaporan = JasperCompileManager.compileReport(disain);
            ResultSet rs = statement.executeQuery(query);
            JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(rs);
            JasperPrint cetak = JasperFillManager.fillReport(nilaiLaporan, new HashMap(), resultSetDataSource);
            
            JasperViewer.viewReport(cetak);  
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak\n"+e);
        }
        

       /* try {
            Map<String, Object> params = new HashMap();

            params.put("tahun_spp", view.selectionYearReport.getSelectedItem());

            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("Leaf_Green.jrxml"));

            JasperPrint jp = JasperFillManager.fillReport(jasperReport, params, DbConnections.createConnection());
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

}
