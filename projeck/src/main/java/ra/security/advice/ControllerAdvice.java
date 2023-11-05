package ra.security.advice;

import jdk.jshell.spi.ExecutionControl;
import jdk.jshell.spi.ExecutionControl.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailParseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ra.security.exception.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String>  loginFail(LoginException loginException){
        return new ResponseEntity<>(loginException.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> invalidRequest(MethodArgumentNotValidException ex){
        Map<String,String> err = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(c->{
            err.put(c.getField(),c.getDefaultMessage());
        });
        return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UserException.class)
    public String existed(ExecutionControl.UserException e){
        return e.getMessage();
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MyCustomRuntimeException.class)
    public ResponseEntity<String> handleMyCustomRuntimeException(MyCustomRuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>("Không xác thực", HttpStatus.FORBIDDEN);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String fileQuaLon(){
        return "File lớn quá, nhỏ hơn 5MB thôi";
    }

    @ExceptionHandler(CategoryException.class)
    public String handleExceptionCategory(CategoryException e) {
        return  e.getMessage();
    }

    @ExceptionHandler(OrderException.class)
    public String handleExceptionOrder(OrderException e) {
        return e.getMessage();
    }

    @ExceptionHandler(CartItemException.class)
    public String handleExceptionCartItem(CartItemException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ProductException.class)
    public String handleExceptionProduct(ProductException e) {
        return  e.getMessage();
    }

    @ExceptionHandler(ImageProductException.class)
    public String handleExceptionImage(ImageProductException e) {
        return  e.getMessage();
    }
    @ExceptionHandler(PromotionException.class)
    public String handleExceptionImage(PromotionException e) {
        return  e.getMessage();
    }
    @ExceptionHandler(ManufacturerException.class)
    public String handleManufacturerException(ManufacturerException e) {
        return  e.getMessage();
    }

}
