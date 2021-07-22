package com.ashish.reactive.demo.demoreactive;

import com.ashish.reactive.demo.demoreactive.model.Employee;
import com.ashish.reactive.demo.demoreactive.model.Student;

import java.util.*;
import java.util.stream.Collectors;

public class TestApp {
    public static void main(String[] args) {
       Employee e1= new Employee(null, "Ashish",65.5,Collections.singletonList("GLASGOW"));
       Employee e2= new Employee(null, "Aish",75.5, Collections.singletonList("LONDON"));
       Employee e3= new Employee(null, "Aman",85.5,Collections.singletonList("MANCHESTER"));

        List<Employee> emps=new ArrayList<>();
        emps.add(e1);
        emps.add(e2);
        emps.add(e3);
        System.out.println(emps.stream().map(employee -> employee.getCity())
                .collect(Collectors.toList()));

        System.out.println(emps.stream().map(employee -> employee.getCity())
                .flatMap(Collection::stream).collect(Collectors.toList()));

        System.out.println(emps.stream()
                .filter(employee -> employee.getCity().get(0).equalsIgnoreCase("GLASGOW"))
                .findFirst().get().getName());

        Optional<Employee> optionalEmployee=emps.stream()
                .filter(employee -> employee.getCity().get(0).equalsIgnoreCase("XXX"))
                .findFirst();
        System.out.println(optionalEmployee.isPresent());

        /*Collections.sort(emps, new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return 0;
            }
        });*/
       //emps.sort((Employee emp1, Employee emp2)->emp1.getMarks().compareTo(emp2.getMarks()));


    }
}

@FunctionalInterface
interface MyInterface{
    void test();
}
