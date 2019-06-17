package appAdictive.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

@Entity(name = "Peso")
@Table(name = "peso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PesoEntity {

	@Id
	@Column (name = "id_peso")
	@GeneratedValue
	private Integer idPeso;

	@Column
	private Float peso;

	@Column
	private Date fecha;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_usuario")
	private UsuarioEntity usuario;

}
