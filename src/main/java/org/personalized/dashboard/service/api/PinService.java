package org.personalized.dashboard.service.api;

import org.personalized.dashboard.model.Pin;

import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
public interface PinService {


    /**
     * Create a pin for the user
     *
     * @param pin
     * @return
     */
    String createPin(Pin pin);

    /**
     * Get the pin for the id
     *
     * @param pinId
     * @return
     */
    Pin getPin(String pinId);

    /**
     * Update the pin
     *
     * @param pin
     * @return
     */
    Long updatePin(Pin pin);

    /**
     * Delete the  pin for the pinId
     *
     * @param pinId
     */
    void deletePin(String pinId);

    /**
     * Count the pin for the user
     *
     * @return
     */
    Long countPins();

    /**
     * Fetch the pins with given limit and offset
     *
     * @param limit
     * @param offset
     * @return
     */
    List<Pin> fetchPins(int limit, int offset);

}
