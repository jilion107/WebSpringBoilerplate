package com.xmnjm.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by jilion.chen on 3/3/2017.
 */
@Entity
@Table(name="company")
public class Company {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "name")
    private String companyName;

    public Company(String companyName) {
        this.companyName = companyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return companyName;
    }

    public void setName(String name) {
        this.companyName = name;
    }
}
