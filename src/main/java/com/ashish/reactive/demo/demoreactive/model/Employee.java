package com.ashish.reactive.demo.demoreactive.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@AllArgsConstructor
public class Employee {

    private String id;

    private String name;

    private Double marks;

    private List<String> city;

}
