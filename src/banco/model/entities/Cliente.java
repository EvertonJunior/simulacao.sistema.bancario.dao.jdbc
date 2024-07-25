package banco.model.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class Cliente implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	private String email;
	private Date dataDeNascimento;
	
	public Cliente() {
		
	}

	public Cliente(Integer id, String nome, String email, Date dataDeNascimento) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.dataDeNascimento = dataDeNascimento;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public Date getDataDeNascimento() {
		return dataDeNascimento;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setDataDeNascimento(Date dataDeNascimento) {
		this.dataDeNascimento = dataDeNascimento;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "clienteId: " + id + ", nome: " + nome + ", email: " + email + ", Data de nascimento: " + dataDeNascimento;
	}
	
	
	
	
	
	
	
}
