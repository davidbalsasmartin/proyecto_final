package appAdictive.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import appAdictive.util.enums.InfoTipo;
import appAdictive.util.enums.TipoEj;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

@Entity(name = "Rutina")
@Table(name = "rutina")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RutinaEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idRutina;

	@Column
	private String nombre;
	
	@Column
	private int copias;
	
	@Column(name = "fecha_creacion")
	private Date fechaCreacion;

	@Column(name = "descripcion_dias")
	@Enumerated(EnumType.STRING)
	private InfoTipo descripcionDias;

	@Column
	private String descripcion;
	
	@Column
	private String creador;
	
	@Column
	private int numeroDias;

	@Column
	@Enumerated(EnumType.STRING)
	private TipoEj tipo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private UsuarioEntity usuario;

	@OneToMany(mappedBy = "rutina", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	private List<DiaEntity> dias;

	public DiaEntity addDias(DiaEntity dias) {
		getDias().add(dias);
		return dias;
	}
}
