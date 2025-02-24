package cat.institutmarianao.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import cat.institutmarianao.dao.ModuleDao;
import cat.institutmarianao.dao.StudentDao;
import cat.institutmarianao.model.Module;
import cat.institutmarianao.model.Student;
import jakarta.ejb.EJB;

@RunWith(Arquillian.class)
public class StudentDaoImplTest {

	@EJB
	private StudentDao studentDao;

	@EJB
	private ModuleDao moduleDao;

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class, "students-jdbc.jar").addPackages(true, "cat.institutmarianao.dao")
				.addPackage("cat.institutmarianao.model").addClass(org.h2.Driver.class)
				.addAsManifestResource("db.properties").addAsManifestResource("META-INF/MANIFEST.MF", "MANIFEST.MF")
				.addAsManifestResource("META-INF/ejb-jar.xml", "ejb-jar.xml");
	}

	@Test
	public void testFindByPk() throws ClassNotFoundException, SQLException, IOException {
		final String DNI = "27182818B";
		Student student = studentDao.findByPk(DNI);

		assertNotNull(student);
		assertEquals(DNI, student.getDni());
	}

	@Test
	public void testFindAll() throws ClassNotFoundException, SQLException, IOException {
		List<Student> students = studentDao.findAll();

		assertNotNull(students);
		assertEquals(2, students.size());
	}

	@Test
	public void testAddAndRemoveOk() throws ClassNotFoundException, SQLException, IOException {
		// Test add student
		final String DNI = "48164276E";
		Student student = createArielStudent();

		student = studentDao.add(student);

		assertNotNull(student);
		assertEquals(DNI, student.getDni());

		// Test remove student
		studentDao.remove(student);

		student = studentDao.findByPk(DNI);

		assertNull(student);
	}

	@Test
	public void testAddRollback() {
		assertThrows(Exception.class, () -> studentDao.add(null));
	}

	@Test
	public void testRemoveByDniOk() throws ClassNotFoundException, SQLException, IOException {
		final String DNI = "31415926A";
		Student student = studentDao.findByPk(DNI);

		assertNotNull(student);
		assertEquals(DNI, student.getDni());

		studentDao.removeByDni(DNI);

		student = studentDao.findByPk(DNI);

		assertNull(student);
	}

	@Test
	public void testRemoveByDniRollback() {
		assertThrows(Exception.class, () -> studentDao.removeByDni(null));
	}

	private Student createArielStudent() throws ClassNotFoundException, SQLException, IOException {
		List<Module> modules = moduleDao.findAllByCycleCode("DAW");

		Student student = new Student();
		student.setDni("48164276E");
		student.setName("Ariel");
		student.setSurname("Gómez Valiente");
		student.setEmail("thearielcompany@gmail.com");
		student.setCycle("DAW");
		student.setModules(modules);

		return student;
	}
}
