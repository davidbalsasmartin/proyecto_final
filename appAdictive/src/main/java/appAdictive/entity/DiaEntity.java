package appAdictive.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

@Entity(name = "Dia")
@Table(name = "dia")
@Data
public class DiaEntity {

	@Id
	@Column(name = "id_dia")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idDia;
	
	@OneToMany(mappedBy = "dia", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
	private List<DiaEjercicioEntity> diaEjercicios;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_rutina")
	private RutinaEntity rutina;

	public DiaEjercicioEntity addDiaEjercicios(DiaEjercicioEntity diaEjercicios) {
		getDiaEjercicios().add(diaEjercicios);
		return diaEjercicios;
	}

}
