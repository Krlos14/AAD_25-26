package com.ciglgal1409.AAD.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter

public class Module {
    private int id;
    private String code;
    private String name;
    private int hours;
}
