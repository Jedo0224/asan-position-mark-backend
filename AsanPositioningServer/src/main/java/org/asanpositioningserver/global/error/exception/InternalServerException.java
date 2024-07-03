package org.asanpositioningserver.global.error.exception;

import org.asanpositioningserver.global.error.ErrorCode;

public class InternalServerException extends BusinessException {
    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}