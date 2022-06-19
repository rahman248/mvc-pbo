/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.controller;

import com.smpn29.repo.dao.KelasDao;
import com.smpn29.repo.dao.SppDao;
import com.smpn29.repo.dao.StudentDao;
import com.smpn29.repo.model.Kelas;
import com.smpn29.repo.model.Spp;
import com.smpn29.repo.model.Student;
import com.smpn29.view.ui.StudentView;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Rahman S
 */
public class StudentController {
    StudentDao dao;
    KelasDao kelasDao;
    SppDao sppDao;

    ArrayList<Kelas> listKelas;
    ArrayList<Spp> listSPP;

    String[] listTextSPP;
    String[] listNamaKelas;

    boolean isEdit = false;

    public StudentController() {
        dao = new StudentDao();
        kelasDao = new KelasDao();
        sppDao = new SppDao();

        listKelas = kelasDao.all();
        listSPP = sppDao.all();

        listTextSPP = listSPP.stream().map((Spp spp) -> {
            return spp.getTahun();
        }).toArray(String[]::new);

        listNamaKelas = listKelas.stream().map((Kelas kelas) -> {
            return kelas.getKelasName();
        }).toArray(String[]::new);
    }

    public void loadTable(StudentView view) {
        ArrayList<Student> listStudent = dao.all();

        DefaultTableModel tableModel = (DefaultTableModel) view.getTableSiswa().getModel();

        tableModel.setNumRows(0);

        listStudent.forEach((siswa) -> {
            Object[] row = new Object[7];

            row[0] = siswa.getNisn();
            row[1] = siswa.getNis();
            row[2] = siswa.getNama();
            row[3] = siswa.getKelas() != null ? siswa.getKelas().getKelasName(): "-";
            row[4] = siswa.getAlamat();
            row[5] = siswa.getNoTelp();

            Spp spp = siswa.getSpp();

            row[6] = spp != null ? spp.getTahun() : "-";

            tableModel.addRow(row);
        });
    }

    public void setupSelectKelas(StudentView view) {

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(listNamaKelas);

        view.getSelectKelas().setModel(model);
    }

    public void setupSelectSPP(StudentView view) {

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(listTextSPP);

        view.getSelectSPP().setModel(model);
    }

    public void save(StudentView view) {
        Student siswa;

        String nisn = view.getTextNISN().getText();
        Student existingSiswa = dao.find(nisn);

        if (existingSiswa == null) {
            System.out.println("Create new siswa");
            siswa = new Student();
        } else {
            if (isEdit == false) {
                JOptionPane.showMessageDialog(null, "Siswa dengan NISN tersebut sudah terdaftar!");

                return;
            }

            System.out.println("Update existing siswa");
            siswa = dao.find(nisn);
        }

        siswa.setNisn(view.getTextNISN().getText());
        siswa.setNis(view.getTextNIS().getText());
        siswa.setNama(view.getTextNama().getText());
        siswa.setAlamat(view.getTextAlamat().getText());
        siswa.setNoTelp(view.getTextNoTelp().getText());

        siswa.setKelasId(getSelectedKelas(view).getKelasId());
        siswa.setSppId(getSelectedSPP(view).getSppId());

        siswa.save();

        // adding new siswa
        if (existingSiswa == null) {
            this.reset(view);
        }

        this.loadTable(view);
    }

    public Kelas getSelectedKelas(StudentView view) {
        // get id kelas from kelas jComboBox selectKelas
        int selectedIndex = view.getSelectKelas().getSelectedIndex();

        Kelas selectedKelas = listKelas.get(selectedIndex);

        return selectedKelas;
    }

    public Spp getSelectedSPP(StudentView view) {
        // get id kelas from kelas jComboBox selectKelas
        int selectedIndex = view.getSelectSPP().getSelectedIndex();

        Spp selectedSPP = listSPP.get(selectedIndex);

        return selectedSPP;
    }

    public void reset(StudentView view) {
        view.getTextNISN().setText("");
        view.getTextNIS().setText("");
        view.getTextNama().setText("");
        view.getSelectKelas().setSelectedIndex(0);
        view.getTextAlamat().setText("");
        view.getTextNoTelp().setText("");
        view.getSelectSPP().setSelectedIndex(0);

        view.getTextNISN().setEnabled(true);

        isEdit = false;

    }

    public void delete(StudentView view) {
        if (view.getTextNISN().getText().equals("")) {
            JOptionPane.showMessageDialog(view, "Pilih siswa yang akan dihapus");

            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Menghapus siswa akan mempengaruhi data pembayaran! apakah anda yakin?", "Konfirmasi Hapus", JOptionPane.WARNING_MESSAGE);

        if (response != JOptionPane.OK_OPTION) {
            return;
        }

        dao.find(view.getTextNISN().getText()).delete();

        this.reset(view);
        this.loadTable(view);
    }

    public void onTableClick(StudentView view) {
        JTable tabelSiswa = view.getTableSiswa();

        int selectedRow = tabelSiswa.getSelectedRow();

        Student student = dao.find(tabelSiswa.getValueAt(selectedRow, 0).toString());

        view.getTextNISN().setText(student.getNisn());
        view.getTextNIS().setText(student.getNis());
        view.getTextNama().setText(student.getNama());
        view.getTextAlamat().setText(student.getAlamat());
        view.getTextNoTelp().setText(student.getNoTelp());

        view.getSelectKelas().setSelectedItem(student.getKelas() != null ? student.getKelas().getKelasName() : "");

        Spp sppSiswa = student.getSpp();

        view.getSelectSPP().setSelectedItem(sppSiswa != null ? sppSiswa.getTahun() : "");

        view.getTextNISN().setEnabled(false);
        isEdit = true;
    }
}
