package com.lrs.servelt;

import com.lrs.mvc.Response;
import com.lrs.rest.exception.RestException;
import com.lrs.utils.ExceptionUtils;
import io.undertow.server.HttpServerExchange;
import io.undertow.servlet.api.ExceptionHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by fcambarieri on 27/02/16.
 */
public class RestExceptionHandler implements ExceptionHandler {

    Log log = LogFactory.getLog(RestExceptionHandler.class);

    final ObjectMapper mapper = JsonFactory.create();

    @Override
    public boolean handleThrowable(HttpServerExchange exchange, ServletRequest request, ServletResponse response, Throwable throwable) {

        try {

            log.debug ("Catched exception", throwable);

            int status = 500;
            String message = throwable.getMessage();
            String error = "Internal Error";
            Throwable cause = ExceptionUtils.cause(throwable);

            if (throwable instanceof RestException) {
                RestException exception = (RestException)throwable;
                status = exception.getHttpCodeError().getCode();
                error = exception.getHttpCodeError().getErrorCode();
            }


            Response errorResponse = Response.creatErrorBuilder()
                    .setError(error)
                    .setCause(cause)
                    .setMessage(message)
                    .setHttpCode(status)
                    .setContentType("application/json")
                    .build();

            String json = mapper.toJson(errorResponse.getBody());


            exchange.setResponseCode(errorResponse.getHttpCode());
            exchange.getResponseSender().send(json);

        } catch (Throwable t) {
            log.error("Error chaching exception", t);
            exchange.setResponseCode(567);
            exchange.getResponseSender().send(String.format("{'message':%s}", t.getMessage()));
        }


        return false;
    }
}
