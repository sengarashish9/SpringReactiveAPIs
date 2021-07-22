package com.ashish.reactive.demo.demoreactive;

import com.ashish.reactive.demo.demoreactive.model.Student;
import com.ashish.reactive.demo.demoreactive.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoReactiveApplication.class, args);
	}

    @Bean
	CommandLineRunner init(ReactiveMongoOperations operations, StudentRepository studentRepository){
		return args -> {
			Flux<Student> studentFlux= Flux.just(
					new Student(null, "Ashish",65.5),
					new Student(null, "Aish",75.5),
					new Student(null, "Aman",85.5)
			).flatMap(student -> studentRepository.save(student));
			studentFlux.thenMany(studentRepository.findAll())
			.subscribe(student -> {
				System.out.println(student.getName());
			});

			/*operations.collectionExists(Student.class)
					.flatMap(ifExists->ifExists ? operations.dropCollection(Student.class) : Mono.just(ifExists))
					.thenMany(v->operations.createCollection(Student.class))
					.thenMany(studentFlux)
					.thenMany(studentRepository.findAll())
					.subscribe(System.out::println);*/
		};
	}
}
