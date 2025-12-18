package dev.spiritstudios.spectre.api.core.exception;

public class ImpossibleStateException extends RuntimeException {
	public ImpossibleStateException() {
		super("This should be impossible. If you are seeing this, something has gone horribly wrong. Please submit a bug report to the creator of the mod this crash comes from.");
	}

	public ImpossibleStateException(String message) {
		super(message);
	}
}
