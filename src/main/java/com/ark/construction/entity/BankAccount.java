package com.ark.construction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class BankAccount extends BaseEntity {

    private String bankName;
    private String accountHolder;
    private String accountNumber;
    private String ifscCode;
    private String branchName;
    private String upiId;
    private Boolean isDefault = false;

}
