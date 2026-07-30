package com.ark.construction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class Client extends BaseEntity{
    private String name;
    private String phone;
    private String email;
    private String address;
    @OneToMany(mappedBy = "client")
    private List<Project> projects;

}