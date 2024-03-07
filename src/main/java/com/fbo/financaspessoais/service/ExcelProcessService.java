package com.fbo.financaspessoais.service;

import com.fbo.financaspessoais.enums.PatternsEnum;
import com.fbo.financaspessoais.util.SharedInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExcelProcessService {

    /**
     * This method sets up a new Excel file based on the provided list of card data.
     *
     * @param listaCartao the list of card data containing date, establishment, and value
     * @return a byte array representing the newly created Excel file
     */
    public byte[] setupNewExcelFile(List<List<String>> listaCartao){

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SharedInfo.getMesDaFatura()); // Creates a new sheet using the current month

        XSSFColor myColor = new XSSFColor(new java.awt.Color(153, 255, 153), null); // Example: Green color

        XSSFCellStyle greenStyle = workbook.createCellStyle();
        greenStyle.setFillForegroundColor(myColor);
        greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        NumberFormat formatador = NumberFormat.getInstance(new Locale("pt", "BR")); // Format values according to Brazilian Portuguese locale


        for(int i = 0; i < listaCartao.size(); i++){
            Row row = sheet.createRow(i);
            Cell cellData = row.createCell(0);
            cellData.setCellValue(listaCartao.get(i).get(0)); // Sets the date value

            Cell cellEstabelecimento = row.createCell(1);
            cellEstabelecimento.setCellValue(listaCartao.get(i).get(1)); // Sets the establishment value

            Cell cellValor = row.createCell(2);
            try{
                Number numero = formatador.parse(listaCartao.get(i).get(2));
                cellValor.setCellValue(numero.doubleValue()); // Sets the value, parsing it to a double
            } catch (ParseException e) {
                log.error("Error parsing values {}",listaCartao.get(i).get(2)); // Logs an error if parsing fails
                cellValor.setCellValue(listaCartao.get(i).get(2)); // Sets the value as is
            }

            if(listaCartao.get(i).get(2).contains("-")){
                cellValor.setCellStyle(greenStyle); // Applies the green style if the value is negative
            }
        }


        int numeroDeLinhas = listaCartao.size();
        Row rowTotal = sheet.createRow(numeroDeLinhas+1);
        rowTotal.createCell(1).setCellValue("TOTAL");
        Cell cellSoma = rowTotal.createCell(2);
        String referenciaColuna = "C"; // Column of values
        cellSoma.setCellFormula("SUM(" + referenciaColuna + "1:" + referenciaColuna + numeroDeLinhas + ")"); // Sets the formula for summing the values

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            workbook.write(outputStream); // Writes the workbook to the output stream
            log.info("New Excel file created successfully."); // Logs a success message
            return outputStream.toByteArray(); // Returns the byte array representing the Excel file
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage()); // Throws a runtime exception if an IO error occurs
        }
    }

}
