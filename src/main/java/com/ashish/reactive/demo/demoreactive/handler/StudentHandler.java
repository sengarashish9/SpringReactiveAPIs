package com.ashish.reactive.demo.demoreactive.handler;

import com.ashish.reactive.demo.demoreactive.model.Student;
import com.ashish.reactive.demo.demoreactive.model.StudentEvent;
import com.ashish.reactive.demo.demoreactive.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class StudentHandler {

    @Autowired
    StudentRepository studentRepository;

    public Mono<ServerResponse> getAllStudents(ServerRequest serverRequest){
        Flux<Student> students=studentRepository.findAll();
        return  ServerResponse.ok().
                contentType(MediaType.APPLICATION_JSON)
                .body(students,Student.class);
    }

    public Mono<ServerResponse> getStudent(ServerRequest serverRequest){
        String id= serverRequest.pathVariable("id");
        Mono<Student> student=studentRepository.findById(id);

        return  student.flatMap(student_ ->
                                ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromObject(student_)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveStudent(ServerRequest serverRequest){
        Mono<Student> studentMono=serverRequest.bodyToMono(Student.class);

        return studentMono.flatMap(student ->
                ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentRepository.save(student),Student.class));
    }

    public Mono<ServerResponse> updateStudent(ServerRequest serverRequest){
        Mono<Student> newStudentMono=serverRequest.bodyToMono(Student.class);
        String id= serverRequest.pathVariable("id");
        Mono<Student> existingStudentMono=studentRepository.findById(id);
        Mono<ServerResponse> notFound=ServerResponse.notFound().build();

        return newStudentMono.zipWith(existingStudentMono,
                (newStudentMono_,existingStudentMono_)->
                        new Student(existingStudentMono_.getId(),
                                newStudentMono_.getName(), newStudentMono_.getMarks())
        ).flatMap(student -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        .body(studentRepository.save(student),Student.class)
        .switchIfEmpty(notFound));

    }

    public Mono<ServerResponse> deleteStudent(ServerRequest serverRequest){
        String id= serverRequest.pathVariable("id");
        Mono<Student> existingStudentMono=studentRepository.findById(id);
        Mono<ServerResponse> notFound=ServerResponse.notFound().build();
        return existingStudentMono.flatMap(student ->
                ServerResponse.ok().build(studentRepository.delete(student)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteAllStudent(ServerRequest serverRequest){
        return ServerResponse.ok().build(studentRepository.deleteAll());
    }


    public Mono<ServerResponse> getStudentEvents(ServerRequest serverRequest){
            Flux<StudentEvent> studentEventFlux=Flux.interval(Duration.ofSeconds(1))
                    .map(aLong -> new StudentEvent(String.valueOf(aLong),"New Student Event")
                    );
            return ServerResponse.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(studentEventFlux,StudentEvent.class);

    }
}
