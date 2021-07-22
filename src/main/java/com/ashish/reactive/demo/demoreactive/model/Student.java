package com.ashish.reactive.demo.demoreactive.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.bind.annotation.XmlRootElement;

@Document
@Data
@AllArgsConstructor
@XmlRootElement
@NoArgsConstructor
public class Student {

    @Id
    private String id;

    private String name;

    private Double marks;

}
