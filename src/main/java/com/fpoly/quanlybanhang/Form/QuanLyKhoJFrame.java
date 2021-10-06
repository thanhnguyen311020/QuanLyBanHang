/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.Form;

import com.fpoly.quanlybanhang.DAO.HoaDonChiTietDAO;
import com.fpoly.quanlybanhang.DAO.LoaiSPDAO;
import com.fpoly.quanlybanhang.DAO.NhieuNhapCTDAO;
import com.fpoly.quanlybanhang.DAO.PhieuNhapDAO;
import com.fpoly.quanlybanhang.DAO.SanPhamDAO;
import com.fpoly.quanlybanhang.Model.HoaDonChiTiet;
import com.fpoly.quanlybanhang.Model.KhachHang;
import com.fpoly.quanlybanhang.Model.LoaiSanPham;
import com.fpoly.quanlybanhang.Model.NhanVien;
import com.fpoly.quanlybanhang.Model.PhieuNhap;
import com.fpoly.quanlybanhang.Model.PhieuNhapChiTiet;
import com.fpoly.quanlybanhang.Model.SanPham;
import com.fpoly.quanlybanhang.helper.DateHelper;
import com.fpoly.quanlybanhang.helper.DialogHelper;
import com.fpoly.quanlybanhang.helper.ShareHelper;
import com.fpoly.quanlybanhang.helper.ValidateHelper;
import com.fpoly.quanlybanhang.helper.jdbcHelper;
import java.awt.Image;
import java.io.File;
import java.sql.ResultSet;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class QuanLyKhoJFrame extends javax.swing.JInternalFrame {

    LoaiSPDAO lDAO = new LoaiSPDAO();
    SanPhamDAO spDAO = new SanPhamDAO();

    /**
     * Creates new form DonHangJframe
     */
    public QuanLyKhoJFrame() {
        initComponents();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI bi = (BasicInternalFrameUI) this.getUI();
        bi.setNorthPane(null);
        LoadLoaiSP();
        Load_SP();
        loadComboxSP_Loai();

        txtNhanVien.setText(ShareHelper.USER.getMaNV());
        txtNgayNhap.setText(DateHelper.toString(DateHelper.now()));
        loadPhieuNhap();
        setStatusPhieuNhap(true);
        setStatusCTPhieu(false);
        //loadCTPhieu();
        loadComboBoxSP();
        init();
    }
    
    void init(){
        btnAddCT.setEnabled(false);
        btnDelCT.setEnabled(false);
        btnUpdateCT.setEnabled(false);
        btnNewCT.setEnabled(false);
    }

    void LoadLoaiSP() {
        DefaultTableModel model = (DefaultTableModel) tblLoaiSP.getModel();

        model.setRowCount(0);
        try {
            List<LoaiSanPham> list = lDAO.select();
            int i = 0;
            for (LoaiSanPham lsp : list) {
                i++;
                model.addRow(new Object[]{
                    i, lsp.getMaLoai(), lsp.getTenLoai()
                });
            }
            model.fireTableDataChanged();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();

        }
    }

    void insert_LoaiSP() {
        try {
            LoaiSanPham lsp = getLoai();
            lDAO.insert_LoaiSP(lsp);
            this.LoadLoaiSP();
            DialogHelper.alert(this, "Thêm Thành Công");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void update_LoaiSP() {
        if (DialogHelper.confirm(this, "Bạn có muốn cập nhập không")) {
            try {
                LoaiSanPham lsp = getLoai();
                lDAO.insert_LoaiSP(lsp);
                this.LoadLoaiSP();
                DialogHelper.alert(this, "Thêm Thành Công");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    void delete_Delete_LoaiSP() {
        if (DialogHelper.confirm(this, "Bạn Có Muốn Xóa Không")) {
            try {
                String id = txtMaLoai.getText();
                lDAO.delete_LoaSP(id);
                this.LoadLoaiSP();
                DialogHelper.alert(this, "Đã Xóa Thành Công");
            } catch (Exception e) {
                e.printStackTrace();
                DialogHelper.alert(this, "Xóa thất bại");
            }
        }
    }

    LoaiSanPham getLoai() {
        LoaiSanPham lsp = new LoaiSanPham();
        lsp.setMaLoai(txtMaLoai.getText());
        lsp.setTenLoai(txtTenLoai.getText());
        return lsp;
    }

    void setLoai(LoaiSanPham lsp) {
        txtTenLoai.setText(lsp.getTenLoai());
        txtMaLoai.setText(lsp.getMaLoai());
    }
    int index = 0;

    void editLoai() {
        try {
            String id = (String) tblLoaiSP.getValueAt(this.index, 1);
            System.out.println("id= " + id);
            LoaiSanPham lsp = lDAO.findByID(id);
            if (lsp != null) {
                this.setLoai(lsp);
                this.setStatusLoai(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void setStatusLoai(boolean isertable) {
        btnAddLoai.setEnabled(isertable);
        btnUpdateLoai.setEnabled(!isertable);
        btnDelLoai.setEnabled(!isertable);
    }

    void loadSPbyIDLoai(String id) {
        try {
            DefaultTableModel model = (DefaultTableModel) tblThamChieuSP.getModel();
            model.setRowCount(0);
            List<SanPham> list = spDAO.selectID(id);
            for (SanPham sp : list) {
                model.addRow(new Object[]{
                    sp.getMaSP(), sp.getTenSP(), id
                });
            }
            model.fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Lỗi Truy vấn dữ liệu");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtMaSP = new javax.swing.JTextField();
        txtTenSP = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtGiaNhap = new javax.swing.JTextField();
        txtSoLuong = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtGiaBan = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNghiChu = new javax.swing.JTextField();
        lblAnh = new javax.swing.JLabel();
        btnSelectImage = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jLabel27 = new javax.swing.JLabel();
        btnNew = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        cbbLoaiSP = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLoaiSP = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblThamChieuSP = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        txtMaLoai = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTenLoai = new javax.swing.JTextField();
        btnAddLoai = new javax.swing.JButton();
        btnUpdateLoai = new javax.swing.JButton();
        btnDelLoai = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblPhieuNhap = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblCTPhieu = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtMaPhieu = new javax.swing.JTextField();
        txtNhanVien = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtNgayNhap = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtNhaPhanPhoi = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtGhiChu = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        btnNewPhieu = new javax.swing.JButton();
        btnAddPhieu = new javax.swing.JButton();
        btnUpdatePhieu = new javax.swing.JButton();
        btnXoaPhieu = new javax.swing.JButton();
        btnDelCT = new javax.swing.JButton();
        btnUpdateCT = new javax.swing.JButton();
        btnAddCT = new javax.swing.JButton();
        btnNewCT = new javax.swing.JButton();
        txtPhieuTongTien = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtPhieuDonGia = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtPhieuSoLuong = new javax.swing.JTextField();
        txtPhieuGhiChu = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtMaPhieuCT = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtMaPhieuCT1 = new javax.swing.JTextField();
        cboMaSPPhieu = new javax.swing.JComboBox<>();

        setPreferredSize(new java.awt.Dimension(1046, 705));

        jPanel1.setBackground(new java.awt.Color(52, 152, 219));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon("D:\\Fpoly\\Fall 2020 (4)\\Dự Án 1\\Icon\\1x\\outline_domain_white_36dp.png")); // NOI18N
        jLabel5.setText("QUẢN LÝ KHO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(861, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 52, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addGap(7, 7, 7)))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã SP", "Tên SP", "Số Lượng", "Giá Nhập", "Giá Bán", "Mã Loại", "Ghi Chú"
            }
        ));
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSanPham);

        jLabel1.setText("Mã SP:");

        jLabel2.setText("Tên SP:");

        txtSoLuong.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        txtSoLuong.setEnabled(false);

        jLabel3.setText("Số Lượng");

        jLabel4.setText("Giá Nhập:");

        jLabel6.setText("Giá Bán");

        jLabel7.setText("Loại:");

        jLabel8.setText("Ghi Chú");

        lblAnh.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblAnh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAnh.setText("Ảnh");
        lblAnh.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnSelectImage.setText("Chọn Ảnh");
        btnSelectImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectImageActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Tìm Kiếm");

        jRadioButton1.setText("Giá");

        jRadioButton2.setText("Số Lượng");

        jRadioButton3.setText("jRadioButton1");

        jRadioButton4.setText("jRadioButton1");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setText("Lọc Theo");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearch)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jRadioButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jRadioButton2))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jRadioButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 4, Short.MAX_VALUE)
                                .addComponent(jRadioButton4))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(jLabel27)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton4))
                .addGap(62, 62, 62))
        );

        btnNew.setBackground(new java.awt.Color(52, 152, 219));
        btnNew.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setText("Làm Mới");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(52, 152, 219));
        btnAdd.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Thêm Mới");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(52, 152, 219));
        btnUpdate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setText("Cập Nhập");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnRemove.setBackground(new java.awt.Color(52, 152, 219));
        btnRemove.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setText("Xóa");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        cbbLoaiSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(35, 35, 35)
                                .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnNew)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAdd))
                            .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel6)))
                                .addGap(35, 35, 35)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNghiChu, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                                    .addComponent(cbbLoaiSP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(btnUpdate)
                                .addGap(39, 39, 39)
                                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnSelectImage))
                    .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(43, 43, 43))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(cbbLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtNghiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSelectImage))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 7, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Sản Phẩm", jPanel2);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        tblLoaiSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "STT", "Mã Loại", "Tên Loại"
            }
        ));
        tblLoaiSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLoaiSPMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblLoaiSP);

        tblThamChieuSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã SP", "Tên SP", "Loại SP"
            }
        ));
        jScrollPane3.setViewportView(tblThamChieuSP);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Mã Loại:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Tên Loại");

        btnAddLoai.setText("Thêm");
        btnAddLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLoaiActionPerformed(evt);
            }
        });

        btnUpdateLoai.setText("Sửa ");
        btnUpdateLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateLoaiActionPerformed(evt);
            }
        });

        btnDelLoai.setText("Xóa");
        btnDelLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelLoaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(btnAddLoai)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnUpdateLoai)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelLoai))
                            .addComponent(txtTenLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(133, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTenLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddLoai)
                            .addComponent(btnUpdateLoai)
                            .addComponent(btnDelLoai))))
                .addGap(44, 44, 44)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Loại Sản Phẩm", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        tblPhieuNhap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã Phiếu", "Nhân Viên", "Ngày Nhập", "Nhà Phân Phối", "Tổng Tiền"
            }
        ));
        tblPhieuNhap.setRowHeight(23);
        tblPhieuNhap.setSurrendersFocusOnKeystroke(true);
        tblPhieuNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPhieuNhapMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblPhieuNhap);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Phiếu Nhập");

        tblCTPhieu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã Phiếu CT", "Mã SP", "Số Lượng", "Đơn Giá", "Tổng Tiền"
            }
        ));
        tblCTPhieu.setRowHeight(23);
        tblCTPhieu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCTPhieuMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblCTPhieu);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Chi Tiết Phiếu Nhập");

        jLabel15.setText("Mã Phiếu:");

        txtNhanVien.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtNhanVien.setEnabled(false);

        jLabel16.setText("Nhân Viên:");

        txtNgayNhap.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNgayNhap.setEnabled(false);

        jLabel17.setText("Ngày Nhập");

        jLabel18.setText("Nhà Phân Phối");

        txtTongTien.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTongTien.setForeground(new java.awt.Color(255, 51, 51));
        txtTongTien.setEnabled(false);

        jLabel19.setText("Tổng Tiền:");

        jLabel20.setText("Nghi Chú:");

        btnNewPhieu.setText("Làm Mới");
        btnNewPhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewPhieuActionPerformed(evt);
            }
        });

        btnAddPhieu.setText("Thêm Mới");
        btnAddPhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPhieuActionPerformed(evt);
            }
        });

        btnUpdatePhieu.setText("Cập Nhập");
        btnUpdatePhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdatePhieuActionPerformed(evt);
            }
        });

        btnXoaPhieu.setText("Xóa");
        btnXoaPhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaPhieuActionPerformed(evt);
            }
        });

        btnDelCT.setText("Xóa");
        btnDelCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelCTActionPerformed(evt);
            }
        });

        btnUpdateCT.setText("Cập Nhập");
        btnUpdateCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateCTActionPerformed(evt);
            }
        });

        btnAddCT.setText("Thêm Mới");
        btnAddCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCTActionPerformed(evt);
            }
        });

        btnNewCT.setText("Làm Mới");
        btnNewCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCTActionPerformed(evt);
            }
        });

        txtPhieuTongTien.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPhieuTongTien.setForeground(new java.awt.Color(255, 51, 51));
        txtPhieuTongTien.setEnabled(false);

        jLabel21.setText("Thành Tiền:");

        jLabel22.setText("Đơn Giá");

        txtPhieuDonGia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPhieuDonGia.setEnabled(false);

        jLabel23.setText("Số Lượng");

        txtPhieuSoLuong.setText("0");
        txtPhieuSoLuong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtPhieuSoLuongMouseEntered(evt);
            }
        });

        jLabel24.setText("Nghi Chú:");

        txtMaPhieuCT.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtMaPhieuCT.setEnabled(false);

        jLabel25.setText("Mã Phiếu CT:");

        jLabel26.setText("Mã SP:");

        jLabel28.setText("Mã Phiếu:");

        txtMaPhieuCT1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtMaPhieuCT1.setEnabled(false);

        cboMaSPPhieu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboMaSPPhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMaSPPhieuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(233, 233, 233)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                            .addComponent(jLabel18)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtNhaPhanPhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                            .addComponent(jLabel17)
                                            .addGap(26, 26, 26)
                                            .addComponent(txtNgayNhap))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel16))
                                            .addGap(27, 27, 27)
                                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtNhanVien)
                                                .addComponent(txtMaPhieu)))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addComponent(jLabel19)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel20)
                                        .addComponent(txtGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnNewPhieu)
                                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addComponent(btnAddPhieu)
                                            .addGap(18, 18, 18)
                                            .addComponent(btnUpdatePhieu)
                                            .addGap(18, 18, 18)
                                            .addComponent(btnXoaPhieu))
                                        .addComponent(txtPhieuSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(cboMaSPPhieu, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                                .addComponent(txtMaPhieuCT, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel28)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtMaPhieuCT1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(btnNewCT)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnAddCT)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnUpdateCT)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnDelCT))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel22)
                                        .addComponent(jLabel21)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addGap(81, 81, 81)
                                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtPhieuDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtPhieuTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGap(152, 152, 152)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23))
                                .addGap(225, 225, 225)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addComponent(txtPhieuGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txtMaPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(txtNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel17)
                                    .addComponent(txtNgayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(txtNhaPhanPhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel19)
                                    .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(txtGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNewPhieu)
                    .addComponent(btnAddPhieu)
                    .addComponent(btnUpdatePhieu)
                    .addComponent(btnXoaPhieu))
                .addGap(12, 12, 12)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaPhieuCT, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24)
                            .addComponent(jLabel28)
                            .addComponent(txtMaPhieuCT1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel26)
                                    .addComponent(cboMaSPPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel23)
                                    .addComponent(txtPhieuSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel22)
                                    .addComponent(txtPhieuDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel21)
                                    .addComponent(txtPhieuTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtPhieuGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNewCT)
                            .addComponent(btnAddCT)
                            .addComponent(btnUpdateCT)
                            .addComponent(btnDelCT))))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Phiếu Nhập Kho", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLoaiActionPerformed
        insert_LoaiSP();
    }//GEN-LAST:event_btnAddLoaiActionPerformed

    private void btnUpdateLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateLoaiActionPerformed
        update_LoaiSP();
    }//GEN-LAST:event_btnUpdateLoaiActionPerformed

    private void btnDelLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelLoaiActionPerformed
        delete_Delete_LoaiSP();
    }//GEN-LAST:event_btnDelLoaiActionPerformed

    private void tblLoaiSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLoaiSPMouseClicked
        try {
            int row = tblLoaiSP.getSelectedRow();
            String id = (String) tblLoaiSP.getValueAt(row, 1);
            LoaiSanPham u = lDAO.findByID(id);
            if (u != null) {
                setLoai(u);
                loadSPbyIDLoai(id);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_tblLoaiSPMouseClicked

    void Load_SP() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblSanPham.getModel();
            model.setRowCount(0);
            List<SanPham> list = spDAO.select();
            for (SanPham sp : list) {
                model.addRow(new Object[]{
                    sp.getMaSP(), sp.getTenSP(), sp.getSoLuong(), sp.getGiaNhap(), sp.getGiaBan(), sp.getMaLoai(), sp.getNghiChu()
                });
            }
            model.fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "lỗi truy vấn dữ liệu");
        }
    }

    void insert_SP() {
        try {
            SanPham sp = getSP();
            spDAO.insert_SanPham(sp);
            this.Load_SP();
            DialogHelper.alert(this, "Thêm Thành Công");
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Thêm Thất Bại");
        }
    }

    void update_SP() {
        try {
            SanPham sp = getSP();
            spDAO.update_SanPham(sp);
            this.Load_SP();
            DialogHelper.alert(this, "Cập Nhập Thành Công");
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Cập Nhập Thất Bại");
        }
    }

    void delete_SP() {
        String id = txtMaSP.getText();
        if (DialogHelper.confirm(this, "Bạn Có Muốn Xóa Sản Phẩm Không")) {
            try {
                spDAO.delete_SanPham(id);
                DialogHelper.alert(this, "Xóa Thành Công");
            } catch (Exception e) {
                e.printStackTrace();
                DialogHelper.alert(this, "Xóa Thấ Bại");
            }
        }
    }

    SanPham getSP() {
        SanPham sp = new SanPham();
        sp.setMaSP(txtMaSP.getText());
        sp.setTenSP(txtTenSP.getText());
        sp.setSoLuong(Integer.valueOf(txtSoLuong.getText()));
        sp.setGiaNhap(Float.valueOf(txtGiaNhap.getText()));
        sp.setGiaBan(Float.valueOf(txtGiaBan.getText()));
        //sp.setMaLoai(txtLoai.getText());
        sp.setNghiChu(txtNghiChu.getText());
        sp.setAnh(lblAnh.getToolTipText());

        LoaiSanPham lsp = (LoaiSanPham) cbbLoaiSP.getSelectedItem();
        sp.setMaLoai(lsp.getMaLoai());

        return sp;
    }

    private void setSP(SanPham sp) {
        txtMaSP.setText(sp.getMaSP());
        txtTenSP.setText(sp.getTenSP());
        txtSoLuong.setText(String.valueOf(sp.getSoLuong()));
        txtGiaBan.setText(String.valueOf(sp.getGiaBan()));
        txtGiaNhap.setText(String.valueOf(sp.getGiaNhap()));
        txtNghiChu.setText(sp.getNghiChu());

        // load image lên Jlable
        lblAnh.setToolTipText(sp.getAnh());
        if (sp.getAnh() != null) {

            ImageIcon path = ShareHelper.readLogos(sp.getAnh());
            Image img = path.getImage();
            Image imgScale = img.getScaledInstance(lblAnh.getWidth(), lblAnh.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon selectedIcon = new ImageIcon(imgScale);
            lblAnh.setText(null);
            lblAnh.setIcon(selectedIcon);

        } else {
            lblAnh.setIcon(null);
            lblAnh.setText("No Avata");
        }

        // load TiptoolText mã loại lên CBB
        cbbLoaiSP.setToolTipText(String.valueOf(sp.getMaLoai()));

        // List<LoaiSanPham> list = lDAO.findByIDs(cbbLoaiSP.getToolTipText());
//         cbbLoaiSP.setSelectedItem(lDAO.findByID(sp.getMaLoai()));
//        cbbLoaiSP.setSelectedIndex(Integer.parseInt(cbbLoaiSP.getToolTipText()));
    }

    void loadComboxSP_Loai() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbbLoaiSP.getModel();
        List<LoaiSanPham> list = lDAO.select();
        model.removeAllElements();
        for (LoaiSanPham lsp : list) {

            model.addElement(lsp);
        }

    }

    void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (ShareHelper.saveLogo(file)) {
                // hiển thị lên form
                ImageIcon path = ShareHelper.readLogos(file.getName());
                Image img = path.getImage();
                Image imgScale = img.getScaledInstance(lblAnh.getWidth(), lblAnh.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon selectedIcon = new ImageIcon(imgScale);
                lblAnh.setIcon(selectedIcon);
                lblAnh.setToolTipText(file.getName());

//                lblAnh.setIcon(ShareHelper.readLogos(file.getName()));
//                lblAnh.setToolTipText(file.getName());
            }
        }
    }

    private void btnSelectImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectImageActionPerformed
        selectImage();
    }//GEN-LAST:event_btnSelectImageActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        insert_SP();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        update_SP();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        delete_SP();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void tblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamMouseClicked
        try {
            int row = tblSanPham.getSelectedRow();
            String id = (String) tblSanPham.getValueAt(row, 0);
            SanPham u = spDAO.findByID(id);
            if (u != null) {
                setSP(u);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_tblSanPhamMouseClicked

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        this.setSP(new SanPham());
        //this.setStatus(true);
        lblAnh.setIcon(null);
        lblAnh.setText("No avata");
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNewPhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewPhieuActionPerformed
        clearPhieuNhap();
        clearCTPhieu();
        loadCTPhieu(null);
        btnAddCT.setEnabled(false);
        btnDelCT.setEnabled(false);
        btnUpdateCT.setEnabled(false);
        btnNewCT.setEnabled(false);
    }//GEN-LAST:event_btnNewPhieuActionPerformed

    PhieuNhapDAO pnDAO = new PhieuNhapDAO();

    void loadPhieuNhap() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblPhieuNhap.getModel();
            model.setRowCount(0);
            List<PhieuNhap> list = pnDAO.select();
            for (PhieuNhap pn : list) {
                model.addRow(new Object[]{
                    pn.getMaPN(), pn.getMaNV(), DateHelper.toString(pn.getNgayNhap()),
                    pn.getNhaPP(), pn.getTongtien()
                });
            }
            model.fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Lỗi Truy Vấn Dữ Liệu");
        }
    }

    void insert_PhieuNhap() {
        try {
            PhieuNhap pn = getPhieuNhap();
            pnDAO.insert_PhieuNhap(pn);
            loadPhieuNhap();
            DialogHelper.alert(this, "Thêm Thành Công");
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Thêm Thất Bại");
        }
    }

    void update_PhieuNhap() {
        try {
            PhieuNhap pn = getPhieuNhap();
            pnDAO.update_PhieuNhap(pn);
            loadPhieuNhap();
            DialogHelper.alert(this, "Thêm Thành Công");
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Thêm Thất Bại");
        }
    }

    void delete_PhieuNhap() {
        try {
            String id = txtMaPhieu.getText();
            pnDAO.delete_PhieuNhap(id);
            loadPhieuNhap();
            DialogHelper.alert(this, "Thêm Thành Công");
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Thêm Thất Bại");
        }
    }

    void clearPhieuNhap() {
        setPhieuNhap(new PhieuNhap());
        txtNhanVien.setText(ShareHelper.USER.getMaNV());
        setStatusPhieuNhap(true);

    }

    void editPhieuNhap() {
        try {
            int row = tblPhieuNhap.getSelectedRow();
            String id = (String) tblPhieuNhap.getValueAt(row, 0);
            PhieuNhap pn = pnDAO.findByID(id);
            if (pn != null) {
                this.setPhieuNhap(pn);
                this.clearCTPhieu();
                txtMaPhieuCT1.setText(id);
                this.setStatusPhieuNhap(false);
                loadCTPhieu(pn.getMaPN());
                setStatusCTPhieu(true);
                this.clearCTPhieu();
                this.setStatusCTPhieu(true);

            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Lỗi dữ liệu");
        }
    }

    void setStatusPhieuNhap(boolean isertable) {
        btnAddPhieu.setEnabled(isertable);
        btnUpdatePhieu.setEnabled(!isertable);
        btnXoaPhieu.setEnabled(!isertable);
    }

    PhieuNhap getPhieuNhap() {
        PhieuNhap pn = new PhieuNhap();
        pn.setMaPN(txtMaPhieu.getText());
        pn.setMaNV(txtNhanVien.getText());
        pn.setNhaPP(txtNhaPhanPhoi.getText());
        pn.setGhiChu(txtGhiChu.getText());
        pn.setNgayNhap(DateHelper.toDae(txtNgayNhap.getText()));
        if (txtTongTien.getText().equals("")) {
            pn.setTongtien(0);
        } else {
            pn.setTongtien(Float.valueOf(txtTongTien.getText()));
        }
        return pn;
    }

    void setPhieuNhap(PhieuNhap pn) {
        txtMaPhieu.setText(pn.getMaPN());
        txtNhanVien.setText(pn.getMaNV());
        txtNhaPhanPhoi.setText(pn.getNhaPP());
        txtNgayNhap.setText(DateHelper.toString(pn.getNgayNhap()));
        txtTongTien.setText(String.valueOf(pn.getTongtien()));
        txtGhiChu.setText(pn.getGhiChu());

    }

    private void btnAddPhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPhieuActionPerformed
        this.insert_PhieuNhap();
    }//GEN-LAST:event_btnAddPhieuActionPerformed

    private void btnUpdatePhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdatePhieuActionPerformed
        this.update_PhieuNhap();
    }//GEN-LAST:event_btnUpdatePhieuActionPerformed

    private void btnXoaPhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaPhieuActionPerformed
        this.delete_PhieuNhap();
    }//GEN-LAST:event_btnXoaPhieuActionPerformed

    private void tblPhieuNhapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPhieuNhapMouseClicked
        editPhieuNhap();
        btnNewCT.setEnabled(true);
    }//GEN-LAST:event_tblPhieuNhapMouseClicked

    NhieuNhapCTDAO ctDAO = new NhieuNhapCTDAO();

    void loadCTPhieu(String id) {
        DefaultTableModel model = (DefaultTableModel) tblCTPhieu.getModel();
        model.setRowCount(0);
        List<PhieuNhapChiTiet> list = ctDAO.select(id);
        for (PhieuNhapChiTiet ct : list) {
            model.addRow(new Object[]{
                ct.getPhieuNhapCT(), ct.getMaSP(), ct.getSoluong(), ct.getDonGia(), ct.getTongTien()
            });
        }
        model.fireTableDataChanged();
    }

    void loadComboBoxSP() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboMaSPPhieu.getModel();
        model.removeAllElements();
        List<SanPham> list = spDAO.select();
        for (SanPham sp : list) {
            model.addElement(sp);
        }
    }

    void loadThanhTien() {
        SanPham sp1 = (SanPham) cboMaSPPhieu.getSelectedItem();
        String id = sp1.getMaSP();
        SanPham sp = spDAO.findByID(id);
        if (sp != null) {
            int SoLuong = Integer.valueOf(txtPhieuSoLuong.getText());
            float donGia = sp.getGiaBan();
            float ThanhTien = donGia * SoLuong;
            txtPhieuTongTien.setText(String.valueOf(ThanhTien));
        }
    }

    void loadDonGia() {
        try {
            SanPham sp1 = (SanPham) cboMaSPPhieu.getSelectedItem();
            txtPhieuDonGia.setText(String.valueOf(sp1.getGiaBan()));
            System.out.println("Giá Bán = " + sp1.getGiaBan());
        } catch (Exception e) {

        }
    }

    void insert_CTPhieu() {
        String id = txtMaPhieuCT1.getText();
        try {
            PhieuNhapChiTiet ct = getCTPhieu();

            ctDAO.insert_PhieuCT(ct);
            DialogHelper.alert(this, "Thêm Thành Công");
            setTongTien(id);
            loadCTPhieu(id);
            
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Thêm Thất Bại");
        }
    }

    void update_CTPhieu() {
        String id = txtMaPhieuCT1.getText();
        try {
            PhieuNhapChiTiet ct = getCTPhieu1();
            ctDAO.update_PhieuCT(ct);
            DialogHelper.alert(this, "Cập Nhập Thành Công");
            setTongTien(id);
           loadCTPhieu(id);
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Cập Nhập Thất Bại");
        }
    }

    void del_CTPhieu() {
        String ids = txtMaPhieuCT1.getText();
        try {
            int id = Integer.valueOf(txtMaPhieuCT.getText());
            ctDAO.delete_PhieuCT(id);
            setTongTien(ids);
            this.loadCTPhieu(ids);
            this.clearCTPhieu();
            DialogHelper.alert(this, "Xóa Thành Công");
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Xóa Thất Bại");
        }
    }

    void clearCTPhieu() {
        setCTPhieu(new PhieuNhapChiTiet());
        

    }

    void edit_CTPhieu() {
        int row = tblCTPhieu.getSelectedRow();
        int id = (int) tblCTPhieu.getValueAt(row, 0);
        PhieuNhapChiTiet ct = ctDAO.findById(id);
        if (ct != null) {
            setCTPhieu(ct);
            setStatusCTPhieu(false);
        }
    }

    void setStatusCTPhieu(boolean isertable) {
        btnAddCT.setEnabled(isertable);
        btnDelCT.setEnabled(!isertable);
        btnUpdateCT.setEnabled(!isertable);
        
    }

    PhieuNhapChiTiet getCTPhieu() {
        PhieuNhapChiTiet ct = new PhieuNhapChiTiet();
        ct.setMaPN(txtMaPhieuCT1.getText());
        SanPham sp = (SanPham) cboMaSPPhieu.getSelectedItem();
        ct.setMaSP(sp.getMaSP());
        ct.setSoluong(Integer.valueOf(txtPhieuSoLuong.getText()));
        ct.setDonGia(Float.valueOf(txtPhieuDonGia.getText()));
        ct.setTongTien(Float.valueOf(txtPhieuTongTien.getText()));
        ct.setChuThich(txtPhieuGhiChu.getText());
        return ct;
    }

    PhieuNhapChiTiet getCTPhieu1() {
        PhieuNhapChiTiet ct = new PhieuNhapChiTiet();
        ct.setPhieuNhapCT(Integer.valueOf(txtMaPhieuCT.getText()));
        ct.setMaPN(txtMaPhieuCT1.getText());
        SanPham sp = (SanPham) cboMaSPPhieu.getSelectedItem();
        ct.setMaSP(sp.getMaSP());
        ct.setSoluong(Integer.valueOf(txtPhieuSoLuong.getText()));
        ct.setDonGia(Float.valueOf(txtPhieuDonGia.getText()));
        ct.setTongTien(Float.valueOf(txtPhieuTongTien.getText()));
        ct.setChuThich(txtPhieuGhiChu.getText());
        return ct;
    }

    void setCTPhieu(PhieuNhapChiTiet ct) {
        txtMaPhieuCT.setText(String.valueOf(ct.getPhieuNhapCT()));

        txtPhieuSoLuong.setText(String.valueOf(ct.getSoluong()));
        // txtPhieuDonGia.setText(String.valueOf(ct.getDonGia()));
        txtPhieuTongTien.setText(String.valueOf(ct.getTongTien()));
        txtPhieuGhiChu.setText(ct.getChuThich());
    }

    void setTongTien(String MaPhieuNhap) {
        String sql = "select SUM(PhieuNhapCT.TongTien) as 'TongTien', PhieuNhap.MaPN\n"
                + "from PhieuNhap inner join PhieuNhapCT on PhieuNhap.MaPN=PhieuNhapCT.MaPN\n"
                + "where PhieuNhap.MaPN= ? group by PhieuNhap.MaPN ";
        ResultSet rs = jdbcHelper.executeQuery(sql, MaPhieuNhap);
        String TongTienHienTai="";
        
        try {
            if (rs.next()) {
                TongTienHienTai = rs.getString("TongTien");
                txtTongTien.setText(TongTienHienTai);
                String ctv = "update PhieuNhap set TongTien = "+ TongTienHienTai + "Where MaPN =?";
                System.out.println(ctv);
                jdbcHelper.executeUpdate(ctv, MaPhieuNhap);
                loadPhieuNhap();
            }else{
                TongTienHienTai = "0";
                txtTongTien.setText(TongTienHienTai);
                String ctv = "update PhieuNhap set TongTien = "+ TongTienHienTai + "Where MaPN =?";
                System.out.println(ctv);
                jdbcHelper.executeUpdate(ctv, MaPhieuNhap);
                loadPhieuNhap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnAddCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCTActionPerformed
        this.insert_CTPhieu();
    }//GEN-LAST:event_btnAddCTActionPerformed

    private void btnNewCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCTActionPerformed
        this.clearCTPhieu();
    }//GEN-LAST:event_btnNewCTActionPerformed

    private void btnUpdateCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateCTActionPerformed
        this.update_CTPhieu();
    }//GEN-LAST:event_btnUpdateCTActionPerformed

    private void btnDelCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelCTActionPerformed
        this.del_CTPhieu();
    }//GEN-LAST:event_btnDelCTActionPerformed

    private void tblCTPhieuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCTPhieuMouseClicked
        edit_CTPhieu();
    }//GEN-LAST:event_tblCTPhieuMouseClicked

    private void cboMaSPPhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMaSPPhieuActionPerformed
        loadDonGia();
    }//GEN-LAST:event_cboMaSPPhieuActionPerformed

    private void txtPhieuSoLuongMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPhieuSoLuongMouseEntered
        loadThanhTien();
    }//GEN-LAST:event_txtPhieuSoLuongMouseEntered

    
    boolean checkEmptySanPham(StringBuilder sb){
        ValidateHelper.chekEmpyDemo(txtMaSP, sb, "");
        ValidateHelper.chekEmpyDemo(txtTenSP, sb, "");
        ValidateHelper.chekEmpyDemo(txtGiaBan, sb, "");
        ValidateHelper.chekEmpyDemo(txtGiaBan, sb, "");
        if (sb.length()>0) {
            return true;
        }
        return false;
    }
    // Quan Ly Nhap Kho

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddCT;
    private javax.swing.JButton btnAddLoai;
    private javax.swing.JButton btnAddPhieu;
    private javax.swing.JButton btnDelCT;
    private javax.swing.JButton btnDelLoai;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnNewCT;
    private javax.swing.JButton btnNewPhieu;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSelectImage;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnUpdateCT;
    private javax.swing.JButton btnUpdateLoai;
    private javax.swing.JButton btnUpdatePhieu;
    private javax.swing.JButton btnXoaPhieu;
    private javax.swing.JComboBox<String> cbbLoaiSP;
    private javax.swing.JComboBox<String> cboMaSPPhieu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JTable tblCTPhieu;
    private javax.swing.JTable tblLoaiSP;
    private javax.swing.JTable tblPhieuNhap;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTable tblThamChieuSP;
    private javax.swing.JTextField txtGhiChu;
    private javax.swing.JTextField txtGiaBan;
    private javax.swing.JTextField txtGiaNhap;
    private javax.swing.JTextField txtMaLoai;
    private javax.swing.JTextField txtMaPhieu;
    private javax.swing.JTextField txtMaPhieuCT;
    private javax.swing.JTextField txtMaPhieuCT1;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextField txtNgayNhap;
    private javax.swing.JTextField txtNghiChu;
    private javax.swing.JTextField txtNhaPhanPhoi;
    private javax.swing.JTextField txtNhanVien;
    private javax.swing.JTextField txtPhieuDonGia;
    private javax.swing.JTextField txtPhieuGhiChu;
    private javax.swing.JTextField txtPhieuSoLuong;
    private javax.swing.JTextField txtPhieuTongTien;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenLoai;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables
}
