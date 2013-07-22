package org.samurai.logging.v10;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * This goofy little controller allows me to mistype a logging API without the rest of the project getting confused and thinking
 * that I'm trying to do something on a non-existing project resource. Now you get a reasonable error message instead of
 * a 500.
 *
 * This is because the admin/logger namespace semantics are different from the request handling in the rest of the project.
 * This is akin to having a custom 404 controller for the logging API to ensure requests don't accidentally fall
 * through into your project's controller logic.
 *
 * User: Jim Hazen
 * Date: 6/11/13
 * Time: 6:26 PM
 */
@Controller
public class UnknownLoggingAPIController
{
    @RequestMapping("/1.0/admin/logging/{api}/{action}")
    public @ResponseBody
    ResponseEntity<Map> unknownAPIAction(@PathVariable String api,
                                         @PathVariable String action)
    {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("error", "Missing resource");
        payload.put("errorDetails", ""+api+"/"+action+" is not a supported logging API.  Please check documentation and try again");
        return new ResponseEntity<Map>(payload, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/1.0/admin/logging/{api}")
    public @ResponseBody
    ResponseEntity<Map> unknownAPI(@PathVariable String api)
    {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("error", "Missing resource");
        payload.put("errorDetails", ""+api+" is not a supported logging API.  Please check documentation and try again");
        return new ResponseEntity<Map>(payload, HttpStatus.NOT_FOUND);
    }
}
