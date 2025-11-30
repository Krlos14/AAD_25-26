package com.ciglgal1409.AAD.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter

public class Student {
    private int id;
    private String nif;
    private String name;
    private String email;
    //private String curse;
    //private List<Module> modules;
}