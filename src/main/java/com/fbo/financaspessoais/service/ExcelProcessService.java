package com.fbo.financaspessoais.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

//@Service
public class ExcelProcessService {

    //TODO, mandar gastos para planilha

    public static void main(String[] args) throws IOException {
        setupNewExcelFile();
    }

    public static void setupNewExcelFile(){
        String caminhoRelativo = "src/main/resources/static/seuArquivo.xlsx";

        Workbook workbook = new XSSFWorkbook(); // Use XSSFWorkbook para formatos .xlsx

        // Adicionar planilhas conforme necessário
        Sheet sheet1 = workbook.createSheet("Planilha1");
        Sheet sheet2 = workbook.createSheet("Planilha2");

        // Adicionar dados ou realizar outras operações conforme necessário

        // Salvar o arquivo
        try (FileOutputStream fileOut = new FileOutputStream(caminhoRelativo)) {
            workbook.write(fileOut);
            System.out.println("Novo arquivo Excel criado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
