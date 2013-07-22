package org.samurai.common.error.v10;

import org.hibernate.validator.method.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Once we start supporting multiple providers this will need to turn into a generalized error router.
 * Until then it'll hard code all ES consistent envelope.
 * User: Jim Hazen (jim.hazen@palm.com)
 * Date: 11/1/12
 * Time: 1:32 PM
 */
@RequestMapping("/errors/*")
@Controller
public class ErrorController
{
    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    @SuppressWarnings("deprecation")
    @RequestMapping("validationFailed")
    public
    @ResponseBody
    ResponseEntity<Map> handleValidationFailure(HttpServletRequest request)
    {

        ValidationException ex = (ValidationException) request.getAttribute("javax.servlet.error.exception");
        log.debug("Handling", ex);

        /*
        The 1.0 javax.validation API is somewhat of a mess.  Here we get validation exceptions.
        However there are no methods that grant access to what validation failed.
        The validation mechanism uses a pluggable provider approach with no ability to access the provider.
        Hard code support for Hibernate validator 4.3.1.  But be ready for this support to "evolve" in the next release.
        As you can see the MCVE is already deprecated and won't be there in the next release (5.0).

        NOTE: 5.0 is out and the code migration is easy, however it isn't backwards compatible and the 3.2.x series
        of the spring framework has "hibernate validator" support that is statically linked to HV 4.x and breaks
        on the 5.x series of HV.
         */
        MethodConstraintViolationException mcve = (MethodConstraintViolationException) ex;
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Validation errors: ");
        for (MethodConstraintViolation violation : mcve.getConstraintViolations())
        {
            errorMessage.append("[");
            errorMessage.append(violation.getMessage());
            errorMessage.append("]");
        }
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("error", "Validation");
        payload.put("errorDetails", errorMessage.toString());
        return new ResponseEntity<Map>(payload, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("404")
    public
    @ResponseBody
    ResponseEntity<Map> handle404(HttpServletRequest request) {

        Exception ex = (Exception) request.getAttribute("javax.servlet.error.exception");
        log.debug("Handling", ex);
        if (ex != null)
            log.info(ex.getMessage());

        Map<String, String> payload = new HashMap<String, String>();
        payload.put("error", "Missing resource");
        payload.put("errorDetails", "Unable to find or execute your call since it appears not to exist.  Please check the documentation and try again.");
        return new ResponseEntity<Map>(payload, HttpStatus.NOT_FOUND);    }

    @SuppressWarnings("deprecated")
    @RequestMapping("500")
    public
    @ResponseBody
    ResponseEntity<Map> handle500(HttpServletRequest request) {
        Throwable ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
        /*
        I think there is a classloader problem that causes MCVExceptions to not be understood by the TC web.xml exception handling mechanism.
        The same method of handling errors that works for auth errors doesn't work here.
        I tried a few things and finally gave up and went with this hack.
         */
        if (ValidationException.class.isAssignableFrom(ex.getClass()))
            return handleValidationFailure(request);

        log.warn("Handling uncaught exception", ex); //print after the known delegation

        Map<String, String> payload = new HashMap<String, String>();
        payload.put("error", "Unknown server side problem");
        payload.put("errorDetails", "An unknown error occurred.  Please consult logs or contact our support organization for assistance.");
        return new ResponseEntity<Map>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
