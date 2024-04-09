package com.fbo.financaspessoais.util;

import com.fbo.financaspessoais.enums.BankEnum;
import com.fbo.financaspessoais.enums.PatternsEnum;
import com.fbo.financaspessoais.enums.ValoresIndesejadosEnum;
import com.fbo.financaspessoais.enums.Valuable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

@Getter
@Slf4j
@Component
public class C6ExtractorHelper implements ExtractorHelper{

    private static final Map<String, String> MES_PARA_MES_ANTERIOR = new HashMap<>();
    private String vencimento;
    private String mesDaFatura;
    private String total;

    static {
        MES_PARA_MES_ANTERIOR.put("Janeiro", "DEZ");
        MES_PARA_MES_ANTERIOR.put("Fevereiro", "JAN");
        MES_PARA_MES_ANTERIOR.put("Março", "FEV");
        MES_PARA_MES_ANTERIOR.put("Abril", "MAR");
        MES_PARA_MES_ANTERIOR.put("Maio", "ABR");
        MES_PARA_MES_ANTERIOR.put("Junho", "MAI");
        MES_PARA_MES_ANTERIOR.put("Julho", "JUN");
        MES_PARA_MES_ANTERIOR.put("Agosto", "JUL");
        MES_PARA_MES_ANTERIOR.put("Setembro", "AGO");
        MES_PARA_MES_ANTERIOR.put("Outubro", "SET");
        MES_PARA_MES_ANTERIOR.put("Novembro", "OUT");
        MES_PARA_MES_ANTERIOR.put("Dezembro", "NOV");
    }

    @Override
    public String getMesDaFaturaAnteriorDoVencimento(String mes) {
        String mesAnterior = MES_PARA_MES_ANTERIOR.get(mes);

        if (mesAnterior == null) {
            log.error("Mês indefinido - ERRO");
            throw new RuntimeException("Mês de vencimento não encontrado, tente novamente");
        }

        return mesAnterior;
    }

    @Override
    public void extrairMesVencimentoETotal(String pageText) {

        setVencimento(pageText);
        setMesDaFatura(vencimento);
        setTotal(pageText);
    }

    public void setVencimento(String text){
        Matcher matcher = PatternsEnum.VENCIMENTO_C6.getPattern().matcher(text);
        if (matcher.find()) {
            log.info("Vencimento_C6 -> " +matcher.group(0));
            this.vencimento = matcher.group(0);
            SharedInfo.setVencimento(matcher.group(0));
            log.info("Encontrado vencimento -> "+ vencimento);
        }
    }

    public void setMesDaFatura(String text){
        Matcher matcher = PatternsEnum.MESES.getPattern().matcher(vencimento);
        if(matcher.find()){
            log.info("MESES_ABREVIADO -> " +matcher.group(0));
            SharedInfo.setMesDeVencimento(matcher.group(0));
            this.mesDaFatura = getMesDaFaturaAnteriorDoVencimento(matcher.group(0));
            SharedInfo.setMesDaFatura(this.mesDaFatura);
        }
    }

    public void setTotal(String text){
        Matcher matcher = PatternsEnum.VALOR_FATURA_C6.getPattern().matcher(text);
        if(matcher.find()){
            log.info("VALOR_FATURA_C6 -> " +matcher.group(2));
            this.total = matcher.group(2);
            SharedInfo.setTotal(matcher.group(2));
        }
    }

}
