package com.ark.construction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class AppUser extends BaseEntity {

    private String name;
    private String username;
    private String password;
    private String role;

}