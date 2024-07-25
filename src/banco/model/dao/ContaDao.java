package banco.model.dao;

import java.util.List;

import banco.model.entities.Cliente;
import banco.model.entities.Conta;

public interface ContaDao {

	void abrirConta(Conta obj);
	void fecharConta(Integer id);
	Conta buscarPorId(Integer id);
	List<Conta> ListarContas();
	List<Conta>ListarContasPorCliente(Cliente cliente);
	
	
}
