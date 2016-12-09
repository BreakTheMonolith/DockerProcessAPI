package guru.breakthemonolith.docker;

import org.force66.beantester.BeanTester;
import org.junit.Assert;
import org.junit.Test;

public class GenericBeanTest {

	@Test
	public void testDockerRunSpecification() throws Exception {
		BeanTester beanTester = new BeanTester();
		beanTester.testBean(DockerRunSpecification.class);

		DockerRunSpecification spec = new DockerRunSpecification("testImage");
		Assert.assertEquals("testImage", spec.getImageName());
	}

}
