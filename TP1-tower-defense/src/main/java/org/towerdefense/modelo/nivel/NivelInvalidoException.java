package org.towerdefense.modelo.nivel;

public class NivelInvalidoException extends RuntimeException {
    public NivelInvalidoException(String message) {
        super(message);
    }

    public NivelInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
