package harmonize.ErrorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import harmonize.DTOs.ResponseDTO;
import harmonize.ErrorHandling.Exceptions.EntityAlreadyExistsException;
import harmonize.ErrorHandling.Exceptions.EntityNotFoundException;
import harmonize.ErrorHandling.Exceptions.InternalServerErrorException;
import harmonize.ErrorHandling.Exceptions.InvalidArgumentException;
import harmonize.ErrorHandling.Exceptions.UnauthorizedException;
import harmonize.ErrorHandling.Exceptions.UserNotFriendException;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ResponseDTO> handleInternalServerErrorException(InternalServerErrorException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseDTO> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    @ExceptionHandler(UserNotFriendException.class)
    public ResponseEntity<ResponseDTO> handleUserNotFriendException(UserNotFriendException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDTO(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<ResponseDTO> handleInvalidArgumentException(InvalidArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ResponseDTO> handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDTO(HttpStatus.CONFLICT, e.getMessage()));
    }
}
