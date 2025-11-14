package ru.kpfu.tasktracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExceptionDto {

    private Integer statusCode;

    private String message;

    private Map<String, String> validationErrors;

}
