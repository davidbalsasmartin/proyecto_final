package appAdictive.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import appAdictive.util.enums.Requisito;
import appAdictive.util.enums.TipoEjercicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Ejercicio")
@Table(name = "ejercicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EjercicioEntity {

	@Id
	@Column (name = "id_ejercicio")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idEjercicio;

	@Column
	private String nombre;

	@Enumerated(EnumType.STRING)
	private Requisito requisito;

	@Column
	@Enumerated(EnumType.STRING)
	private TipoEjercicio tipo;
	
	@ManyToMany (fetch = FetchType.LAZY)
	@JoinTable(
	  name = "ejer_musc_princ",
	  joinColumns = @JoinColumn(name = "id_ejercicio"), 
	  inverseJoinColumns = @JoinColumn(name = "id_musculo"))
	private List<MusculoEntity> musculosPrinc;
	
	@ManyToMany (fetch = FetchType.LAZY)
	@JoinTable(
	  name = "ejer_musc_sec",
	  joinColumns = @JoinColumn(name = "id_ejercicio"), 
	  inverseJoinColumns = @JoinColumn(name = "id_musculo"))
	private List<MusculoEntity> musculosSec;

	public MusculoEntity addMusculosPrinc(MusculoEntity musculosPrinc) {
		getMusculosPrinc().add(musculosPrinc);
		return musculosPrinc;
	}
	
	public MusculoEntity addMusculosSec(MusculoEntity musculosSec) {
		getMusculosSec().add(musculosSec);
		return musculosSec;
	}
}
