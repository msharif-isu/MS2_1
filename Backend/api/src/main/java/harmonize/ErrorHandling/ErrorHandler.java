package harmonize.ErrorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import harmonize.DTOs.ErrorDTO;
import harmonize.ErrorHandling.Exceptions.InternalServerErrorException;
import harmonize.ErrorHandling.Exceptions.RoleNotFoundException;
import harmonize.ErrorHandling.Exceptions.RolePermissionException;
import harmonize.ErrorHandling.Exceptions.UnauthorizedException;
import harmonize.ErrorHandling.Exceptions.UnauthorizedUserException;
import harmonize.ErrorHandling.Exceptions.UserAlreadyFriendException;
import harmonize.ErrorHandling.Exceptions.UserAlreadyInvitedException;
import harmonize.ErrorHandling.Exceptions.UserFriendSelfException;
import harmonize.ErrorHandling.Exceptions.UserInfoInvalidException;
import harmonize.ErrorHandling.Exceptions.UserNotFoundException;
import harmonize.ErrorHandling.Exceptions.UserNotFriendException;
import harmonize.ErrorHandling.Exceptions.UsernameTakenException;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorDTO> handleInternalServerErrorException(InternalServerErrorException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDTO> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleRoleNotFoundException(RoleNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(UsernameTakenException.class)
    public ResponseEntity<ErrorDTO> handleUsernameTakenException(UsernameTakenException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(UserInfoInvalidException.class)
    public ResponseEntity<ErrorDTO> handleUserInfoInvalidException(UserInfoInvalidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(RolePermissionException.class)
    public ResponseEntity<ErrorDTO> handleRolePermissionException(RolePermissionException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyFriendException.class)
    public ResponseEntity<ErrorDTO> handleUserAlreadyFriendException(UserAlreadyFriendException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyInvitedException.class)
    public ResponseEntity<ErrorDTO> handleUserAlreadyInvitedException(UserAlreadyInvitedException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(UserNotFriendException.class)
    public ResponseEntity<ErrorDTO> handleUserNotFriendException(UserNotFriendException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(UserFriendSelfException.class)
    public ResponseEntity<ErrorDTO> handleUserFriendSelfException(UserFriendSelfException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ErrorDTO> handleUnauthorizedUserException(UnauthorizedUserException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO(HttpStatus.FORBIDDEN, e.getMessage()));
    }
}
