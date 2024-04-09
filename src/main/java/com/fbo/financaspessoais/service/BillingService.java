package com.fbo.financaspessoais.service;

import com.fbo.financaspessoais.model.BillingRecord;
import com.fbo.financaspessoais.model.dtos.BillingRegisterDto;
import com.fbo.financaspessoais.repositories.BillingRepository;
import com.fbo.financaspessoais.util.SharedInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    public void save(List<BillingRegisterDto> billingRegisterDtoList) {

        var batchIdForList = SharedInfo.getMesDaFatura()+"-"+UUID.randomUUID();

        log.info("generated batch_id: "+batchIdForList);

        billingRegisterDtoList.forEach(billingRegisterDto -> {
            BillingRecord bRecord = BillingRecord.builder()
                    .value(billingRegisterDto.getValue())
                    .date(billingRegisterDto.getDate())
                    .establishment(billingRegisterDto.getEstablishment())
                    .batchId(batchIdForList)
                    .month(SharedInfo.getMesDaFatura())
                    .dueMonth(SharedInfo.getMesDeVencimento())
                    .dueDate(SharedInfo.getVencimento())
                    .year(SharedInfo.getMesDeVencimento().equalsIgnoreCase("jan")? (LocalDate.now().getYear()-1) : LocalDate.now().getYear())
                    .bank(SharedInfo.getBanco().toUpperCase())
                    .build();
            log.info("billingRecord: "+bRecord.toString());
            billingRepository.save(bRecord);
        });

        List<BillingRecord> all = billingRepository.findAll();
        System.out.println(all);

    }

}
