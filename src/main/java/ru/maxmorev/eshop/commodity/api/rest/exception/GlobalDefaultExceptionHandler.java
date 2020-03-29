package ru.maxmorev.eshop.commodity.api.rest.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.maxmorev.eshop.commodity.api.rest.response.Message;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * Other errors
     * @param req
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Message handleBadRequest(HttpServletRequest req, Exception ex) {
        log.error(ex.getLocalizedMessage(), ex);
        Message responseMessage = new Message(Message.ERROR, req.getRequestURL().toString(), ex.getMessage(), Collections.EMPTY_LIST);
        return responseMessage;
    }

    /**
     * Processing HibernateException
     * @param req
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {org.springframework.dao.DataAccessException.class, PersistenceException.class})
    @ResponseBody
    public Message handleHibernateException(HttpServletRequest req, Exception ex) {
        Message responseMessage = new Message(Message.ERROR, req.getRequestURL().toString(), "Internal storage error", Collections.EMPTY_LIST);
        log.error("HibernateException {}", ex);
        return responseMessage;
    }

    /**
     * Validation errors
     * @param req request method
     * @param ex MethodArgumentNotValidException
     * @return @see ru.maxmorev.restful.eshop.rest.response.Message
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Message validationException(HttpServletRequest req, MethodArgumentNotValidException ex) {
        log.debug("Errors: {}", ex.getBindingResult().getAllErrors());
        List<Message.ErrorDetail> fieldsErrorDetails = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new Message.ErrorDetail(e.getField(), e.getDefaultMessage()))
                .collect(Collectors.toList());
        Message responseMessage = new Message(Message.ERROR, req.getRequestURL().toString(), "Validation error", fieldsErrorDetails);
        return responseMessage;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public Message handleIllegalArgumentException(HttpServletRequest req, Exception ex) {
        log.error(ex.getLocalizedMessage(), ex);
        Message responseMessage = new Message(Message.ERROR, req.getRequestURL().toString(), ex.getLocalizedMessage(), Collections.EMPTY_LIST);
        return responseMessage;
    }
}
