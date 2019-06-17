package appAdictive.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

@Entity(name = "DiaEjercicio")
@Table(name = "dia_ejercicio")
@Data 
public class DiaEjercicioEntity {

	@Id
	@Column(name = "id_dia_ejercicio")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idDiaEjercicio;

	@Column
	private String series;

	@Column
	private int descanso;

	@Column
	private int intensidad;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dia")
	private DiaEntity dia;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_ejercicio")
	private EjercicioEntity ejercicio;
}
