/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.controller;

import com.smpn29.repo.dao.PetugasDao;
import com.smpn29.repo.model.Petugas;
import com.smpn29.view.ui.PetugasView;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Rahman S
 */
public class PetugasController {
    PetugasDao dao;
    
    public PetugasController() {
        dao = new PetugasDao();
    }
    
    public void loadTable(PetugasView view) {
        ArrayList<Petugas> listPetugas = dao.all();
        
        DefaultTableModel model = (DefaultTableModel) view.getTablePetugas().getModel();
        
        model.setNumRows(0);
        
        listPetugas.forEach((petugas) -> {
            Object[] row = new Object[5];
            
            row[0] = petugas.getIdPetugas();
            row[1] = petugas.getUsername();
            row[2] = petugas.getPassword();
            row[3] = petugas.getName();
            row[4] = petugas.getLevel();
            
            model.addRow(row);
        });
    }
    
    public void save(PetugasView view) {
        Petugas petugas;
        
        String id = view.getTextID().getText();
        
        if (id.equals("")) {
            System.out.println("Create new petugas");
            petugas = new Petugas();
        } else {
            System.out.println("Update existing petugas");
            petugas = dao.find(Integer.parseInt(id));
        }
        
        petugas.setUsername(view.getTextUsername().getText());
        petugas.setPassword(view.getTextPassword().getText());
        petugas.setName(view.getTextNama().getText());
        petugas.setLevel(view.getSelectLevel().getSelectedItem().toString());
        
        petugas.save();
        
        // only reset if creating new user
        if (id.equals("")) this.reset(view);
        
        this.loadTable(view);
    }
    
    public void setFormFieldFromClickedTable(PetugasView view) {
        int selectedRow = view.getTablePetugas().getSelectedRow();
        
        String id = view.getTablePetugas().getValueAt(selectedRow, 0).toString();
        String username = view.getTablePetugas().getValueAt(selectedRow, 1).toString();
        String password = view.getTablePetugas().getValueAt(selectedRow, 2).toString();
        String name = view.getTablePetugas().getValueAt(selectedRow, 3).toString();
        String level = view.getTablePetugas().getValueAt(selectedRow, 4).toString();
        
        view.getTextID().setText(id);
        view.getTextUsername().setText(username);
        view.getTextPassword().setText(password);
        view.getTextNama().setText(name);
        view.getSelectLevel().setSelectedItem(level);
    }
    
    public void reset(PetugasView view) {
        view.getTextID().setText("");
        view.getTextUsername().setText("");
        view.getTextPassword().setText("");
        view.getTextNama().setText("");
        view.getSelectLevel().setSelectedItem("");
    }
    
    public void delete(PetugasView view) {
        if (view.getTextID().getText().equals("")) {
            JOptionPane.showMessageDialog(view, "Pilih petugas yang akan dihapus");
            
            return;
        }
        
        int response = JOptionPane.showConfirmDialog(null, "Menghapus petugas akan mempengaruhi data pembayaran! apakah anda yakin?", "Konfirmasi Hapus", JOptionPane.WARNING_MESSAGE);

        if (response != JOptionPane.OK_OPTION) {
            return;
        }
        
        int petugasId = Integer.parseInt(view.getTextID().getText());
        
        dao.find(petugasId).delete();
        
        this.reset(view);
        this.loadTable(view);
    }
}
