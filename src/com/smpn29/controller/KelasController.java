/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.controller;

import com.smpn29.repo.dao.KelasDao;
import com.smpn29.repo.model.Kelas;
import com.smpn29.view.ui.KelasView;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Rahman S
 */
public class KelasController {
    
     KelasDao dao;

    public KelasController() {
        dao = new KelasDao();
    }

    public void loadTable(KelasView view) {
        ArrayList<Kelas> listKelas = dao.all();

        DefaultTableModel tableModel = (DefaultTableModel) view.getTableKelas().getModel();

        tableModel.setRowCount(0);

        listKelas.forEach((kelas) -> {
            Object[] row = new Object[3];

            row[0] = kelas.getKelasId();
            row[1] = kelas.getKelasName();
            row[2] = kelas.getKelasCode();

            tableModel.addRow(row);
        });
    }

    public void save(KelasView view) {
        Kelas kelas;

        String id = view.getTextID().getText();

        if (id.equals("")) {
            System.out.println("Create new kelas");
            kelas = new Kelas();
        } else {
            System.out.println("Update existing kelas");
            kelas = dao.find(Integer.parseInt(id));
        }

        kelas.setKelasName(view.getNamaKelas().getText());
        kelas.setKelasCode(view.getTextCode().getText());

        kelas.save();
        
        if (id.equals("")) this.reset(view);

        this.loadTable(view);
    }

    public void reset(KelasView view) {
        view.getTextID().setText("");
        view.getNamaKelas().setText("");
        view.getTextCode().setText("");
    }

    public void delete(KelasView view) {
        if (view.getTextID().getText().equals("")) {
            JOptionPane.showMessageDialog(view, "Pilih kelas yang akan dihapus");

            return;
        }
        
        int response = JOptionPane.showConfirmDialog(null, "Menghapus kelas akan mempengaruhi data siswa! apakah anda yakin?", "Konfirmasi Hapus", JOptionPane.WARNING_MESSAGE);

        if (response != JOptionPane.OK_OPTION) {
            return;
        }

        int id = Integer.parseInt(view.getTextID().getText());

        dao.find(id).delete();

        this.reset(view);
        this.loadTable(view);
    }

    public void setFormFieldFromClickedTable(KelasView view) {
        int selectedRow = view.getTableKelas().getSelectedRow();

        view.getTextID().setText(view.getTableKelas().getValueAt(selectedRow, 0).toString());
        view.getNamaKelas().setText(view.getTableKelas().getValueAt(selectedRow, 1).toString());
        view.getTextCode().setText(view.getTableKelas().getValueAt(selectedRow, 2).toString());

    }
    
}
