/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.controller;

import com.smpn29.repo.dao.SppDao;
import com.smpn29.repo.model.Spp;
import com.smpn29.view.main.SppView;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Rahman S
 */
public class SppController {
    
     SppDao dao;

    public SppController() {
        this.dao = new SppDao();
    }

    public void loadTable(SppView view) {
        ArrayList<Spp> listSpp = dao.all();

        DefaultTableModel tableModel = (DefaultTableModel) view.getSppTable().getModel();

        tableModel.setNumRows(0);

        listSpp.forEach((spp) -> {
            Object[] row = new Object[3];

            row[0] = spp.getSppId();
            row[1] = spp.getTahun();
            row[2] = spp.getNominal();

            tableModel.addRow(row);
        });

    }

    public void save(SppView view) {
        Spp spp;

        int id = Integer.parseInt("0" + view.getTextID().getText());

        Spp existingSPP = dao.find(id);

        if (id == 0 || existingSPP == null) {
            spp = new Spp();
        } else {
            spp = existingSPP;
        }

        spp.setNominal(Integer.parseInt(view.getTextNominal().getText()));
        spp.setTahun(view.getTextTahun().getText());

        spp.save();

        if (id == 0 || existingSPP == null) {
            this.reset(view);
        }

        this.loadTable(view);
    }

    public void reset(SppView view) {
        view.getTextID().setText("");
        view.getTextTahun().setText("");
        view.getTextNominal().setText("");
    }

    public void delete(SppView view) {
        if (view.getTextID().getText().equals("")) {
            JOptionPane.showMessageDialog(view, "Pilih SPP yang akan dihapus");

            return;
        }

        int id = Integer.parseInt(view.getTextID().getText());

        Spp spp = dao.find(id);

        if (spp.getSiswa().size() > 0) {
            JOptionPane.showMessageDialog(view, "SPP ini digunakan oleh " + spp.getSiswa().size() + " siswa, harap ubah atau hapus siswa yang menggunakan jenis SPP ini terlebih dahulu!");

            return;
        }
        
        int response = JOptionPane.showConfirmDialog(null, "Menghapus SPP akan mempengaruhi data siswa! apakah anda yakin?", "Konfirmasi Hapus", JOptionPane.WARNING_MESSAGE);

        if (response != JOptionPane.OK_OPTION) {
            return;
        }

        spp.delete();

        this.reset(view);
        this.loadTable(view);
    }

    public void tableClicked(SppView view) {
        int selectedRow = view.getSppTable().getSelectedRow();

        int selectedId = Integer.parseInt(view.getSppTable().getValueAt(selectedRow, 0).toString());

        Spp spp = dao.find(selectedId);

        view.getTextID().setText("" + spp.getSppId());
        view.getTextTahun().setText("" + spp.getTahun());
        view.getTextNominal().setText("" + spp.getNominal());

    }
    
}
