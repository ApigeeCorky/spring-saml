package com.truedash.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchResourceFound extends Exception {
    
    private static final long serialVersionUID = 2056616540792129803L;
    
    public NoSuchResourceFound() {
    }
    
    public NoSuchResourceFound(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }
    
    public NoSuchResourceFound(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
    
    public NoSuchResourceFound(String arg0) {
        super(arg0);
    }
    
    public NoSuchResourceFound(Throwable arg0) {
        super(arg0);
    }
    
}
