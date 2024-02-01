package com.fbo.financaspessoais.util;

import com.fbo.financaspessoais.enums.PatternsEnum;
import com.fbo.financaspessoais.enums.ValoresIndesejadosEnum;
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
public class NubankExtractorHelper implements ExtractorHelper{

    private static final Map<String, String> MES_PARA_MES_ANTERIOR = new HashMap<>();
    private String vencimento;
    private String mesDaFatura;
    private String total;

    static {
        MES_PARA_MES_ANTERIOR.put("JAN", "DEZ");
        MES_PARA_MES_ANTERIOR.put("FEV", "JAN");
        MES_PARA_MES_ANTERIOR.put("MAR", "FEV");
        MES_PARA_MES_ANTERIOR.put("ABR", "MAR");
        MES_PARA_MES_ANTERIOR.put("MAI", "ABR");
        MES_PARA_MES_ANTERIOR.put("JUN", "MAI");
        MES_PARA_MES_ANTERIOR.put("JUL", "JUN");
        MES_PARA_MES_ANTERIOR.put("AGO", "JUL");
        MES_PARA_MES_ANTERIOR.put("SET", "AGO");
        MES_PARA_MES_ANTERIOR.put("OUT", "SET");
        MES_PARA_MES_ANTERIOR.put("NOV", "OUT");
        MES_PARA_MES_ANTERIOR.put("DEZ", "NOV");
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

//        System.out.println("extrairMesVencimentoETotal -> pagetext = "+ pageText);
        setVencimento(pageText);
        setMesDaFatura(vencimento);
        setTotal(pageText);

    }

    public void setVencimento(String text) {

        Matcher matcher = PatternsEnum.VENCIMENTO_NUBANK.getPattern().matcher(text);

        if (matcher.find()) {
            log.info("VENCIMENTO_NUBANK -> " +matcher.group(0));
            this.vencimento = matcher.group(0);
            SharedInfo.setVencimento(matcher.group(0));
        }
    }

    public void setMesDaFatura(String text){
        Matcher matcher = PatternsEnum.MESES_ABREVIADO.getPattern().matcher(text);
        if(matcher.find()){
            log.info("MESES_ABREVIADO -> " +matcher.group(0));
            this.mesDaFatura = getMesDaFaturaAnteriorDoVencimento(matcher.group(0));
            SharedInfo.setMesDaFatura(this.mesDaFatura);
        }
    }

    public void setTotal(String text){
        Matcher matcher = PatternsEnum.VALOR_FATURA_NUBANK.getPattern().matcher(text);
        if(matcher.find()){
            log.info("VALOR_FATURA_NUBANK -> " +matcher.group(2));
            this.total = matcher.group(2);
            SharedInfo.setTotal(matcher.group(2));
        }
    }
}
