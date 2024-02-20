package com.example.errorhandling;
import com.example.response.ErrorResponse;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleBadRequestException(BadRequestException ex)
    {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception e) {
        // Log the exception details for debugging
        logger.error("Error: ", e);


        // Provide a response with more information
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please contact support with reference ID: " + generateReferenceId());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        //TODO: make it better. - that's just for now
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    private String generateReferenceId() {
        // Generate a unique reference ID for tracking the issue
        // You might use a timestamp or some other mechanism
        //TODO: make this method later
        return "REF-" + System.currentTimeMillis();
    }
}



