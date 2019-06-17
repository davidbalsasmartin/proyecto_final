package appAdictive.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity(name = "Musculo")
@Table(name = "musculo")
@Data
public class MusculoEntity implements Serializable {

	private static final long serialVersionUID = 2253728599610516490L;

	@Id
	@Column(name = "id_musculo")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idMusculo;
	
	@Column
	private String nombre;

}
