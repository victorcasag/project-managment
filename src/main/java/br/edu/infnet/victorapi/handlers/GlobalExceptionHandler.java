package br.edu.infnet.victorapi.handlers;

import br.edu.infnet.victorapi.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Entidade Não Encontrada")
                .message(ex.getMessage())
                .path(extractPath(request))
                .details(Map.of(
                        "entityName", ex.getEntityName() != null ? ex.getEntityName() : "unknown",
                        "field", ex.getField() != null ? ex.getField() : "unknown",
                        "value", ex.getValue() != null ? ex.getValue() : "unknown"
                ))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityInvalidException.class)
    public ResponseEntity<ErrorResponse> handleEntityInvalidException(
            EntityInvalidException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Dados Inválidos")
                .message(ex.getMessage())
                .path(extractPath(request))
                .details(Map.of(
                        "entityName", ex.getEntityName() != null ? ex.getEntityName() : "unknown",
                        "field", ex.getField() != null ? ex.getField() : "unknown",
                        "value", ex.getValue() != null ? ex.getValue() : "unknown"
                ))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExistsException(
            EntityAlreadyExistsException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflito de Dados")
                .message(ex.getMessage())
                .path(extractPath(request))
                .details(Map.of(
                        "entityName", ex.getEntityName() != null ? ex.getEntityName() : "unknown",
                        "field", ex.getField() != null ? ex.getField() : "unknown",
                        "value", ex.getValue() != null ? ex.getValue() : "unknown"
                ))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityDeactivatedException.class)
    public ResponseEntity<ErrorResponse> handleEntityDeactivatedException(
            EntityDeactivatedException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Entidade Desativada")
                .message(ex.getMessage())
                .path(extractPath(request))
                .details(Map.of(
                        "entityName", ex.getEntityName() != null ? ex.getEntityName() : "unknown",
                        "field", ex.getField() != null ? ex.getField() : "unknown",
                        "value", ex.getValue() != null ? ex.getValue() : "unknown"
                ))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BusinessOperationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessOperationException(
            BusinessOperationException ex, WebRequest request) {

        Map<String, Object> details = new HashMap<>();
        if (ex.getOperation() != null) details.put("operation", ex.getOperation());
        if (ex.getEntityName() != null) details.put("entityName", ex.getEntityName());
        if (ex.getEntityId() != null) details.put("entityId", ex.getEntityId());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error("Falha na Operação")
                .message(ex.getMessage())
                .path(extractPath(request))
                .details(details)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de Validação")
                .message(ex.getMessage())
                .path(extractPath(request))
                .details(Map.of("fieldErrors", ex.getFieldErrors()))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {

        Map<String, Object> details = new HashMap<>();
        if (ex.getResource() != null) details.put("resource", ex.getResource());
        if (ex.getAction() != null) details.put("action", ex.getAction());
        if (ex.getUserId() != null) details.put("userId", ex.getUserId());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Acesso Negado")
                .message(ex.getMessage())
                .path(extractPath(request))
                .details(details)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleSpringAccessDeniedException(
            org.springframework.security.access.AccessDeniedException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Acesso Negado")
                .message("Você não tem permissão para acessar este recurso")
                .path(extractPath(request))
                .details(Map.of("reason", "Permissões insuficientes"))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            org.springframework.security.authentication.BadCredentialsException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Credenciais Inválidas")
                .message("Email ou senha incorretos")
                .path(extractPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de Validação")
                .message("Dados de entrada inválidos")
                .path(extractPath(request))
                .details(Map.of("fieldErrors", fieldErrors))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {

        Map<String, String> fieldErrors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> existing
                ));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de Validação")
                .message("Violação de restrições")
                .path(extractPath(request))
                .details(Map.of("fieldErrors", fieldErrors))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        String message = String.format("Parâmetro '%s' deve ser do tipo %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Tipo de Parâmetro Inválido")
                .message(message)
                .path(extractPath(request))
                .details(Map.of(
                        "parameter", ex.getName(),
                        "providedValue", ex.getValue() != null ? ex.getValue().toString() : "null",
                        "requiredType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
                ))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Argumento Inválido")
                .message(ex.getMessage())
                .path(extractPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Erro Interno")
                .message("Ocorreu um erro interno no servidor")
                .path(extractPath(request))
                .details(Map.of("originalMessage", ex.getMessage()))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Erro Interno do Servidor")
                .message("Ocorreu um erro inesperado")
                .path(extractPath(request))
                .details(Map.of("originalMessage", ex.getMessage()))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}