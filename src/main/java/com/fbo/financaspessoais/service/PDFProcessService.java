package com.fbo.financaspessoais.service;

import com.fbo.financaspessoais.enums.BankEnum;
import com.fbo.financaspessoais.enums.ValoresIndesejadosEnum;
import com.fbo.financaspessoais.enums.Valuable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fbo.financaspessoais.enums.BankEnum.C6;

@Service
public class PDFProcessService {

    //TODO processamento do boleto


    public List<String> processPDF(MultipartFile file, String bank){
        List<String> linesWithValues = new ArrayList<>();

        switch (BankEnum.fromValue(bank)){
            case C6 :
                return readPDFC6(file);
            case NUBANK:
                return readPDFNubank(file);
            default:
                System.out.println("Falha no reconhecimento do banco");
                throw new RuntimeException("falha");
        }

    }


    public  List<String> readPDFC6(MultipartFile file){
        try {
			InputStream inputStream = file.getInputStream();

            StringBuffer sb = new StringBuffer();

            PdfReader reader = new PdfReader(inputStream);
            int numPages = reader.getNumberOfPages();

            for (int pageNum = 1; pageNum <= numPages; pageNum++) {
                String pageText = PdfTextExtractor.getTextFromPage(reader, pageNum);
                if (pageText.contains("Transações"))
                    sb.append("Texto da página " + pageNum + ":\n" + pageText + System.lineSeparator());
            }
            System.out.println("Todo conteúdo = " + sb.toString());
            List<String> valores = extrairLinhasComValores(sb.toString());


            valores.forEach(v -> {
                System.out.println("Conteúdo -> "+v);
            });

            reader.close();

            return valores;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<String> readPDFNubank(MultipartFile file){
        try {
			InputStream inputStream = file.getInputStream();
            StringBuffer sb = new StringBuffer();
            PdfReader reader = new PdfReader(inputStream);
            int numPages = reader.getNumberOfPages();

            for (int pageNum = 1; pageNum <= numPages; pageNum++) {
                String pageText = PdfTextExtractor.getTextFromPage(reader, pageNum);
                if(pageText.contains("TRANSAÇÕES"))
                    sb.append("Texto da página " + pageNum + ":\n" + pageText + System.lineSeparator());
            }

            System.out.println("Todo conteúdo = " + sb.toString());


            List<String> valores = extrairLinhasComValores(sb.toString());

            valores.forEach(v -> {
                System.out.println("Conteúdo -> "+v);
            });

            reader.close();

            return valores;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private static List<String> extrairLinhasComValores(String conteudo) {
        List<String> linhasComValores = new ArrayList<>();

        // Padrão para identificar linhas com valores monetários
        Pattern padraoValores = Pattern.compile("\\d{2} (?i)(jan|fev|mar|abr|mai|jun|jul|ago|set|out|nov|dez).*");


        // Criar um Matcher para encontrar correspondências no conteúdo
        Matcher matcher = padraoValores.matcher(conteudo);

        // Iterar sobre as correspondências e adicionar as linhas correspondentes à lista
        while (matcher.find()) {
            if(!contemValorIndesejado(matcher.group(), ValoresIndesejadosEnum.class))
                linhasComValores.add(matcher.group());
        }

        return linhasComValores;
    }

    private static <T extends Enum<T> & Valuable> boolean contemValorIndesejado(String linha, Class<T> enumClass){
        for (T valor : enumClass.getEnumConstants()) {
            if (linha.contains(valor.getValue())) {
//                System.out.println("valor > "+valor.getValue());
                return true; // Contém um valor indesejado, então retorna true
            }
//            System.out.println("valor > "+valor.getValue());
        }
        return false; // Não contém nenhum valor indesejado
    }



}
