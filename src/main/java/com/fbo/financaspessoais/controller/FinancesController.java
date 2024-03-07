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
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + SharedInfo.getMesDaFatura() + "-Fatura-" + bank + ".xlsx")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(processService.processPDF(file, bank));
    }

}
