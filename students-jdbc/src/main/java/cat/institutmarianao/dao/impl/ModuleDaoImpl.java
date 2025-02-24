package cat.institutmarianao.dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import cat.institutmarianao.dao.ModuleDao;
import cat.institutmarianao.model.Module;
import jakarta.ejb.Stateless;

@Stateless
public class ModuleDaoImpl extends BaseDaoImpl<Module, Object> implements ModuleDao {

	@Override
	public Module findByPk(String moduleCode, String cycleCode)
			throws ClassNotFoundException, SQLException, IOException {
		String query = "SELECT m.code AS module_code, m.cycle_code AS cycle_code, m.name AS module_name FROM modules m JOIN cycles c "
				+ "ON m.cycle_code = c.code WHERE m.code = ? AND m.cycle_code = ?";

		PreparedStatement stmt = getPreparedStatement(query);

		stmt.setString(1, moduleCode);
		stmt.setString(2, cycleCode);

		return findUniqueResult(stmt);
	}

	@Override
	public List<Module> findAllByCycleCode(String cycleCode) throws ClassNotFoundException, SQLException, IOException {
		String query = "SELECT code AS module_code, cycle_code AS cycle_code, name AS module_name FROM modules WHERE cycle_code = ?";

		PreparedStatement stmt = getPreparedStatement(query);

		stmt.setString(1, cycleCode);

		return executeQuery(stmt);
	}

	@Override
	protected Module buildObjectFromResultSet(ResultSet rs) throws SQLException {
		Module module = new Module();

		module.setCode(rs.getString("module_code"));
		module.setCycleCode(rs.getString("cycle_code"));
		module.setName(rs.getString("module_name"));

		return module;
	}

}
