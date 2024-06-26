package com.fbo.financaspessoais.util;

import com.fbo.financaspessoais.enums.PatternsEnum;
import com.fbo.financaspessoais.enums.ValoresIndesejadosEnum;
import com.fbo.financaspessoais.enums.Valuable;
import com.fbo.financaspessoais.model.BillingRecord;
import com.fbo.financaspessoais.model.dtos.BillingRegisterDto;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ExtractorHelper {

    public String getMesDaFaturaAnteriorDoVencimento(String mes);
    public default List<BillingRegisterDto> extrairLinhasComValores(MultipartFile file) throws RuntimeException {
        try{
            InputStream inputStream = file.getInputStream();
            StringBuffer sb = new StringBuffer();
            PdfReader reader = new PdfReader(inputStream);
            int numPages = reader.getNumberOfPages();

            for (int pageNum = 1; pageNum <= numPages; pageNum++) {
                String pageText = PdfTextExtractor.getTextFromPage(reader, pageNum);
                if(pageNum == 1){
                    extrairMesVencimentoETotal(pageText);
                }

                if (pageText.contains("Transações") || pageText.contains("TRANSAÇÕES")){
                    sb.append("Texto da página " + pageNum + ":\n" + pageText + System.lineSeparator());
                }
            }

            List<String> linhasComValores = new ArrayList<>();

            reader.close();

            // Padrão para identificar linhas com valores monetários
            // Criar um Matcher para encontrar correspondências no conteúdo
            Matcher matcher;
            matcher = PatternsEnum.LINHA_COM_VALORES.getPattern().matcher(sb.toString());

            // Iterar sobre as correspondências e adicionar as linhas correspondentes à lista
            while (matcher.find()) {
                if(!contemValorIndesejado(matcher.group(), ValoresIndesejadosEnum.class))
                    linhasComValores.add(matcher.group().toUpperCase());
            }

            List<BillingRegisterDto> listToReturn = new ArrayList<>();

            linhasComValores.forEach(d -> {
                listToReturn.add(processarDado(d));
            });

            return listToReturn;
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public void extrairMesVencimentoETotal(String pageText);

    public default <T extends Enum<T> & Valuable> boolean contemValorIndesejado(String linha, Class<T> enumClass){
        for (T valor : enumClass.getEnumConstants()) {
            if (linha.contains(valor.getValue())) {
                return true; // Contém um valor indesejado, então retorna true
            }
        }
        return false; // Não contém nenhum valor indesejado
    }

    private BillingRegisterDto processarDado(String dado) {
        Pattern pattern = PatternsEnum.DATA_MAIS_DADOS.getPattern();
        Matcher matcher = pattern.matcher(dado);
        String data = "";
        String segundaParte = "";

        if (matcher.find()) {
            data = matcher.group(1);
            segundaParte = matcher.group(2).trim();

        } else {
            System.out.println(("String não corresponde ao padrão esperado. -> "+dado));
        }
        return processarSegundaParte(data, segundaParte);
    }

    private BillingRegisterDto processarSegundaParte(String data, String segundaParte) {
        Pattern estabValorPattern = PatternsEnum.SEGUNDA_PARTE_VALOR.getPattern();
        Matcher estabValorMatcher = estabValorPattern.matcher(segundaParte);
        String parte1 = "";
        String parte2 = "";

        if (estabValorMatcher.find()) {

            parte1 = estabValorMatcher.group(1).trim();
            parte2 = !parte1.toLowerCase().contains("desconto") ? estabValorMatcher.group(2).trim() : "-"+estabValorMatcher.group(2).trim();

        } else {
            System.out.println("Não corresponde ao padrão esperado na segunda parte: " + segundaParte);
        }
        //TODO ajustar para o BillingRecord

        return BillingRegisterDto.builder()
                .date(data)
                .establishment(parte1)
                .value(parte2).build();
    }


}
