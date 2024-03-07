package com.fbo.financaspessoais.service;

import com.fbo.financaspessoais.enums.BankEnum;
import com.fbo.financaspessoais.enums.PatternsEnum;
import com.fbo.financaspessoais.enums.ValoresIndesejadosEnum;
import com.fbo.financaspessoais.enums.Valuable;
import com.fbo.financaspessoais.util.C6ExtractorHelper;
import com.fbo.financaspessoais.util.NubankExtractorHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


@Service
@Slf4j
public class PDFProcessService {

    //TODO REFATORAR TODO CODIGO

    @Autowired
    private ExcelProcessService excelProcessService;

    private BankEnum bankUtil;

    @Autowired
    private C6ExtractorHelper c6ExtractorHelper;

    @Autowired
    private NubankExtractorHelper nubankExtractorHelper;


    /**
     * Process the PDF file based on the specified bank.
     *
     * @param file The PDF file to process
     * @param bank The bank name
     * @return The processed PDF file as byte array
     * @throws RuntimeException if the bank is not recognized
     */
    public byte[] processPDF(MultipartFile file, String bank){

        // Initialize list to hold extracted strings from PDF
        List<List<String>> strings = new ArrayList<>();

        // Process PDF based on the specified bank
        switch (BankEnum.fromValue(bank)){
            case C6 :
                bankUtil = BankEnum.C6;
                log.info("Starting process -> C6 bank bill");
                strings = c6ExtractorHelper.extrairLinhasComValores(file);
                log.info("Successful reading PDF");
                // Process the extracted strings and return the result
                return excelProcessService.setupNewExcelFile(strings);
            case NUBANK:
                bankUtil = BankEnum.NUBANK;
                log.info("Starting process -> NUBANK bank bill");
                strings = nubankExtractorHelper.extrairLinhasComValores(file);
                // Process the extracted strings and return the result
                return excelProcessService.setupNewExcelFile(strings);
            default:
                // Throw an exception if the bank is not recognized
                System.out.println("Falha no reconhecimento do banco");
                throw new RuntimeException("falha");
        }

    }

}
