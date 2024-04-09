package com.fbo.financaspessoais.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillingRegisterDto {
    private String date;
    private String establishment;
    private String value;
}
