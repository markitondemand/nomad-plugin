package org.jenkinsci.plugins.nomad;

import hudson.model.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import static org.junit.Assert.assertTrue;

/**
 * @author Yegor Andreenko
 */
public class NomadApiTest {

    private NomadApi nomadApi = new NomadApi("http://localhost", "TOKEN");
    private NomadSlaveTemplate slaveTemplate = new NomadSlaveTemplate(
            "300", "256", "100",
            null, "remoteFs", "3","1", Node.Mode.NORMAL,
            "ams", "0", "image", "dc01", "", "", false, "bridge",""
    );

    private NomadCloud nomadCloud = new NomadCloud(
            "nomad",
            "nomadUrl",
            "jenkinsUrl",
            "jenkinsAcl",
            "slaveUrl",
            Collections.singletonList(slaveTemplate));

    @Before
    public void setup() {
        slaveTemplate.setCloud(nomadCloud);
    }

    @Test
    public void testStartSlave() {
        String job = nomadApi.buildSlaveJob("slave-1","secret", slaveTemplate);

        assertTrue(job.contains("\"Region\":\"ams\""));
        assertTrue(job.contains("\"CPU\":300"));
        assertTrue(job.contains("\"MemoryMB\":256"));
        assertTrue(job.contains("\"SizeMB\":100"));
        assertTrue(job.contains("\"GetterSource\":\"slaveUrl\""));
        assertTrue(job.contains("\"privileged\":false"));
        assertTrue(job.contains("\"network_mode\":\"bridge\""));
    }

}
