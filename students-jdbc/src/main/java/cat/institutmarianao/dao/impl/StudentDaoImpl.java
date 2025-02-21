package cat.institutmarianao.dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cat.institutmarianao.model.Module;
import cat.institutmarianao.model.Student;
import jakarta.ejb.Stateless;

@Stateless
public class StudentDaoImpl extends BaseDaoImpl<Student, Object> implements cat.institutmarianao.dao.StudentDao {

	@Override
	public Student findByPk(String dni) throws ClassNotFoundException, SQLException, IOException {
		String query = "SELECT dni AS 'DNI', s.name AS 'student name', surname AS 'student surname', email, module_cycle_code AS 'cycle code', m.code AS 'module code', m.name AS 'module name' "
				+ "FROM students s JOIN modules_students ms " + "ON dni = student_dni JOIN modules m "
				+ "ON module_code = m.code AND module_cycle_code = cycle_code " + "WHERE dni = ?";

		PreparedStatement stmt = getPreparedStatement(query);

		stmt.setString(1, dni);

		return findUniqueResult(stmt);
	}

	@Override
	public List<Student> findAll() throws ClassNotFoundException, SQLException, IOException {
		String query = "SELECT dni AS 'DNI', s.name AS 'student name', surname AS 'student surname', email, module_cycle_code AS 'cycle code', m.code AS 'module code', m.name AS 'module name' "
				+ "FROM students s JOIN modules_students ms " + "ON dni = student_dni JOIN modules m "
				+ "ON module_code = m.code AND module_cycle_code = cycle_code";

		PreparedStatement stmt = getPreparedStatement(query);

		return executeQuery(stmt);
	}

	@Override
	public Student add(Student student) throws ClassNotFoundException, SQLException, IOException {
		// TODO
		return null;
	}

	@Override
	public void remove(Student student) throws ClassNotFoundException, SQLException, IOException {
		// TODO

	}

	@Override
	public void removeByDni(String dni) throws ClassNotFoundException, SQLException, IOException {
		// TODO

	}

	@Override
	protected List<Student> executeQuery(PreparedStatement preparedStatement) throws SQLException {
		Map<String, Student> students = new HashMap<>();
		List<Student> results = new ArrayList<>();

		ResultSet rs = preparedStatement.executeQuery();

		while (rs.next()) {
			Student student = buildObjectFromResultSet(rs);

			if (!students.containsKey(student.getDni())) {
				students.put(student.getDni(), student);

			} else {
				students.get(student.getDni()).getModules().addAll(student.getModules());
			}

			results.add(student);
		}
		rs.close();
		return results;
	}

	@Override
	protected Student buildObjectFromResultSet(ResultSet rs) throws SQLException {
		Student student = new Student();

		student.setDni(rs.getString("DNI"));
		student.setName(rs.getString("student name"));
		student.setSurname(rs.getString("student surname"));
		student.setEmail(rs.getString("email"));

		Module module = new Module();
		module.setCode(rs.getString("module code"));
		module.setCycleCode(rs.getString("cycle code"));
		module.setName(rs.getString("module name"));

		List<Module> modules = new ArrayList<>();

		modules.add(module);
		student.setModules(modules);

		return student;
	}

}
