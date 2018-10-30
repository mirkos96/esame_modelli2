package org.personalized.dashboard.utils.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.model.BatchSize;
import org.personalized.dashboard.utils.Constants;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * Created by sudan on 30/5/15.
 */
@ActiveProfiles("test")
public class BatchSizeValidationTest {

    private ValidationService batchSizeValidationService;

    @Before
    public void initialize() {
        this.batchSizeValidationService = new BatchSizeValidationService();
    }

    @Test
    public void testBatchSizeValidation() {
        BatchSize batchSize = new BatchSize();
        batchSize.setLimit(10);
        batchSize.setOffset(0);

        List<ErrorEntity> errorEntities = batchSizeValidationService.validate(batchSize);
        Assert.assertEquals("Error count is 0", 0, errorEntities.size());

        batchSize.setOffset(-1);
        errorEntities = batchSizeValidationService.validate(batchSize);
        Assert.assertEquals("Error count is 1", 1, errorEntities.size());
        Assert.assertEquals("Error is INVALID_OFFSET", ErrorCodes.INVALID_OFFSET.name(),
                errorEntities.get(0).getName());

        batchSize.setOffset(0);
        batchSize.setLimit(Constants.MAX_BATCH_SIZE + 1);
        errorEntities = batchSizeValidationService.validate(batchSize);
        Assert.assertEquals("Error count is 1", 1, errorEntities.size());
        Assert.assertEquals("Error is INVALID_LIMIT", ErrorCodes.INVALID_LIMIT.name(),
                errorEntities.get(0).getName());

        batchSize.setOffset(-1);
        batchSize.setLimit(-1);
        errorEntities = batchSizeValidationService.validate(batchSize);
        Assert.assertEquals("Error count is 2", 2, errorEntities.size());
        Assert.assertEquals("Error 1 is INVALID_LIMIT", ErrorCodes.INVALID_LIMIT.name(),
                errorEntities.get(0).getName());
        Assert.assertEquals("Error 2 is INVALID_OFFSET", ErrorCodes.INVALID_OFFSET.name(),
                errorEntities.get(1).getName());

    }
}
