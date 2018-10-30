(function($, window, document) {

    "use strict"

    var PIN_URL_ROOT = '/iPersonal/dashboard/pins';

    var Pin = Base.extend({
        urlRoot: PIN_URL_ROOT,

        initialize: function() {
            this.mandatory = {
                    'name': true,
                    'imageUrl': true,
                    'description': true
                },
                this.maxLength = {
                    'name': 50,
                    'imageUrl': 150,
                    'description': 1000
                },
                this.formAttributes = ['name', 'imageUrl', 'description']
        }

    });

    var Pins = Backbone.Collection.extend({
        model: Pin
    })

    var PinView = BaseView.extend({

        el: $('#pin-wrapper'),
        entityType: 'PIN',
        upsertTemplate: $('#pin-upsert-template').html(),
        displayTemplate: $('#pin-display-template').html(),

        events: {
            'click #pin-submit': 'upsertPin',
            'click #pin-cancel': 'resetValues',
            'click #pin-tag-img': 'displayTagSelection',
        },

        getModel: function(id) {

            return new Pin({
                name: '',
                description: '',
                imageUrl: '',
                tags: []
            });
        },


        buildModel: function(entity) {
            return new Pin(entity);
        },

        prepareVariables: function() {
            this.tagImage = $('#pin-tag-img');
            this.searchTag = $('#pin-tag');
            this.saveForm = $('#pin-form');

        },

        initializeUpdateForm: function() {
            this.prepareVariables();
            Init.initPin();
        },

        upsertPin: function(e) {

            var self = this;
            e.preventDefault();

            var entityId = self.saveForm.find('.entityId').html();

            if (entityId) {
                self.model = new Pin({
                    id: entityId,
                    pinId: entityId
                });
            } else {
                self.model = new Pin();
            }

            self.model.set({
                name: self.saveForm.find('[name=name]').val(),
                imageUrl: self.saveForm.find('[name=imageUrl]').val(),
                description: self.saveForm.find('[name=description]').val()
            });

            self.model.on('invalid', function(model, error) {
                self.renderErrors(error);
            });

            var result = self.model.save({
                success: function(response) {},
                error: function(error) {}
            });

            if (result) {
                result.complete(function(response) {
                    if (response.status != 201 && response.status != 200) {
                        var errors = self.buildErrorObject(response, self);
                        self.renderErrors(errors);
                    } else {
                        var tags = self.searchTag.val();

                        if (!entityId) {
                            self.postCreation(response.responseText, "PIN", self.model.get('name'), 1, tags);
                            self.model.set({
                                id: response.responseText,
                                'createdOn': Math.floor(Date.now()),
                                'modifiedAt': Math.floor(Date.now()),
                                'tags': tags
                            });
                        } else {
                            self.postCreation(entityId, "PIN", self.model.get('name'), 0, tags);
                            self.model.set({
                                id: entityId,
                                'modifiedAt': Math.floor(Date.now()),
                                'tags': tags
                            });
                            self.collection.remove(self.collection.at(self.findIndex(entityId)));
                        }
                        self.collection.unshift(self.model);
                        var entityList = self.buildEntityList();
                        backboneGlobalObj.trigger('entity:displaylist', entityList);
                    }
                });
            }
        },


        fetchPins: function(e) {

            var self = this;

            if (e) {
                e.preventDefault();
            }

            if (this.collection.length == parseInt(entityCountModel.attributes.pins)) {
                var entityList = this.buildEntityList();
                backboneGlobalObj.trigger('entity:displaylist', entityList);
                return;
            }

            this.model = new Pin();
            this.model.fetch({
                data: {
                    offset: this.collection.length,
                    limit: 20
                }
            }).complete(function(response) {
                if (response.status == 200) {
                    var pins = JSON.parse(response.responseText)['pin'];
                    if (pins instanceof Array) {
                        for (var index in pins) {
                            var pin = new Pin(pins[index])
                            pin.set({
                                id: pins[index]['pinId']
                            });
                            if (pin.attributes.tags && !(pin.attributes.tags instanceof Array)) {
                                var tags = [];
                                tags.push(pin.attributes.tags);
                                pin.set({
                                    'tags': tags
                                });
                            }

                            self.collection.push(pin);
                        }
                    } else if (pins) {
                        var pin = new Pin(pins);
                        pin.set({
                            id: pins['pinId']
                        });
                        if (pin.attributes.tags && !(pin.attributes.tags instanceof Array)) {
                            var tags = [];
                            tags.push(pin.attributes.tags);
                            pin.set({
                                'tags': tags
                            });
                        }
                        self.collection.push(pin);
                    }
                    var entityList = self.buildEntityList();
                    backboneGlobalObj.trigger('entity:displaylist', entityList);
                }
            });
        },

        buildEntityList: function() {

            var entityList = [];

            for (var i = 0; i < this.collection.length; i++) {
                var description = this.collection.models[i].attributes.description;
                var entity = {
                    'entityId': this.collection.models[i].attributes.id,
                    'entityTitle': this.collection.models[i].attributes.name,
                    'url': this.collection.models[i].attributes.imageUrl,
                    'entitySummary': description ? description.substring(0, 100) : description,
                    'entityType': 'pin',
                    'modifiedAt': this.collection.models[i].attributes.modifiedAt,
                };
                entityList.push(entity);
            }
            return entityList;
        },

        getDeletableModel: function(id) {

            return new Pin({
                id: id
            });
        }
    });

    window.pinView = new PinView({
        collection: new Pins()
    });

})(jQuery, window, document);