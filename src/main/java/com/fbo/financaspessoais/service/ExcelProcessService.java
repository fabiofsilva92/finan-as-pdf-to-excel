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


    public byte[] setupNewExcelFile(List<List<String>> listaCartao){

        //TODO Ajustar nao precisa mais da logica abaixo
//        mesFrequente = getMesMaisFrequente(dados);

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SharedInfo.getMesDaFatura());

        XSSFColor myColor = new XSSFColor(new java.awt.Color(153, 255, 153), null); // Exemplo: Cor verde

        XSSFCellStyle greenStyle = workbook.createCellStyle();
        greenStyle.setFillForegroundColor(myColor);
        greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        NumberFormat formatador = NumberFormat.getInstance(new Locale("pt", "BR"));


        for(int i = 0; i < listaCartao.size(); i++){
            Row row = sheet.createRow(i);
            Cell cellData = row.createCell(0);
            cellData.setCellValue(listaCartao.get(i).get(0));

            Cell cellEstabelecimento = row.createCell(1);
            cellEstabelecimento.setCellValue(listaCartao.get(i).get(1));

            Cell cellValor = row.createCell(2);
            try{
                Number numero = formatador.parse(listaCartao.get(i).get(2));
                double valor = numero.doubleValue();
                cellValor.setCellValue(valor);
            } catch (ParseException e) {
                log.error("Error parsing values {}",listaCartao.get(i).get(2));
                cellValor.setCellValue(listaCartao.get(i).get(2));
            }

            if(listaCartao.get(i).get(2).contains("-")){
                cellValor.setCellStyle(greenStyle);
            }
        }
        //Soma dos valores
        int numeroDeLinhas = listaCartao.size();
        Row rowTotal = sheet.createRow(numeroDeLinhas+1);
        rowTotal.createCell(1).setCellValue("TOTAL");
        Cell cellSoma = rowTotal.createCell(2);
        String referenciaColuna = "C"; // Coluna de valores
        cellSoma.setCellFormula("SUM(" + referenciaColuna + "1:" + referenciaColuna + numeroDeLinhas + ")");


        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            workbook.write(outputStream);
            log.info("Novo arquivo Excel criado com sucesso.");
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
