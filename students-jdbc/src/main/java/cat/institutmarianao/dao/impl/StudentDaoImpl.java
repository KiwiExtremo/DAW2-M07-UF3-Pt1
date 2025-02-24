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
		String query = "SELECT dni AS DNI, s.name AS student_name, surname AS student_surname, email, module_cycle_code AS cycle_code, m.code AS module_code, m.name AS module_name "
				+ "FROM students s JOIN modules_students ms " + "ON dni = student_dni JOIN modules m "
				+ "ON module_code = m.code AND module_cycle_code = cycle_code " + "WHERE dni = ?";

		PreparedStatement stmt = getPreparedStatement(query);

		stmt.setString(1, dni);

		return findUniqueResult(stmt);
	}

	@Override
	public List<Student> findAll() throws ClassNotFoundException, SQLException, IOException {
		String query = "SELECT dni AS DNI, s.name AS student_name, surname AS student_surname, email, module_cycle_code AS cycle_code, m.code AS module_code, m.name AS module_name "
				+ "FROM students s JOIN modules_students ms " + "ON dni = student_dni JOIN modules m "
				+ "ON module_code = m.code AND module_cycle_code = cycle_code";

		PreparedStatement stmt = getPreparedStatement(query);

		return executeQuery(stmt);
	}

	@Override
	public Student add(Student student) throws ClassNotFoundException, SQLException, IOException {
		String studentQuery = "INSERT INTO students (dni, name, surname, email) " + "VALUES (?, ?, ?, ?)";
		String moduleStudentQuery = "INSERT INTO modules_students (module_code, module_cycle_code, student_dni "
				+ "VALUES (?, ?, ?)";

		try {
			if (connection == null) {
				connection = dBConnection.getConnection();
			}

			connection.setAutoCommit(false);

			PreparedStatement studentStmt = getPreparedStatement(studentQuery);
			studentStmt.setString(1, student.getDni());
			studentStmt.setString(2, student.getName());
			studentStmt.setString(3, student.getSurname());
			studentStmt.setString(4, student.getEmail());

			executeUpdateQuery(studentStmt);

			PreparedStatement moduleStmt = getPreparedStatement(moduleStudentQuery);

			for (Module module : student.getModules()) {
				moduleStmt.setString(1, module.getCode());
				moduleStmt.setString(2, module.getCycleCode());
				moduleStmt.setString(3, student.getDni());

				moduleStmt.addBatch();
			}

			moduleStmt.executeBatch();

			connection.commit();

		} catch (SQLException e) {
			connection.rollback();

		} finally {
			connection.setAutoCommit(true);
		}

		return findByPk(student.getDni());
	}

	@Override
	public void remove(Student student) throws ClassNotFoundException, SQLException, IOException {
		removeByDni(student.getDni());
	}

	@Override
	public void removeByDni(String dni) throws ClassNotFoundException, SQLException, IOException {
		String studentQuery = "DELETE FROM students WHERE dni = ?";
		String moduleStudentQuery = "DELETE FROM modules_students WHERE student_dni = ?";

		try {
			if (connection == null) {
				connection = dBConnection.getConnection();
			}

			connection.setAutoCommit(false);

			PreparedStatement studentStmt = getPreparedStatement(studentQuery);
			studentStmt.setString(1, dni);

			executeUpdateQuery(studentStmt);

			PreparedStatement moduleStmt = getPreparedStatement(moduleStudentQuery);
			moduleStmt.setString(1, dni);

			executeUpdateQuery(moduleStmt);

		} catch (SQLException e) {
			connection.rollback();

		} finally {
			connection.setAutoCommit(true);
		}
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
		student.setName(rs.getString("student_name"));
		student.setSurname(rs.getString("student_surname"));
		student.setEmail(rs.getString("email"));

		Module module = new Module();
		module.setCode(rs.getString("module_code"));
		module.setCycleCode(rs.getString("cycle_code"));
		module.setName(rs.getString("module_name"));

		List<Module> modules = new ArrayList<>();

		modules.add(module);
		student.setModules(modules);

		return student;
	}

}
