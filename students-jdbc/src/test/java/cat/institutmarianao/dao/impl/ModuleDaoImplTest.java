package cat.institutmarianao.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import cat.institutmarianao.model.Module;
import jakarta.ejb.EJB;

@RunWith(Arquillian.class)
public class ModuleDaoImplTest {

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
		Module module = moduleDao.findByPk("M07", "DAW");

		assertNotNull(module);
		assertEquals("M07", module.getCode());
	}

	@Test
	public void testFindAllByCycleCode() throws ClassNotFoundException, SQLException, IOException {
		List<Module> modules = moduleDao.findAllByCycleCode("DAW");

		assertNotNull(modules);
		assertEquals(14, modules.size());
	}
}
