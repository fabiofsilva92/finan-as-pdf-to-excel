package com.fbo.financaspessoais.util;

import lombok.Data;

@Data
public class SharedInfo {

    private static String vencimento;
    private static String mesDaFatura;
    private static String total;

    public static String getVencimento() {
        return vencimento;
    }

    public static void setVencimento(String vencimento) {
        SharedInfo.vencimento = vencimento;
    }

    public static String getMesDaFatura() {
        return mesDaFatura;
    }

    public static void setMesDaFatura(String mesDaFatura) {
        SharedInfo.mesDaFatura = mesDaFatura;
    }

    public static String getTotal() {
        return total;
    }

    public static void setTotal(String total) {
        SharedInfo.total = total;
    }
}
