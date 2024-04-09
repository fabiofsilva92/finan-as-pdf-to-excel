package com.fbo.financaspessoais.model;

import lombok.Data;

import java.util.Date;


@Data
public class BillingBatch {
    private String batchId;
    private Date creationDate;
    private Object object;
}
