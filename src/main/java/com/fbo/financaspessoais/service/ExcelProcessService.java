package com.fbo.financaspessoais.service;

import com.fbo.financaspessoais.model.dtos.BillingRegisterDto;
import com.fbo.financaspessoais.util.SharedInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

@Service
@Slf4j
public class ExcelProcessService {

    @Autowired
    private BillingService billingService;

    // Format values according to Brazilian Portuguese locale
    private NumberFormat formatador = NumberFormat.getInstance(new Locale("pt", "BR"));

    /**
     * This method sets up a new Excel file based on the provided list of card data.
     *
     * @param listaCartao the list of card data containing date, establishment, and value
     * @return a byte array representing the newly created Excel file
     */
    public byte[] formatAndPopulateExcelSheet(List<BillingRegisterDto> listaCartao){

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SharedInfo.getMesDaFatura()); // Creates a new sheet using the current month


        for(int i = 0; i < listaCartao.size(); i++){
            Row row = sheet.createRow(i);
            Cell cellData = row.createCell(0);
            cellData.setCellValue(listaCartao.get(i).getDate()); // Sets the date value

            Cell cellEstabelecimento = row.createCell(1);
            cellEstabelecimento.setCellValue(listaCartao.get(i).getEstablishment()); // Sets the establishment value

            Cell cellValor = row.createCell(2);
            try{
                Number numero = formatador.parse(listaCartao.get(i).getValue());
                cellValor.setCellValue(numero.doubleValue()); // Sets the value, parsing it to a double
            } catch (ParseException e) {
                log.error("Error parsing values {}",listaCartao.get(i).getValue()); // Logs an error if parsing fails
                cellValor.setCellValue(listaCartao.get(i).getValue()); // Sets the value as is
            }

            if(listaCartao.get(i).getValue().contains("-")){
                cellValor.setCellStyle(getGreenColor(workbook)); // Applies the green style if the value is negative
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
            billingService.save(listaCartao);
            return outputStream.toByteArray(); // Returns the byte array representing the Excel file
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage()); // Throws a runtime exception if an IO error occurs
        }
        //Todo se tudo der certo, popular o banco assincronamente.
    }

    private XSSFCellStyle getGreenColor(XSSFWorkbook workbook){
        XSSFColor myColor = new XSSFColor(new java.awt.Color(153, 255, 153), null); // Example: Green color

        XSSFCellStyle greenStyle = workbook.createCellStyle();
        greenStyle.setFillForegroundColor(myColor);
        greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return greenStyle;
    }
}
