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
		Student student = studentDao.findByPk("48164276E");

		assertNotNull(student);
		assertEquals("48164276E", student.getDni());
	}

	@Test
	public void testAddOk() throws ClassNotFoundException, SQLException, IOException {
		Student student = createArielStudent();

		student = studentDao.add(student);

		assertNotNull(student);
		assertEquals("48164276E", student.getDni());
	}

	@Test
	public void testAddRollback() {
		assertThrows(Exception.class, () -> studentDao.add(null));
	}

	@Test
	public void testRemove() throws ClassNotFoundException, SQLException, IOException {
		Student student = studentDao.findByPk("27182818B");

		studentDao.remove(student);

		assertNull(studentDao.findByPk("27182818B"));
	}

	@Test
	public void testRemoveByDniOk() throws ClassNotFoundException, SQLException, IOException {
		studentDao.removeByDni("31415926A");

		assertNull(studentDao.findByPk("31415926A"));
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
