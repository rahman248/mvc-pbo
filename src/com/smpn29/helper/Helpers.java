package com.smpn29.helper;

/**
 * @Author : Rahman S
 * @Project : smpn29jkt
 * @Create : 18, 18/06/2022 06 2022
 * @Package : com.smpn29.helper
 **/
public class Helpers {
    public static String getTextBulan(int bulanIndex) {
        String[] textBulan = new String[]{
                "Januari",
                "Februari",
                "Maret",
                "April",
                "Mei",
                "Juni",
                "Juli",
                "Agustus",
                "September",
                "Oktober",
                "November",
                "Desember"
        };

        return textBulan[bulanIndex];
    }
}
