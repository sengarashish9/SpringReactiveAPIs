package com.ashish.reactive.demo.demoreactive.routeconfig;

import com.ashish.reactive.demo.demoreactive.handler.StudentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouteConfig {

    @Bean
    RouterFunction<ServerResponse> routes(StudentHandler studentHandler){
        return route(GET("/handlerstudents").and(accept(MediaType.APPLICATION_JSON)),
                studentHandler::getAllStudents)
                .andRoute(POST("/handlerstudents").and(contentType(MediaType.APPLICATION_JSON)),
                        studentHandler::saveStudent)
                .andRoute(PUT("/handlerstudents/{id}").and(contentType(MediaType.APPLICATION_JSON)),
                        studentHandler::updateStudent)
                .andRoute(DELETE("/handlerstudents").and(accept(MediaType.APPLICATION_JSON)),
                        studentHandler::deleteAllStudent)
               .andRoute(GET("/handlerstudents/events").and(accept(MediaType.TEXT_EVENT_STREAM)),
                        studentHandler::getStudentEvents)
                .andRoute(GET("/handlerstudents/{id}").and(accept(MediaType.APPLICATION_JSON)),
                        studentHandler::getStudent)
                .andRoute(DELETE("/handlerstudents/{id}").and(accept(MediaType.APPLICATION_JSON)),
                        studentHandler::deleteStudent)
                ;
    }
}
