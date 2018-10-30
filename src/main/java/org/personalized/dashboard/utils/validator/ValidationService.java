package org.personalized.dashboard.utils.validator;

import java.util.List;

/**
 * Created by sudan on 26/5/15.
 */
public interface ValidationService<T> {

    /**
     * Validates for a valid object
     *
     * @param t
     * @return
     */
    List<ErrorEntity> validate(T t);
}
