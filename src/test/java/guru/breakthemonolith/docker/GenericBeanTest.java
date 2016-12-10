package guru.breakthemonolith.docker;

import org.force66.beantester.BeanTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GenericBeanTest {

	BeanTester beanTester;

	@Before
	public void setUp() {
		beanTester = new BeanTester();
	}

	@Test
	public void testDockerRunSpecification() throws Exception {
		beanTester.testBean(DockerRunSpecification.class);

		DockerRunSpecification spec = new DockerRunSpecification("testImage");
		Assert.assertEquals("testImage", spec.getImageName());
	}

	public void testDockerComposeSpecification() throws Exception {
		beanTester.testBean(DockerComposeConfiguration.class);

		DockerComposeConfiguration config = new DockerComposeConfiguration("testYaml");
		Assert.assertEquals("testYaml", config.getConfigYamlFileName());
	}

}
