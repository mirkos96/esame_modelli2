package org.personalized.dashboard.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.bootstrap.ConfigManager;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.PinDao;
import org.personalized.dashboard.dao.impl.PinDaoImpl;
import org.personalized.dashboard.model.Pin;
import org.personalized.dashboard.utils.ConfigKeys;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.generator.IdGenerator;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

/**
 * Created by sudan on 31/5/15.
 */
@ActiveProfiles("test")
public class PinDaoTest {

    private PinDao pinDao;

    @Before
    public void initialize() throws IOException {
        ConfigManager.init();
        this.pinDao = new PinDaoImpl(new IdGenerator());
    }

    @Test
    public void testPinDao() {

        Boolean isDebugMode = Boolean.valueOf(ConfigKeys.MONGO_DEBUG_FLAG);

        /*
            To run these test cases enable isDebugMode in config.properties

         */
        if (isDebugMode) {
            MongoBootstrap.init();
            MongoBootstrap.getMongoDatabase().getCollection(Constants.PINS).drop();

            Pin pin1 = new Pin();
            pin1.setName("pin1");
            pin1.setDescription("desc1");
            pin1.setImageUrl("http://www.google.com");

            Pin pin2 = new Pin();
            pin2.setName("pin2");
            pin2.setDescription("desc2");
            pin2.setImageUrl("http://www.yahoo.com");

            // Create two pins
            String pinid1 = pinDao.create(pin1, "1");
            String pinid2 = pinDao.create(pin2, "1");

            Pin pinRead1 = pinDao.get(pinid1, "1");
            Pin pinRead2 = pinDao.get(pinid2, "1");

            // Verify the created values
            Assert.assertEquals("PinIds match", pinid1, pinRead1.getPinId());
            Assert.assertEquals("name match", "pin1", pinRead1.getName());
            Assert.assertEquals("description match", "desc1", pinRead1.getDescription());
            Assert.assertEquals("image url match", "http://www.google.com", pinRead1.getImageUrl());
            Assert.assertNotNull("Createdon is not null", pinRead1.getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", pinRead1.getModifiedAt());

            Assert.assertEquals("PinIds match", pinid2, pinRead2.getPinId());
            Assert.assertEquals("name match", "pin2", pinRead2.getName());
            Assert.assertEquals("description match", "desc2", pinRead2.getDescription());
            Assert.assertEquals("image url match", "http://www.yahoo.com", pinRead2.getImageUrl());
            Assert.assertNotNull("Createdon is not null", pinRead2.getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", pinRead2.getModifiedAt());

            // Update and verify the updated values

            pinRead2.setName("google_advanced");
            pinRead2.setDescription("desc_advanced");
            pinRead2.setImageUrl("http://www.bing.com");
            pinDao.update(pinRead2, "1");

            pinRead2 = pinDao.get(pinid2, "1");

            Assert.assertEquals("PinIds match", pinid2, pinRead2.getPinId());
            Assert.assertEquals("name match", "google_advanced", pinRead2.getName());
            Assert.assertEquals("description match", "desc_advanced", pinRead2.getDescription());
            Assert.assertEquals("image url match", "http://www.bing.com", pinRead2.getImageUrl());
            Assert.assertNotNull("Createdon is not null", pinRead2.getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", pinRead2.getModifiedAt());

            // verify the count
            long count = pinDao.count("1");
            Assert.assertEquals("Count is 2", 2, count);


            // verify the paginated api
            List<Pin> pins = pinDao.get(10, 0, "1");
            Assert.assertEquals("Count is 2", 2, pins.size());

            Assert.assertEquals("PinIds match", pinid2, pins.get(0).getPinId());
            Assert.assertEquals("name match", "google_advanced", pins.get(0).getName());
            Assert.assertEquals("description match", "desc_advanced", pins.get(0).getDescription());
            Assert.assertEquals("image url match", "http://www.bing.com", pins.get(0).getImageUrl
                    ());
            Assert.assertNotNull("Createdon is not null", pins.get(0).getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", pins.get(0).getModifiedAt());

            Assert.assertEquals("PinIds match", pinid1, pins.get(1).getPinId());
            Assert.assertEquals("name match", "pin1", pins.get(1).getName());
            Assert.assertEquals("description match", "desc1", pins.get(1).getDescription());
            Assert.assertEquals("image url match", "http://www.google.com", pins.get(1)
                    .getImageUrl());
            Assert.assertNotNull("Createdon is not null", pins.get(1).getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", pins.get(1).getModifiedAt());

            // verify delete followed by data
            pinDao.delete(pinid2, "1");
            count = pinDao.count("1");
            Assert.assertNull("Pin2 is deleted", pinDao.get(pinid2, "1"));
            Assert.assertNotNull("Pin1 is not deleted", pinDao.get(pinid1, "1"));
            Assert.assertEquals("Count now is 1", 1, count);

            pins = pinDao.get(10, 0, "1");
            Assert.assertEquals("Count is 1", 1, pins.size());

            Assert.assertEquals("PinIds match", pinid1, pins.get(0).getPinId());
            Assert.assertEquals("name match", "pin1", pins.get(0).getName());
            Assert.assertEquals("description match", "desc1", pins.get(0).getDescription());
            Assert.assertEquals("image url match", "http://www.google.com", pins.get(0)
                    .getImageUrl());
            Assert.assertNotNull("Createdon is not null", pins.get(0).getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", pins.get(0).getModifiedAt());

            MongoBootstrap.getMongoDatabase().getCollection(Constants.PINS).drop();

        }
    }

}
