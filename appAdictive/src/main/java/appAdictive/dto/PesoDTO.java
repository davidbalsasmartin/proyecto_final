package appAdictive.dto;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PesoDTO {

	@NotNull
	@Digits(integer=3,fraction=2)
	@Min(40)
	@Max(140)
	private Float peso;

	private Date fecha;

}
