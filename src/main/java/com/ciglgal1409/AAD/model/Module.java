package com.ciglgal1409.AAD.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private Integer hours;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;
}