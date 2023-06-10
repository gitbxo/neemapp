package org.neem.neemapp.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NeemAppException {

	public static class InvalidValueException extends RuntimeException {
		private static final long serialVersionUID = 101L;

		InvalidValueException(String msg) {
			super(msg);
		}
	}

	@ControllerAdvice
	public static class InvalidValueAdvice {
		@ResponseBody
		@ExceptionHandler(InvalidValueException.class)
		@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
		String notFoundHandler(InvalidValueException ex) {
			return ex.getMessage();
		}
	}

	public static class PatientNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 102L;

		PatientNotFoundException(String id) {
			super("Could not find patient " + id);
		}
	}

	@ControllerAdvice
	public static class PatientNotFoundAdvice {
		@ResponseBody
		@ExceptionHandler(PatientNotFoundException.class)
		@ResponseStatus(HttpStatus.NOT_FOUND)
		String notFoundHandler(PatientNotFoundException ex) {
			return ex.getMessage();
		}
	}

	public static class PlanNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 103L;

		PlanNotFoundException(String id) {
			super("Could not find plan " + id);
		}
	}

	@ControllerAdvice
	public static class PlanNotFoundAdvice {
		@ResponseBody
		@ExceptionHandler(PlanNotFoundException.class)
		@ResponseStatus(HttpStatus.NOT_FOUND)
		String notFoundHandler(PlanNotFoundException ex) {
			return ex.getMessage();
		}
	}

	public static class SubscriptionNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 104L;

		SubscriptionNotFoundException(String patient_id, String plan_id) {
			super("Could not find subscription for ( " + patient_id + " , " + plan_id + " )");
		}
	}

	@ControllerAdvice
	public static class SubscriptionNotFoundAdvice {
		@ResponseBody
		@ExceptionHandler(SubscriptionNotFoundException.class)
		@ResponseStatus(HttpStatus.NOT_FOUND)
		String notFoundHandler(SubscriptionNotFoundException ex) {
			return ex.getMessage();
		}
	}

	public static class UserNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 201L;

		UserNotFoundException(Long id) {
			super("Could not find user " + id);
		}
	}

	@ControllerAdvice
	public class UserNotFoundAdvice {
		@ResponseBody
		@ExceptionHandler(UserNotFoundException.class)
		@ResponseStatus(HttpStatus.NOT_FOUND)
		String userNotFoundHandler(UserNotFoundException ex) {
			return ex.getMessage();
		}
	}

}
