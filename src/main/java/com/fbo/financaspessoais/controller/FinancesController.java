package com.fbo.financaspessoais.controller;

import com.fbo.financaspessoais.service.PDFProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping
public class FinancesController {
    //TODO controller

    @Autowired
    private PDFProcessService processService;

    @PostMapping("process")
    public ResponseEntity<List<String>> processFatura(@RequestPart("file") MultipartFile file,
                                                      @RequestParam String bank){
        List<String> strings = processService.processPDF(file, bank);

        return ResponseEntity.ok().body(strings);
    }

}
