package banco.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Conta implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id; //
	private Double saldo;
	
	private Cliente cliente;
	
	
	public Conta() {
		
	}


	public Conta(Integer id, Double saldo, Cliente cliente) {
		this.id = id;
		this.saldo = saldo;
		this.cliente = cliente;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Double getSaldo() {
		return saldo;
	}
	
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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
		Conta other = (Conta) obj;
		return Objects.equals(id, other.id);
	}


	@Override
	public String toString() {
		return "Conta [contaId=" + id + ", saldo=" + saldo + ", cliente=" + cliente + "]";
	}



	
	
	
}
