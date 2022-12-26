package com.example.excercise_lab10;

import java.io.Serializable;

public class deadline implements Serializable
        {


    private String monHoc;
    private String ngayGiao;
    private String hanNop;
    private String NoiDung;
    private String TrangThai;
    private  int stt;


    public deadline(){};

    public deadline(String monHoc, String ngayGiao, String hanNop, String noiDung, String trangThai, int stt) {
        this.monHoc = monHoc;
        this.ngayGiao = ngayGiao;
        this.hanNop = hanNop;
        NoiDung = noiDung;
        TrangThai = trangThai;
        this.stt = stt;
    }

            public String getMonHoc() {
                return monHoc;
            }

            public void setMonHoc(String monHoc) {
                this.monHoc = monHoc;
            }

            public String getNgayGiao() {
                return ngayGiao;
            }

            public void setNgayGiao(String ngayGiao) {
                this.ngayGiao = ngayGiao;
            }

            public String getHanNop() {
                return hanNop;
            }

            public void setHanNop(String hanNop) {
                this.hanNop = hanNop;
            }

            public String getNoiDung() {
                return NoiDung;
            }

            public void setNoiDung(String noiDung) {
                NoiDung = noiDung;
            }

            public String getTrangThai() {
                return TrangThai;
            }

            public void setTrangThai(String trangThai) {
                TrangThai = trangThai;
            }

            public int getStt() {
                return stt;
            }

            public void setStt(int stt) {
                this.stt = stt;
            }
        }
