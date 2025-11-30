package com.ciglgal1409.AAD.model;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter

public class Enrollment {

    private int studentId;
    private int moduleId;
    private LocalDate date;
}
