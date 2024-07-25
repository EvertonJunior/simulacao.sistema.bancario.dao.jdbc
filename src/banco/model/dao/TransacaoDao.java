package banco.model.dao;

import java.util.List;

import banco.model.entities.Conta;
import banco.model.entities.Transacao;

public interface TransacaoDao {

	
	void registrarNovaTransacao(Transacao transacao);
	List<Transacao> listarTransacoesPorConta(Integer id);
	List<Transacao> listarTodasTransacoes();
}
