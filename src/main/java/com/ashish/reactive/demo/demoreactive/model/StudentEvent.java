package com.ashish.reactive.demo.demoreactive.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentEvent {

    private String eventId;
    private String eventType;


}
