package com.ashish.reactive.demo.demoreactive.controller;

import com.ashish.reactive.demo.demoreactive.model.Student;
import com.ashish.reactive.demo.demoreactive.model.StudentEvent;
import com.ashish.reactive.demo.demoreactive.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    Flux<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("{id}")
    Mono<ResponseEntity<Student>> findById(@PathVariable String id) {
        return studentRepository.findById(id)
                .map(student -> ResponseEntity.ok(student))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Student> saveStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }


    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Student>> updateStudent(@PathVariable(value = "id") String id,
                                                       @RequestBody Student student) {
        return studentRepository.findById(id)
                .flatMap(oldStudent -> {
                    oldStudent.setName(student.getName());
                    oldStudent.setMarks(student.getMarks());
                    return studentRepository.save(oldStudent);
                })
                .map(updatedStudent -> ResponseEntity.ok(updatedStudent))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteStudent(@PathVariable(value = "id") String id) {
        return studentRepository.findById(id)
                .flatMap(existingStudent -> studentRepository.delete(existingStudent))
                .then(Mono.just(ResponseEntity.ok().<Void>build())
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StudentEvent> getStudentEvents(){
        return Flux.interval(Duration.ofSeconds(1))
                .map(aLong ->
                        new StudentEvent(String.valueOf(aLong),"new Student Event")
                );
    }

}
