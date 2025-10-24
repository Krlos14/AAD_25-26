package com.ciglgal1409.AAD.model;

import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class student {
    private String name;
    private int id;
    private double nota;
}
