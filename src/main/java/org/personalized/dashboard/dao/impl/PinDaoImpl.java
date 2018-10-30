package org.personalized.dashboard.dao.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.PinDao;
import org.personalized.dashboard.model.Pin;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;
import org.personalized.dashboard.utils.generator.IdGenerator;

import java.util.List;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by sudan on 3/4/15.
 */
public class PinDaoImpl implements PinDao {

    private IdGenerator idGenerator;

    @Inject
    public PinDaoImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public String create(Pin pin, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.PINS);

        String pinId = idGenerator.generateId(Constants.PIN_PREFIX, Constants.ID_LENGTH, true);
        Document document = new Document()
                .append(FieldKeys.PRIMARY_KEY, pinId)
                .append(FieldKeys.PIN_NAME, pin.getName())
                .append(FieldKeys.PIN_DESCRIPTION, pin.getDescription())
                .append(FieldKeys.PIN_IMAGE_URL, pin.getImageUrl())
                .append(FieldKeys.USER_ID, userId)
                .append(FieldKeys.CREATED_ON, System.currentTimeMillis())
                .append(FieldKeys.MODIFIED_AT, System.currentTimeMillis());
        collection.insertOne(document);

        return pinId;
    }

    @Override
    public Pin get(String pinId, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.PINS);
        Document document = collection.find(and
                        (
                                eq(FieldKeys.PRIMARY_KEY, pinId),
                                eq(FieldKeys.USER_ID, userId),
                                ne(FieldKeys.IS_DELETED, true)
                        )
        ).first();

        if (document != null) {
            Pin pin = new Pin();
            pin.setPinId(document.getString(FieldKeys.PRIMARY_KEY));
            pin.setName(document.getString(FieldKeys.PIN_NAME));
            pin.setDescription(document.getString(FieldKeys.PIN_DESCRIPTION));
            pin.setImageUrl(document.getString(FieldKeys.PIN_IMAGE_URL));
            pin.setCreatedOn(document.getLong(FieldKeys.CREATED_ON));
            pin.setModifiedAt(document.getLong(FieldKeys.MODIFIED_AT));
            if (document.containsKey(FieldKeys.ENTITY_TAGS)) {
                List<String> tags = (List<String>) document.get(FieldKeys.ENTITY_TAGS);
                pin.setTags(tags);
            }
            return pin;
        }
        return null;


    }

    @Override
    public Long update(Pin pin, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.PINS);
        Document document = new Document()
                .append(FieldKeys.PIN_NAME, pin.getName())
                .append(FieldKeys.PIN_DESCRIPTION, pin.getDescription())
                .append(FieldKeys.PIN_IMAGE_URL, pin.getImageUrl())
                .append(FieldKeys.MODIFIED_AT, System.currentTimeMillis());

        UpdateResult updateResult = collection.updateOne(
                and(
                        eq(FieldKeys.PRIMARY_KEY, pin.getPinId()),
                        eq(FieldKeys.USER_ID, userId),
                        ne(FieldKeys.IS_DELETED, true)
                ),
                new Document(Constants.SET_OPERATION, document)
        );
        return updateResult.getModifiedCount();

    }

    @Override
    public Long delete(String pinId, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.PINS);

        Document document = new Document()
                .append(FieldKeys.IS_DELETED, true);

        UpdateResult updateResult = collection.updateOne(
                and(
                        eq(FieldKeys.PRIMARY_KEY, pinId),
                        eq(FieldKeys.USER_ID, userId)
                ),
                new Document(Constants.SET_OPERATION, document)
        );
        return updateResult.getModifiedCount();

    }

    @Override
    public Long count(String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.PINS);

        return collection.count(
                and(
                        eq(FieldKeys.USER_ID, userId),
                        ne(FieldKeys.IS_DELETED, true)
                )
        );

    }

    @Override
    public List<Pin> get(int limit, int offset, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.PINS);

        FindIterable<Document> iterator = collection.find(and
                        (
                                eq(FieldKeys.USER_ID, userId),
                                ne(FieldKeys.IS_DELETED, true)
                        )
        ).skip(offset).limit(limit).sort(
                new Document(FieldKeys.MODIFIED_AT, -1)
        );

        final List<Pin> pins = Lists.newArrayList();
        iterator.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                Pin pin = new Pin();
                pin.setPinId(document.getString(FieldKeys.PRIMARY_KEY));
                pin.setName(document.getString(FieldKeys.PIN_NAME));
                pin.setDescription(document.getString(FieldKeys.PIN_DESCRIPTION));
                pin.setImageUrl(document.getString(FieldKeys.PIN_IMAGE_URL));
                pin.setCreatedOn(document.getLong(FieldKeys.CREATED_ON));
                pin.setModifiedAt(document.getLong(FieldKeys.MODIFIED_AT));
                if (document.containsKey(FieldKeys.ENTITY_TAGS)) {
                    List<String> tags = (List<String>) document.get(FieldKeys.ENTITY_TAGS);
                    pin.setTags(tags);
                }
                pins.add(pin);
            }
        });
        return pins;

    }
}
