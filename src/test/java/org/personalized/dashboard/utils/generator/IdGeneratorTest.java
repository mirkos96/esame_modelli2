package org.personalized.dashboard.utils.generator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

/**
 * Created by sudan on 30/5/15.
 */
@ActiveProfiles("test")
public class IdGeneratorTest {

    private IdGenerator idGenerator;

    @Before
    public void initialize() {
        idGenerator = new IdGenerator();
    }

    @Test
    public void testIdGenerator() {

        String id = idGenerator.generateId("BOK", 16, true);
        Assert.assertEquals("ID length is 16", 16, id.length());
        Assert.assertEquals("ID prefix is BOK", 0, id.indexOf("BOK"));
    }
}
