package com.powerlifting.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Atleta {
    private Long id;
    private String name;
    private Double pesoCorporal;
}
