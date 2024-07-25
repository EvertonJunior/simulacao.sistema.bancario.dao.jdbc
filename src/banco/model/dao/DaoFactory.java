package banco.model.dao;

import banco.model.dao.imp.ClienteDaoJDBC;
import banco.model.dao.imp.ContaDaoJDBC;
import banco.model.dao.imp.TransacaoDaoJDBC;
import db.DB;

public class DaoFactory {

	public static ClienteDao createClienteDao() {
		return new ClienteDaoJDBC(DB.getConnection());
	}
	
	public static ContaDao createContaDao() {
		return new ContaDaoJDBC(DB.getConnection());
	}
	
	public static TransacaoDao createTransacaoDao() {
		return new TransacaoDaoJDBC(DB.getConnection());
	}
	
	
	
}
