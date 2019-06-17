package appAdictive.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Token")
@Table(name = "token")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

	@Id
	@Column (name = "id_token")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idToken;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idUsuario")
	private UsuarioEntity usuario;

	@Column
	private Date fecha_expiracion;
	
	@Column
	private String token;

}
