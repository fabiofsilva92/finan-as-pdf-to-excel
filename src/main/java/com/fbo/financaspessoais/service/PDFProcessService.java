package com.fbo.financaspessoais.service;

import com.fbo.financaspessoais.enums.BankEnum;
import com.fbo.financaspessoais.util.C6ExtractorHelper;
import com.fbo.financaspessoais.util.NubankExtractorHelper;


import com.fbo.financaspessoais.util.SharedInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;


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

        SharedInfo.setBanco(bank);


        switch (BankEnum.fromValue(bank)){
            case C6 :
                bankUtil = BankEnum.C6;
                log.info("Starting process -> C6 bank bill");
                var listWithValuesC6 = c6ExtractorHelper.extrairLinhasComValores(file);
                log.info("Successful reading PDF");
                return excelProcessService.formatAndPopulateExcelSheet(listWithValuesC6);
            case NUBANK:
                bankUtil = BankEnum.NUBANK;
                log.info("Starting process -> NUBANK bank bill");
                var listWithValuesNubank = nubankExtractorHelper.extrairLinhasComValores(file);
                return excelProcessService.formatAndPopulateExcelSheet(listWithValuesNubank);
            default:
                log.info("Falha no reconhecimento do banco");
                throw new RuntimeException("falha");
        }

    }

}
