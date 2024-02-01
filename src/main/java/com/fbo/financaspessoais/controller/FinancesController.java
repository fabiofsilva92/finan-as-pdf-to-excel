package com.fbo.financaspessoais.controller;

import com.fbo.financaspessoais.service.PDFProcessService;
import com.fbo.financaspessoais.util.SharedInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping
public class FinancesController {

    @Autowired
    private PDFProcessService processService;

    @PostMapping("process")
    public ResponseEntity<byte[]> processFatura(@RequestPart("file") MultipartFile file,
                                                      @RequestParam String bank){
        byte[] bytes = processService.processPDF(file, bank);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", SharedInfo.getMesDaFatura() +"-Fatura-"+bank+".xlsx");
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

}
