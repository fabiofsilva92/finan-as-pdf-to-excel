package com.fbo.financaspessoais.enums;

import java.util.regex.Pattern;

public enum PatternsEnum {

    VENCIMENTO_C6("Vencimento:\\s\\d{1,2}\\sde\\s(Janeiro|Fevereiro|Março|Abril|Maio|Junho|Julho|Agosto|Setembro|Outubro|Novembro|Dezembro)"),

    VENCIMENTO_NUBANK("Data do vencimento:\\s\\d{2}\\s(?i)(jan|fev|mar|abr|mai|jun|jul|ago|set|out|nov|dez)\\s\\d{4}"),
    LINHA_COM_VALORES("\\d{2} (?i)(jan|fev|mar|abr|mai|jun|jul|ago|set|out|nov|dez).*"),
    DATA_MAIS_DADOS("(\\d{2}\\s(?:JAN|FEV|MAR|ABR|MAI|JUN|JUL|AGO|SET|OUT|NOV|DEZ))(.*)"),
//    SEGUNDA_PARTE_VALOR("(.*?)(\\d+,\\d+)"),
    SEGUNDA_PARTE_VALOR("(.*?)((\\d+,\\d+)|(\\d+\\.\\d+,\\d+))"),
    MESES("Janeiro|Fevereiro|Março|Abril|Maio|Junho|Julho|Agosto|Setembro|Outubro|Novembro|Dezembro"),
    MESES_ABREVIADO("(?i)(jan|fev|mar|abr|mai|jun|jul|ago|set|out|nov|dez)"),
    VALOR_FATURA_NUBANK("(no valor de\\s+R\\$\\s+)((\\d+,\\d+)|(\\d+\\.\\d+,\\d+))"),
    VALOR_FATURA_C6("(no valor de\\s+R\\$\\s+)((\\d+,\\d+)|(\\d+\\.\\d+,\\d+))")
    ;


    private final Pattern pattern;

    PatternsEnum(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public Pattern getPattern() {
        return pattern;
    }

}
