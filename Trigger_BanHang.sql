
delete HoaDonChiTiet where MaHoaDonCT=5
insert HoaDonChiTiet values('HD001','SP03',8,0,'')
update HoaDonChiTiet set SoLuong =5 where MaHoaDonCT=6
select * from HoaDon
select * from HoaDonChiTiet
select * from SanPham
go
/*Trigger thêm hóa đơn cập nhập kho hàng*/
create trigger trg_DatHang on HoaDonChiTiet AFTER INSERT AS
Begin
	update SanPham
	set SoLuong = SanPham.SoLuong -(select SoLuong from inserted where MaSP = SanPham.MaSP)
	from SanPham
	join inserted on SanPham.MaSP = inserted.MaSP
end
go
/* Trigger xóa hóa đơn cập nhập kho hàng */

create trigger xoaHoaDon_updateKho on HoaDonChiTiet for delete as
begin
	update SanPham
	set SoLuong = SanPham.SoLuong +(select SoLuong from deleted where MaSP = SanPham.MaSP)
	from SanPham
	join deleted on SanPham.MaSP = deleted.MaSP
end
go
/* cập nhập kho khi đơn hàng đc cập nhập (tăng lên hoặc hạ xuống) */
create trigger tgr_updateDonHang_updateKho on HoaDonChiTiet after update as
begin
	update SanPham
	set SoLuong = SanPham.SoLuong - 
	(select SoLuong from inserted where MaSP=SanPham.MaSP)+
	(select SoLuong from deleted where MaSP = SanPham.MaSP)
	from SanPham join deleted on SanPham.MaSP=deleted.MaSP
end
go


select sum(HoaDonChiTiet.TongTien) as 'Tổng Tiền Hóa Đơn', HoaDon.MaHoaDon 
from HoaDonChiTiet join HoaDon on HoaDonChiTiet.MaHoaDon=HoaDon.MaHoaDon
where HoaDon.MaHoaDon='HD001' group by HoaDon.MaHoaDon

go
/* trigger quản lý kho*/

select * from SanPham
select * from PhieuNhapCT
update PhieuNhapCT set SoLuong=2 where MaCTPN=8
insert into PhieuNhapCT values('PN001','SP02',5,'400000','2000000','')
go
create trigger tgr_NhapHang_capnhapSP on PhieuNhapCT after insert as
 begin
		update SanPham
		set SoLuong = SanPham.SoLuong + (select SoLuong from inserted where MaSP = SanPham.MaSP)
		from SanPham join inserted on SanPham.MaSP=inserted.MaSP
 end
 go	

 create trigger tgr_SuaPhieu_CapNhapKho on PhieuNhapCT for update as 
 begin	
	update SanPham
	set SoLuong = SanPham.SoLuong + 
	(select SoLuong from inserted where MaSP=SanPham.MaSP)-
	(select SoLuong from deleted where MaSP = SanPham.MaSP)
	from SanPham join deleted on SanPham.MaSP=deleted.MaSP
end
go

/* thông kê sản phẩm bán chạy*/
select SanPham.MaSP , sum(HoaDonChiTiet.SoLuong) as 'Số luongj sản phẩm' 
from SanPham join HoaDonChiTiet on SanPham.MaSP=HoaDonChiTiet.MaSP group by SanPham.MaSP

select sum(HoaDon.TongTien) as 'Tổng Tiên', MONTH(NgayBan)as 'Tháng'
from HoaDon group by MONTH(NgayBan)