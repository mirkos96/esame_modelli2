package org.personalized.dashboard.utils.validator;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.personalized.dashboard.model.BatchSize;
import org.personalized.dashboard.utils.Constants;

import java.util.List;

/**
 * Created by sudan on 29/5/15.
 */
public class BatchSizeValidationService implements ValidationService<BatchSize> {

    @Override
    public List<ErrorEntity> validate(BatchSize batchSize) {
        List<ErrorEntity> errorEntities = Lists.newArrayList();

        if (batchSize.getLimit() > Constants.MAX_BATCH_SIZE || batchSize.getLimit() < 0) {
            ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.INVALID_LIMIT.name(), ErrorCodes
                    .INVALID_LIMIT.getDescription(), StringUtils.EMPTY);
            errorEntities.add(errorEntity);
        }
        if (batchSize.getOffset() < 0) {
            ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.INVALID_OFFSET.name(),
                    ErrorCodes.INVALID_OFFSET.getDescription(), StringUtils.EMPTY);
            errorEntities.add(errorEntity);
        }
        return errorEntities;
    }
}
