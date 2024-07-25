package banco.model.entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

import banco.model.entities.enums.TipoDeTransacao;

public class Transacao implements Serializable{


	private static long serialVersionUID = 1L;
	
	private Integer id;
	private TipoDeTransacao tipo;
	private Timestamp data;
	private Conta conta;
	private double valor;
	
	public Transacao() {
		
	}

	public Transacao(Integer id, TipoDeTransacao tipo, Timestamp data, Conta conta,
			double valor) {
		this.id = id;
		this.tipo = tipo;
		this.data = data;
		this.conta = conta;
		this.valor = valor;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TipoDeTransacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoDeTransacao tipo) {
		this.tipo = tipo;
	}

	public Timestamp getData() {
		return data;
	}

	public void setData(Timestamp data) {
		this.data = data;
	}

	public Conta getContaOrigem() {
		return conta;
	}

	public void setContaOrigem(Conta conta) {
		this.conta = conta;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
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
		Transacao other = (Transacao) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Transacao [id=" + id + ", tipo=" + tipo + ", valor=" + valor + ", data=" + data + ", conta=" + conta;
	}




	
	
	
}
