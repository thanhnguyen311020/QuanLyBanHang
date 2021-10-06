/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.Form;

import com.fpoly.quanlybanhang.DAO.HoaDonChiTietDAO;
import com.fpoly.quanlybanhang.DAO.HoaDonDAO;
import com.fpoly.quanlybanhang.DAO.KhachHangDAO;
import com.fpoly.quanlybanhang.DAO.SanPhamDAO;
import com.fpoly.quanlybanhang.Model.DonHang;
import com.fpoly.quanlybanhang.Model.HoaDonChiTiet;
import com.fpoly.quanlybanhang.Model.KhachHang;
import com.fpoly.quanlybanhang.Model.SanPham;
import com.fpoly.quanlybanhang.helper.DateHelper;
import com.fpoly.quanlybanhang.helper.DialogHelper;
import com.fpoly.quanlybanhang.helper.ShareHelper;
import com.fpoly.quanlybanhang.helper.ValidateHelper;
import com.fpoly.quanlybanhang.helper.jdbcHelper;
import java.awt.Dialog;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class DonHangJframe extends javax.swing.JInternalFrame {

    HoaDonDAO hdDAO = new HoaDonDAO();
    HoaDonChiTietDAO ctDAO = new HoaDonChiTietDAO();
    SanPhamDAO spDAO = new SanPhamDAO();

    /**
     * Creates new form DonHangJframe
     */
    public DonHangJframe() {
        initComponents();
        init();
    }

    void init() {
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI bi = (BasicInternalFrameUI) this.getUI();
        bi.setNorthPane(null);
        LoadHoaDonToTable();
        txtMaNV.setText(ShareHelper.USER.getMaNV());
        txtNgayLap.setText(DateHelper.toString(DateHelper.now()));
        setStatusHoaDon(true);

        loadComBoBoxKhachHang();
        loadComboBoxSanPham();

        btnAddCT.setEnabled(false);
        btnXoaCT.setEnabled(false);
        btnUpdateCT.setEnabled(!true);
        btnNewCT.setEnabled(false);
    }

    void LoadHoaDonToTable() {
        String Object[] = {"Mã Hóa Đơn", "Nhân Viên", "Khách Hàng", "Ngày Lập", "Tổng Tiền"};
        DefaultTableModel model = new DefaultTableModel(Object, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        tblHoaDon.setModel(model);
        model.setRowCount(0);
        List<DonHang> list = hdDAO.select();
        for (DonHang hd : list) {
            model.addRow(new Object[]{
                hd.getMaHD(), hd.getMaNV(), hd.getMaKH(), DateHelper.toString(hd.getNgayLap()), hd.getThanhTien()
            });
        };
        model.fireTableDataChanged();
    }
    KhachHangDAO khDAO = new KhachHangDAO();

    void loadComBoBoxKhachHang() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboKhachHang.getModel();
        List<KhachHang> list = khDAO.select();
        model.removeAllElements();
        for (KhachHang kh : list) {
            model.addElement(kh);
        }
    }

    void insert_hoaDon() {
        StringBuilder sb = new StringBuilder();
        if (checkEmptyHoaDon(sb)) {
            return;
        }
        try {
            DonHang hd = getHoaDon();
            hdDAO.insert_HoaDon(hd);
            LoadHoaDonToTable();
            DialogHelper.alert(this, "Thêm Hóa Đơn Thành Công");
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Mã hóa đơn không được trùng");
        }
    }

    void update_hoaDon() {
        if (DialogHelper.confirm(this, "Bạn có muốn cập nhập hóa đơn không?")) {

            try {
                DonHang hd = getHoaDon();
                hdDAO.update_HoaDon(hd);
                LoadHoaDonToTable();
                DialogHelper.alert(this, "Cập Nhập Hóa Đơn Thành Công");
            } catch (Exception e) {
                e.printStackTrace();
                DialogHelper.alert(this, "Mã hóa đơn không tồn tại");
            }
        }
    }

    void delete_hoaDon() {
        if (DialogHelper.confirm(this, "Bán có muốn xóa không")) {

            try {
                String id = txtMaHD.getText();
                DonHang hd = hdDAO.findById(id);
                if (hd != null) {
                    hdDAO.delete_HoaDon(id);
                    LoadHoaDonToTable();
                    DialogHelper.alert(this, "Xóa Hóa Đơn Thành Công");
                }else{
                    DialogHelper.alert(this, "Mã Hóa đơn không tồn tại");
                }

            } catch (Exception e) {
                e.printStackTrace();
                DialogHelper.alert(this, "Không thể xóa hóa đơn có hóa đơn chi tiết");
            }
        }
    }

    void clear() {
        this.setHoaDon(new DonHang());
        txtMaNV.setText(ShareHelper.USER.getMaNV());
        this.setStatusHoaDon(true);
    }

    void editHoaDon() {
        try {
            int row = tblHoaDon.getSelectedRow();
            String id = (String) tblHoaDon.getValueAt(row, 0);
            DonHang dh = hdDAO.findById(id);
            if (dh != null) {
                this.setHoaDon(dh);

                LoadHoaDonChiTiet(id);
                this.setStatusHoaDon(false);
                this.clearHoaDonCT();
                setStatusHoaDonCT(true);
                txtMaHD2.setText(dh.getMaHD());
            }
        } catch (Exception e) {
        }
    }

    void setStatusHoaDon(boolean isertable) {
        btnSave.setEnabled(isertable);
        btnUpdate.setEnabled(!isertable);
        btnDel.setEnabled(!isertable);

    }

    DonHang getHoaDon() {
        DonHang hd = new DonHang();
        hd.setMaHD(txtMaHD.getText());
        hd.setMaNV(txtMaNV.getText());
        // hd.setMaKH(txtMaKH.getText());
        KhachHang kh = (KhachHang) cboKhachHang.getSelectedItem();
        hd.setMaKH(kh.getMaKH());

        hd.setNgayLap(DateHelper.toDae(txtNgayLap.getText()));
        if (txtTongTien.getText().equals("")) {
            hd.setThanhTien(0);
        } else {
            hd.setThanhTien(Float.valueOf(txtTongTien.getText()));

        }
        hd.setGhiChu(txtGhiChu.getText());
        return hd;
    }

    void setHoaDon(DonHang hd) {
        txtMaHD.setText(hd.getMaHD());
        txtMaNV.setText(hd.getMaNV());
        //     txtMaKH.setText(hd.getMaKH());
        txtTongTien.setText(String.valueOf(hd.getThanhTien()));
        txtGhiChu.setText(hd.getGhiChu());
        txtNgayLap.setText(DateHelper.toString(hd.getNgayLap()));
    }

    public void SetTongTien(String MaHoaDon) {
        String cautruyvan = "select sum(HoaDonChiTiet.TongTien) as 'TongTienHienTai', HoaDon.MaHoaDon \n"
                + "from HoaDonChiTiet inner join HoaDon on HoaDonChiTiet.MaHoaDon=HoaDon.MaHoaDon \n"
                + "where HoaDon.MaHoaDon=? group by HoaDon.MaHoaDon";
        ResultSet rs = jdbcHelper.executeQuery(cautruyvan, MaHoaDon);
        String ttht = "";

        try {
            if (rs.next()) {
                ttht = rs.getString("TongTienHienTai");
                txtTongTien.setText(ttht);
                String ctv = "update HoaDon set TongTien= " + ttht + "where MaHoaDon= ?";
                System.out.println(ctv);
                jdbcHelper.executeUpdate(ctv, MaHoaDon);
                LoadHoaDonToTable();
            } else {
                ttht = "0";
                txtTongTien.setText(ttht);
                String ctv = "update HoaDon set TongTien = " + ttht + "Where MaHoaDon =?";
                System.out.println(ctv);
                jdbcHelper.executeUpdate(ctv, MaHoaDon);
                LoadHoaDonToTable();
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }

    }

    void LoadHoaDonChiTiet(String id) {
        String header[] = {"Mã CT", "Mã Hóa Đơn", "Mã SP", "Số Lượng", "Tổng Tiền"};
        DefaultTableModel model = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        tblChiTietHoaDon.setModel(model);
        model.setRowCount(0);
        try {
            List<HoaDonChiTiet> list = ctDAO.select(id);
            for (HoaDonChiTiet ct : list) {
                model.addRow(new Object[]{
                    ct.getMaHoaDonCT(), ct.getMaHoaDon(), ct.getMaSP(), ct.getSoLuong(),
                    ct.getThanhTien()
                });
            }
            model.fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadComboBoxSanPham() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboSanPham.getModel();
        model.removeAllElements();
        List<SanPham> list = spDAO.select();
        for (SanPham sp : list) {
            model.addElement(sp);
        }
    }

    void insert_HoaDonCT() {
        try {
            String id = txtMaHD2.getText();
            HoaDonChiTiet ct = getHoaDonCT();
            ctDAO.insert_HoaDonChiTiet(ct);
            SetTongTien(id);
            DialogHelper.alert(this, "Thêm Thành Công");
            LoadHoaDonChiTiet(txtMaHD2.getText());
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Thêm Thất Bại");
        }
    }

    void update_HoaDonCT() {
        String idHD = txtMaHD2.getText();
        try {
            HoaDonChiTiet ct = getHoaDonCT1();

            ctDAO.update_HoaDonChiTiet(ct);
            SetTongTien(idHD);
            DialogHelper.alert(this, "Cập Nhập Thành Công");
            LoadHoaDonChiTiet(idHD);
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Cập Nhập Thất Bại");
        }
    }

    void delete_HoaDonCT() {
        if (DialogHelper.confirm(this, "bạn có muốn xóa không")) {

            try {
                String idHD = txtMaHD2.getText();
                int id = Integer.valueOf(txtMaHDCT.getText());
                ctDAO.delete_HoaDonCT(id);
                SetTongTien(idHD);
                LoadHoaDonChiTiet(idHD);
                DialogHelper.alert(this, "Ok");
            } catch (Exception e) {
                e.printStackTrace();
                DialogHelper.alert(this, "Xóa Thất Bại");
            }
        }
    }

    void LoadThanhTien() {
        SanPham sp1 = (SanPham) cboSanPham.getSelectedItem();
        String id1 = sp1.getMaSP();
        // String id = txtMaSP.getText();
        SanPham sp = spDAO.findByID(id1);

        if (sp != null) {

            int SoLuong = Integer.valueOf(txtSoLuong.getText());
            float donGia = sp.getGiaBan();
            float ThanhTien = donGia * SoLuong;
            txtThanhTien.setText(String.valueOf(ThanhTien));
        }
    }

    void clearHoaDonCT() {
        setHoaDonCT(new HoaDonChiTiet());
        txtMaHDCT.setText("");
    }

    void editHoaDonCT() {
        int row = tblChiTietHoaDon.getSelectedRow();
        int id = (int) tblChiTietHoaDon.getValueAt(row, 0);
        HoaDonChiTiet hdct = ctDAO.findByID(id);
        if (hdct != null) {
            setHoaDonCT(hdct);

            setStatusHoaDonCT(false);
        }
    }

    void setStatusHoaDonCT(boolean isertable) {
        btnAddCT.setEnabled(isertable);
        btnXoaCT.setEnabled(!isertable);
        btnUpdateCT.setEnabled(!isertable);

    }

    HoaDonChiTiet getHoaDonCT() {
        HoaDonChiTiet ct = new HoaDonChiTiet();
        ct.setMaHoaDon(txtMaHD2.getText());

        SanPham sp = (SanPham) cboSanPham.getSelectedItem();
        ct.setMaSP(sp.getMaSP());

        ct.setSoLuong(Integer.valueOf(txtSoLuong.getText()));
        ct.setGhiChu(txtCTGhiChu.getText());
        ct.setThanhTien(Float.valueOf(txtThanhTien.getText()));
        return ct;
    }

    HoaDonChiTiet getHoaDonCT1() {
        HoaDonChiTiet ct = new HoaDonChiTiet();
        ct.setMaHoaDonCT(Integer.valueOf(txtMaHDCT.getText()));
        ct.setMaHoaDon(txtMaHD2.getText());

        SanPham sp = (SanPham) cboSanPham.getSelectedItem();
        ct.setMaSP(sp.getMaSP());

        ct.setSoLuong(Integer.valueOf(txtSoLuong.getText()));
        ct.setGhiChu(txtCTGhiChu.getText());
        ct.setThanhTien(Float.valueOf(txtThanhTien.getText()));
        return ct;
    }

    void setHoaDonCT(HoaDonChiTiet hdct) {
        txtMaHDCT.setText(String.valueOf(hdct.getMaHoaDonCT()));
        txtMaHD2.setText(hdct.getMaHoaDon());
        // txtMaSP.setText(hdct.getMaSP());
        txtSoLuong.setText(String.valueOf(hdct.getSoLuong()));
        txtThanhTien.setText(String.valueOf(hdct.getThanhTien()));

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
        jLabel4 = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblChiTietHoaDon = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        txtMaNV = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtNgayLap = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnNew = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnXoaCT = new javax.swing.JButton();
        btnUpdateCT = new javax.swing.JButton();
        btnAddCT = new javax.swing.JButton();
        btnNewCT = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtThanhTien = new javax.swing.JTextField();
        txtSoLuong = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtMaHD2 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtCTGhiChu = new javax.swing.JTextPane();
        lblExcel = new javax.swing.JLabel();
        lblPDF = new javax.swing.JLabel();
        cboKhachHang = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        cboSanPham = new javax.swing.JComboBox<>();
        txtMaHDCT = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1046, 705));

        jPanel1.setBackground(new java.awt.Color(53, 92, 125));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon("D:\\Fpoly\\Fall 2020 (4)\\Dự Án 1\\Icon\\1x\\outline_addchart_white_36dp.png")); // NOI18N
        jLabel4.setText("QUẢN LÝ ĐƠN HÀNG");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(640, 640, 640)
                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDate))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Bảng Hóa Đơn");

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblHoaDon.setRowHeight(23);
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        tblChiTietHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblChiTietHoaDon.setRowHeight(23);
        tblChiTietHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChiTietHoaDonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblChiTietHoaDon);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Bảng Hóa Đơn Chi Tiết");

        jLabel3.setText("Mã Hóa Đơn:");

        txtMaNV.setBackground(new java.awt.Color(240, 240, 240));
        txtMaNV.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtMaNV.setEnabled(false);

        jLabel5.setText("Mã Nhân Viên:");

        jLabel6.setText("Mã Khách Hàng:");

        jLabel7.setText("Ngày Lập:");

        txtTongTien.setBackground(new java.awt.Color(240, 240, 240));
        txtTongTien.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtTongTien.setForeground(new java.awt.Color(255, 0, 0));
        txtTongTien.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        txtTongTien.setEnabled(false);

        jLabel8.setText("Tổng Tiền:");

        jLabel9.setText("Ghi Chú:");

        btnNew.setText("Làm Mới");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnSave.setText("Thêm Mới");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnUpdate.setText("Cập Nhập");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDel.setText("Xóa");
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });

        btnXoaCT.setText("Xóa");
        btnXoaCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaCTActionPerformed(evt);
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

        jLabel10.setText("Thành Tiền:");

        jLabel11.setText("Số Lượng:");

        txtThanhTien.setBackground(new java.awt.Color(240, 240, 240));
        txtThanhTien.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtThanhTien.setForeground(new java.awt.Color(255, 0, 0));
        txtThanhTien.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        txtThanhTien.setEnabled(false);

        txtSoLuong.setText("0");
        txtSoLuong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSoLuongKeyReleased(evt);
            }
        });

        jLabel12.setText("Sản Phẩm");

        jLabel13.setText("Mã Hóa Đơn");

        jLabel14.setText("Mã Hóa Đơn CT:");

        txtMaHD2.setBackground(new java.awt.Color(240, 240, 240));
        txtMaHD2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtMaHD2.setEnabled(false);

        jLabel15.setText("Ghi Chú:");

        jScrollPane5.setViewportView(txtGhiChu);

        jScrollPane6.setViewportView(txtCTGhiChu);

        lblExcel.setText("Excel");

        lblPDF.setText("PDF");

        cboKhachHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("Add");

        cboSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtMaHDCT.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(222, 222, 222)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                        .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(18, 18, 18)
                                        .addComponent(cboKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNew)
                                .addGap(22, 22, 22)
                                .addComponent(btnSave)
                                .addGap(18, 18, 18)
                                .addComponent(btnUpdate)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9)
                                .addGap(129, 129, 129))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(26, 26, 26)
                                        .addComponent(btnDel))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel15)
                                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(180, 180, 180)
                                .addComponent(jLabel2))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(19, 19, 19)
                                    .addComponent(lblPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(btnNewCT)
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(btnAddCT)
                                            .addGap(18, 18, 18)
                                            .addComponent(btnUpdateCT)
                                            .addGap(18, 18, 18)
                                            .addComponent(btnXoaCT))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(txtMaHDCT, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(cboSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtMaHD2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGap(18, 18, 18)
                                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(cboKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(txtNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnNew)
                                    .addComponent(btnSave)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnUpdate)
                                    .addComponent(btnDel)))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane6))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(txtMaHDCT, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(txtMaHD2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(cboSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNewCT)
                            .addComponent(btnAddCT)
                            .addComponent(btnUpdateCT)
                            .addComponent(btnXoaCT))
                        .addGap(57, 57, 57))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, 42))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1030, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        this.clear();
        setStatusHoaDonCT(false);
        LoadHoaDonChiTiet(null);
        btnNewCTActionPerformed(evt);
        btnAddCT.setEnabled(false);
        btnXoaCT.setEnabled(false);
        btnUpdateCT.setEnabled(!true);
        btnNewCT.setEnabled(false);

    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        insert_hoaDon();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        StringBuilder sb = new StringBuilder();
        if (checkEmptyHoaDon(sb)) {
            return;
        }
        update_hoaDon();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        if (txtMaHD.getText().isEmpty()) {
            DialogHelper.alert(this, "Bạn Phải nhập mã hóa đơn");
            return;
        }
        delete_hoaDon();
        LoadThanhTien();
    }//GEN-LAST:event_btnDelActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
//        int row = tblHoaDon.getSelectedRow();
//        String id = (String) tblHoaDon.getValueAt(row, 0);
//        DonHang hd = hdDAO.findById(id);
//        if (hd != null) {
//            setHoaDon(hd);
//            txtMaHD2.setText(hd.getMaHD());
//            LoadHoaDonChiTiet(id);
//        }
        this.editHoaDon();
        btnNewCT.setEnabled(true);
    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void btnAddCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCTActionPerformed
        insert_HoaDonCT();

    }//GEN-LAST:event_btnAddCTActionPerformed

    private void btnUpdateCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateCTActionPerformed
        update_HoaDonCT();
    }//GEN-LAST:event_btnUpdateCTActionPerformed

    private void btnXoaCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaCTActionPerformed
        delete_HoaDonCT();
        LoadHoaDonToTable();
    }//GEN-LAST:event_btnXoaCTActionPerformed

    private void tblChiTietHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChiTietHoaDonMouseClicked
//        try {
//            int row = tblChiTietHoaDon.getSelectedRow();
//            int id = (int) tblChiTietHoaDon.getValueAt(row, 0);
//            HoaDonChiTiet hdct = ctDAO.findByID(id);
//            if (hdct!= null) {
//                setHoaDonCT(hdct);
//            }
//        } catch (Exception e) {
//        }
        this.editHoaDonCT();
    }//GEN-LAST:event_tblChiTietHoaDonMouseClicked

    private void btnNewCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCTActionPerformed
        txtMaHDCT.setText("");
        txtSoLuong.setText("0");
        txtThanhTien.setText("0");
        setStatusHoaDonCT(true);
    }//GEN-LAST:event_btnNewCTActionPerformed

    private void txtSoLuongKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoLuongKeyReleased
        LoadThanhTien();
    }//GEN-LAST:event_txtSoLuongKeyReleased

    boolean checkEmptyHoaDon(StringBuilder sb) {

        ValidateHelper.chekEmpyDemo(txtMaHD, sb, "s");
        if (sb.length() > 0) {
            DialogHelper.alert(this, "Không được để trống dữ liệu");
            return true;
        }
        return false;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddCT;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnNewCT;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnUpdateCT;
    private javax.swing.JButton btnXoaCT;
    private javax.swing.JComboBox<String> cboKhachHang;
    private javax.swing.JComboBox<String> cboSanPham;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblExcel;
    private javax.swing.JLabel lblPDF;
    private javax.swing.JTable tblChiTietHoaDon;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextPane txtCTGhiChu;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextPane txtGhiChu;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtMaHD2;
    private javax.swing.JLabel txtMaHDCT;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtNgayLap;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtThanhTien;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables
}
