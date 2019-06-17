package appAdictive.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Validated
@Data 
public class MusculoDTO {
    
	@NotNull
	@Digits(fraction = 0, integer = 8)
	@Min(1)
	private Integer idMusculo;
	
    private String nombre;
    
}
