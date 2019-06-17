package appAdictive.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import appAdictive.util.enums.Role;
import appAdictive.util.enums.TipoEj;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity(name = "Usuario")
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEntity {

	@Id
	@Column
	private String email;

	@Column
	private String nombre;

	@JsonIgnore
	@Column
	private String contrasena;

	@Column
	@Enumerated(EnumType.STRING)
	private TipoEj meta;

	@Column
	private int KCal;

	@Column
	private int ciclo;

	@Column(name = "fecha_final")
	private Date fechaFinal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_rutina_ant")
	private RutinaEntity rutinaAnterior;

	@Column
	private int diasDisponibles;

	@Column
	@Enumerated(EnumType.STRING)
	@JsonIgnore
	private Role role;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "id_rutina")
	private RutinaEntity rutina;

	@Builder.Default
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<RutinaEntity> misRutinas = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.PERSIST)
	private List<PesoEntity> peso = new ArrayList<>();

	@Column
	private boolean activo;
	
	@Column
	private boolean ban;

	public RutinaEntity addMisRutinas(RutinaEntity misRutinas) {
		getMisRutinas().add(misRutinas);
		return misRutinas;
	}
	
	public PesoEntity addPeso(PesoEntity peso) {
		getPeso().add(peso);
		return peso;
	}
}
