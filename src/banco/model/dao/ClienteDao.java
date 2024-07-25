package banco.model.dao;

import java.util.List;

import banco.model.entities.Cliente;

public interface ClienteDao {

	void inserirNovoCliente(Cliente cliente);
	void atualizarDados(Cliente cliente);
	void deletarPorID(Integer id);
	Cliente buscarClientePorEmail(String email);
	List<Cliente> listarTodosOsClientes();
	void sacar(Integer idConta, Double valor); // tem que tirar valor da conta escolhida, criar um novo obj transacao e salvar no db
	void depositar(Integer idConta, Double valor);// tem que colocar valor da conta escolhida, criar um novo obj transacao e salvar no db
	void transferir(Integer idConta,Integer idContaDestino, Double valor); // tirar de uma conta, colocar na outra, evitar que caso tenha um erro durante a transacao cancelar a primeira operacao, criar um novo obj transacao, registrar no db.
	
	
}
