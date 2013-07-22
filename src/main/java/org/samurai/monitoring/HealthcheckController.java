package org.samurai.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Developer: Jim Hazen
 * Date: 7/7/13
 * Time: 4:47 PM
 */
@Controller
public class HealthcheckController
{
    private static final Logger log = LoggerFactory.getLogger(HealthcheckController.class);
    private static final org.apache.log4j.Logger l4jLogger = org.apache.log4j.LogManager.getLogger(HealthcheckController.class);

    private static Map<String, String> GOOD = new HashMap<String, String>(1);

    static
    {
        GOOD.put("response", "good");
    }

    @RequestMapping(value = "/healthcheck", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<Map> healthcheck()
    {
        log.debug("ok -- from an SLF4J logger");
        l4jLogger.debug("ok -- from a native log4j logger that has been bridged to SLF4J.  No node change necessary.");
        return new ResponseEntity<Map>(GOOD, HttpStatus.OK);
    }
}
