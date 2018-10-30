(function($, window, document) {

    "use strict"

    var TAG_URL_ROOT = '/iPersonal/dashboard/tags';

    var Tag = Backbone.Model.extend({
        urlRoot: TAG_URL_ROOT,
        defaults: {
            tags: [],
            tagSet: {}
        },

        setTags: function(userTags) {

            for (var i = 0; i < userTags.length; i++) {

                if (!this.attributes.tagSet[userTags[i]]) {
                    this.attributes.tags.push(userTags[i])
                    this.attributes.tagSet[userTags[i]] = true;
                }
            }
        },

        getTags: function() {
            return this.attributes.tags;
        },

        addTags: function(entityId, entityType, entityTitle, entityTags) {

            var self = this;
            var entity = {};
            entity['title'] = entityTitle;
            entity['entityType'] = entityType;
            entity['entityId'] = entityId;

            var tag = new Tag({
                'entity': entity,
                'tags': entityTags
            });

            return tag.save().complete(function(response) {
                if (response.status == 201) {
                    for (var i = 0; i < entityTags.length; i++) {
                        if (!self.attributes.tagSet[entityTags[i]]) {
                            self.attributes.tags.push(entityTags[i]);
                            self.attributes.tagSet[entityTags[i]] = true;
                        }
                    }
                }
            });

        }

    });

    window.tagModel = new Tag();

})(jQuery, window, document);