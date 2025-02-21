package cat.institutmarianao.dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import cat.institutmarianao.dao.CycleDao;
import cat.institutmarianao.model.Cycle;
import jakarta.ejb.Stateless;

@Stateless
public class CycleDaoImpl extends BaseDaoImpl<Cycle, Object> implements CycleDao {

	@Override
	public List<Cycle> findAll() throws ClassNotFoundException, SQLException, IOException {
		String query = "SELECT code, name FROM cycles";

		PreparedStatement stmt = getPreparedStatement(query);

		return executeQuery(stmt);
	}

	@Override
	protected Cycle buildObjectFromResultSet(ResultSet rs) throws SQLException {
		Cycle cycle = new Cycle();

		cycle.setCode(rs.getString("code"));
		cycle.setName(rs.getString("name"));

		return cycle;
	}

}
