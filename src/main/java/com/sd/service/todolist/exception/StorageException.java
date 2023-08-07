package com.sd.service.todolist.exception;

import org.slf4j.helpers.MessageFormatter;

public class StorageException extends RuntimeException {

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(String message) {
        super(message);
    }

    public static final class DataIntegrityException extends StorageException {
        public DataIntegrityException(Throwable cause, String message, Object... argumentArray) {
            super(MessageFormatter.arrayFormat(message, argumentArray).getMessage(), cause);
        }
    }

    public static final class UndeterminedException extends StorageException {
        public UndeterminedException(Throwable cause, String message) {
            super(message, cause);
        }
    }

    public static final class DataNotFoundException extends StorageException {
        public DataNotFoundException(String message, Object... argumentArray) {
            super(MessageFormatter.arrayFormat(message, argumentArray).getMessage());
        }
    }
}
