package org.personalized.dashboard.utils.generator;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by sudan on 27/5/15.
 */
public class IdGenerator {

    public String generateId(String prefix, int totLen, boolean isUpperCase) {
        String id = prefix;
        String timestampStr = Long.toString(System.currentTimeMillis() / 60000, Character
                .MAX_RADIX).toUpperCase();
        int randomPartLength = totLen - id.length() - timestampStr.length();
        String randomStr;
        if (isUpperCase) {
            randomStr = RandomStringUtils.randomAlphanumeric(randomPartLength).toUpperCase();
        } else {
            randomStr = RandomStringUtils.randomAlphanumeric(randomPartLength);
        }
        id = id + timestampStr + randomStr;
        return id;
    }

}
