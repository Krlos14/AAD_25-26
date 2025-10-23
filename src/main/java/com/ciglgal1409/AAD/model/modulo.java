package com.ciglgal1409.AAD.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class modulo {
    private String code;
    private String name;

    public modulo(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
