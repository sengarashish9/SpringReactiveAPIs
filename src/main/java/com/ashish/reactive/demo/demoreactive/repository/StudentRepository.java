package com.ashish.reactive.demo.demoreactive.repository;

import com.ashish.reactive.demo.demoreactive.model.Student;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface StudentRepository
        extends ReactiveMongoRepository<Student,String> {

    Flux<Student> findByNameOrderByMarks(Publisher<String> name);

    Flux<Student> findByName(String name);

}
