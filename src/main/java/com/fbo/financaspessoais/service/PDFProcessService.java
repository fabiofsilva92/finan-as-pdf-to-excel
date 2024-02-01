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


    public byte[] processPDF(MultipartFile file, String bank){
        List<String> linesWithValues = new ArrayList<>();

        switch (BankEnum.fromValue(bank)){
            case C6 :
                bankUtil = BankEnum.C6;
                log.info("Starting process -> C6 bank bill");
                List<List<String>> strings = c6ExtractorHelper.extrairLinhasComValores(file);
                log.info("Successful reading PDF");
                byte[] bytes = excelProcessService.setupNewExcelFile(strings);
                return bytes;
            case NUBANK:
                bankUtil = BankEnum.NUBANK;
                log.info("Starting process -> NUBANK bank bill");
                List<List<String>> strings1 = nubankExtractorHelper.extrairLinhasComValores(file);
                byte[] bytes2 = excelProcessService.setupNewExcelFile(strings1);
                return bytes2;
            default:
                System.out.println("Falha no reconhecimento do banco");
                throw new RuntimeException("falha");
        }

    }

}
