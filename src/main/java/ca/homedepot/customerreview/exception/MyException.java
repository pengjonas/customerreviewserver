package ca.homedepot.customerreview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class MyException extends RuntimeException
{
	public MyException(Long reviewId)
	{
		super("Review " + ((reviewId != null) ? reviewId.toString() : "") + " cannot be saved!");
	}
}