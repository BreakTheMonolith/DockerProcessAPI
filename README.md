# DockerProcessAPI
A Java API that makes it easy to issue local docker and docker-compose commands programatically. This
assumes that the machine in which this runs can issue a 'docker ps' and a 'docker-compose version' 
from a normal command line (not from within a docker-machine).

If your installation requires a docker-machine (e.g. you have a mac or a windows 
machine without Hyper-V / Docker capabilities), then you should use the Spotify
[docker-client](https://github.com/spotify/docker-client) that uses the Docker
Remote API.

## Typical Use Cases
* *Integration testing* - Use DockerProcessAPI to bring up external services and products
your application needs and shut down those products after your tests are over.

* [System requirements and installation](#user-content-system-requirements)

## Usage Notes
A full example can be found [here](https://github.com/BreakTheMonolith/btm-DropwizardHealthChecks/blob/master/btm-DropwizardHealthChecks-rabbitmq/src/test/java/guru/breakthemonolith/health/rabbitmq/RabbitMQHealthCheckTestIntegration.java). Note that this example utilizes @BeforeClass and @AfterClass
methods to place logic to bring up an instance of RabbitMQ for integration test cases and shut them
down after the tests complete.

### Docker Examples
* [Pull a docker image](#user-content-pull-a-docker-image)
* [Log active docker containers](#user-content-run-docker-image)
* [Run docker containers](#user-content-run-docker-image)
* [Kill running docker container](#user-content-kill-docker-container)
* [Obtain log output from Container](#user-content-obtain-log-output-from-a-running-docker-container)

### Docker-Compose Examples
* [Start docker compose environment](#user-content-bring-up-docker-compose-environment)
* [Shutdown docker compose environment](#user-content-shutdown-docker-compose-environment)
* [Log running docker compose environments](#user-content-log-running-docker-compose-environments)
* [Log a specific docker compose configuration](#user-content-log-a-specific-docker-compose-configuration)

### Pull a Docker Image
Issues 'docker pull'.

Example usage:
```  
import guru.breakthemonolith.docker.DockerCommandUtils;

DockerCommandUtils.dockerPull("hello-world");
```  

### List Active Docker Containers
Issues 'docker ps'.

All output will appear in the log under the logger name 'DockerProcessAPI'.

Example usage:
```  
import guru.breakthemonolith.docker.DockerCommandUtils;

DockerCommandUtils.dockerContainerListing();
```  

### Run Docker Image
Issues 'docker run'.

Docker images can be run instream or dispatched. If the docker image is run instream, 
the image will be run in a blocking fashion and the image run will complete before
control is returned to the caller.  If the image is dispatched, control will be
returned after a period of time suitable for the container to have completed
it's start-up sequence and be ready for use.

You specify run attributes by using class DockerRunSpecification. You specify the image name
any port mappings, volume mappings, or environment mappings and whether the run is instread or dispatched.
If the run is dispatched (run asynchronously), then you can specify how long the container
is given to complete its start sequence (default is 3 seconds).

Example usage for dispatched execution:
```  
import guru.breakthemonolith.docker.DockerCommandUtils;
import guru.breakthemonolith.docker.DockerRunSpecification;

DockerRunSpecification runSpec = new DockerRunSpecification("rabbitmq:latest");
runSpec.getPortMap().put(address.getHostAddress() + ":6000", "5672");
runSpec.setDetachedWaitTimeMillis(5000);

rabbitMQContainerName = DockerCommandUtils.dockerRun(runSpec);

... // testing happens here

DockerCommandUtils.dockerKillContainer(rabbitMQContainerName);
```  

### Kill Docker Container
Issues 'docker kill'.

Example usage:
```  
import guru.breakthemonolith.docker.DockerCommandUtils;

DockerCommandUtils.dockerKillContainer(rabbitMQContainerName);
```  

### Obtain log output from a running Docker Container
Issues 'docker logs --details'.

Example usage:
```  
import guru.breakthemonolith.docker.DockerCommandUtils;

DockerCommandUtils.dockerLogContainer(rabbitMQContainerName);
```  

### Bring up docker compose environment
Issues 'docker-compose up -d'.

Example usage:
```  
import guru.breakthemonolith.docker.ComposeCommandUtils;
import guru.breakthemonolith.docker.DockerComposeConfiguration;

ComposeCommandUtils.composeUp(new DockerComposeConfiguration("myEnvironment.yml"));
```  
### Shutdown docker compose environment
Issues 'docker-compose down'.

Example usage:
```  
import guru.breakthemonolith.docker.ComposeCommandUtils;
import guru.breakthemonolith.docker.DockerComposeConfiguration;

ComposeCommandUtils.composeDown(new DockerComposeConfiguration("myEnvironment.yml"));
```  

### Log running docker compose environments
Issues 'docker-compose ps'.

Example usage:
```  
import guru.breakthemonolith.docker.ComposeCommandUtils;
import guru.breakthemonolith.docker.DockerComposeConfiguration;

ComposeCommandUtils.composeListing(new DockerComposeConfiguration("myEnvironment.yml"));
```  

### Log a specific docker compose configuration
Issues 'docker-compose config'.

Example usage:
```  
import guru.breakthemonolith.docker.ComposeCommandUtils;
import guru.breakthemonolith.docker.DockerComposeConfiguration;

ComposeCommandUtils.composeConfig(new DockerComposeConfiguration("myEnvironment.yml"));
```  

## System Requirements
* Java JDK 1.7 or later
* Linux operating system or Windows 10 Pro or Enterprise with Hyper-V functioning with Docker
* Docker and docker-compose should be installed and functioning.

## Installation
Maven users can find dependency information [here](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22guru.breakthemonolith%22%20AND%20a%3A%22DockerProcessAPI%22).

To install, you need to include the following dependent libraries in your classpath:
* org.force66 / ValueObjectBase
* org.apache.commons / commons-lang3
* commons-io / commons-io
* org.slf4j / slf4j-api