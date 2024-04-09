package com.fbo.financaspessoais.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "billing_record")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "batch_id")
    private String batchId;
    private String date;
    private String establishment;
    private String value;
    private String month;
    private String dueMonth;
    private Integer year;
    private String dueDate;
    private String bank;
}