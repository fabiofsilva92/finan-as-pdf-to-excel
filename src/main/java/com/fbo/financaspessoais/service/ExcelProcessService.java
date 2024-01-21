package com.fbo.financaspessoais.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ExcelProcessService {

    //TODO, mandar gastos para planilha

//    public static void main(String[] args) throws IOException {
//        setupNewExcelFile();
//    }

//    public static void setupNewExcelFile(List<String> dados){
    public void setupNewExcelFile(List<String> dados){
        List<List<String>> listaCartao = new ArrayList<>();

        dados.forEach(d -> {
            listaCartao.add(processarDado(d));
        });



        String caminhoRelativo = "src/main/resources/static/seuArquivo2.xlsx";
//
        Workbook workbook = new XSSFWorkbook(); // Use XSSFWorkbook para formatos .xlsx
//
//        // Adicionar planilhas conforme necessário
        Sheet sheet = workbook.createSheet("DEZ");

        for(int i = 0; i < listaCartao.size(); i++){
            Row row = sheet.createRow(i);
            Cell cellData = row.createCell(0);
            cellData.setCellValue(listaCartao.get(i).get(0));

            Cell cellEstabelecimento = row.createCell(1);
            cellEstabelecimento.setCellValue(listaCartao.get(i).get(1));

            Cell cellValor = row.createCell(2);
            cellValor.setCellValue(listaCartao.get(i).get(2));
        }

        try (FileOutputStream fileOut = new FileOutputStream(caminhoRelativo)) {
            workbook.write(fileOut);
            System.out.println("Novo arquivo Excel criado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> processarDado(String dado) {
        String patternString = "(\\d{2}\\s(?:JAN|FEV|MAR|ABR|MAI|JUN|JUL|AGO|SET|OUT|NOV|DEZ))(.*)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(dado);
        String data = "";
        String segundaParte = "";

        if (matcher.find()) {
            data = matcher.group(1);
            segundaParte = matcher.group(2).trim();

        } else {
            System.out.println("String não corresponde ao padrão esperado.");
//            throw new RuntimeException("String não corresponde ao padrão esperado.");
        }
        return processarSegundaParte(data, segundaParte);
    }

    private List<String> processarSegundaParte(String data, String segundaParte) {
        String parte1Pattern = "(.*?)(\\d+,\\d+)";
        Pattern parte1PatternRegex = Pattern.compile(parte1Pattern);
        Matcher parte1Matcher = parte1PatternRegex.matcher(segundaParte);
        String parte1 = "";
        String parte2 = "";

        if (parte1Matcher.find()) {
            parte1 = parte1Matcher.group(1).trim();
            parte2 = parte1Matcher.group(2).trim();

        } else {
            System.out.println("Não corresponde ao padrão esperado na segunda parte: " + segundaParte);
//            throw new RuntimeException("Não corresponde ao padrão esperado na segunda parte: " + segundaParte);
        }
        return List.of(data, parte1, parte2);
    }

    private static void imprimirResultado(String data, String estabelecimento, String valor) {
        System.out.println("Data: " + data);
        System.out.println("Estabelecimento: " + estabelecimento);
        System.out.println("Valor: " + valor);
        System.out.println();
    }


}
