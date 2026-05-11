package com.ark.construction.entity;

import com.ark.construction.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Client extends BaseEntity {

    private String name;

    private String phone;

    private String email;

    private String address;

    @OneToMany(mappedBy = "client")
    private List<Project> projects;
}