package com.core.auditing;

import java.lang.annotation.*;
/**
 * @author zunnoon.farooqui
 * This is a custom annotation created for EDA-micro-services method level auditing.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Auditable {

    String isEnabled() default "TRUE";

    /**
     * There is no need to provide value of field "parameterNames"  if @Auditable annotation added on the method, which is part of your project,
     *         e.g, Controller class, Service Class.
     *         If the Auditable Annotation is added on the method, which is not part of your project(e.g, feign client) then the value of it
     *         should be provided as per below code snippet. If value is not provided then parameter Names will be logged arg0, arg1,....
     *
     *         @Auditable(parameterNames = {"headers","userId", "account", "count", "page"})
     *         @GetMapping(value = "/private/businessUsers/{userId}/serviceNumbers", consumes = "application/json")
     *         public ResponseEntity<ServiceNumbers> getServiceNumbers(@RequestHeader HttpHeaders headers,
     *                         @PathVariable("userId") String userId,
     *                         @RequestParam(value = "account", required = false) String account,
     *                         @RequestParam(value = "count", required = false) Integer count,
     *                         @RequestParam(value = "page", required = false) Integer page);
     */

    String[] parameterNames() default {};


}
