package com.sd.service.todolist.util;

import java.net.URI;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Component
public class ResourceLocator {

    public URI getResourceUri(Class classO, final String method, final Integer id) {
        return MvcUriComponentsBuilder.fromMethodName(
                classO, method, id).buildAndExpand(1).toUri();
    }
}
