(function($, window, document) {

    "use strict"

    window.Base = Backbone.Model.extend({

        defaults: {
            mandatory: {},
            maxLength: {},
            type: {},
            nonModelAttrs: {}
        },

        validate: function(attributes) {

            var errors = {};

            for (var key in this.mandatory) {
                if (!attributes[key]) {
                    errors[key] = key + ' cannot be empty';
                } else if (key == 'date' && isNaN(attributes[key])) {
                    errors[key] = key + ' is Invalid';
                }
            }

            for (var key in this.maxLength) {
                if (attributes[key] && attributes[key].length > this.maxLength[key]) {
                    errors[key] = key + ' cannot exceed ' + this.maxLength[key] + ' characters';
                }
            }

            for (var key in this.type) {
                if (this.type[key] == 'int' || this.type[key] == 'double') {
                    if (attributes[key] && isNaN(parseInt(attributes[key]))) {
                        errors[key] = 'Invalid value for ' + key;
                    }
                }
            }

            for (var key in this.nonModelAttrs) {
                delete attributes[key];
            }

            return $.isEmptyObject(errors) ? false : errors;
        }
    });

    window.BaseView = Backbone.View.extend({

        resetValues: function(e) {

            if (e) {
                e.preventDefault();
            }
            this.$el.empty();

        },

        renderErrors: function(error) {

            for (var i = 0; i < this.model.formAttributes.length; i++) {
                var key = this.model.formAttributes[i];
                if (error[key]) {
                    this.saveForm.find('[name=' + key + ']').addClass('error-field');
                    this.saveForm.find('.' + key + '-error').html(error[key]);
                } else {
                    this.saveForm.find('[name=' + key + ']').removeClass('error-field').val();
                    this.saveForm.find('.' + key + '-error').html('');
                }
            }
        },

        buildErrorObject: function(response) {

            var errorEntities = JSON.parse(response.responseText)['errorEntity'];
            var errors = {};

            for (var i = 0; i < this.model.formAttributes.length; i++) {
                var key = this.model.formAttributes[i];
                errors[key] = [];
            }

            if (errorEntities instanceof Array) {
                for (var i = 0; i < errorEntities.length; i++) {
                    var errorEntity = errorEntities[i];
                    errors[errorEntity.field].push(errorEntity.description);
                }
            } else {
                var errorEntity = errorEntities;
                errors[errorEntity.field].push(errorEntity.description);
            }

            for (var field in errors) {
                errors[field] = errors[field].join(';');
            }
            return errors;
        },

        displayTagSelection: function(e) {

            this.tagImage.addClass('invisible');
            this.searchTag.parent('div.form-group').removeClass('invisible');

            var tags = tagModel.getTags();
            if (tags) {
                for (var i = 0; i < tags.length; i++) {
                    this.searchTag.append($('<option></option>').attr('value', tags[i]).text(tags[i]));
                }
                this.searchTag.trigger('chosen:updated');
            }
        },

        postCreation: function(entityId, entityType, entityTitle, relativeValue, tags, expenseCategories) {

            if (tags) {
                backboneGlobalObj.trigger('tag:add', {
                    'entityId': entityId,
                    'entityType': entityType,
                    'entityTitle': entityTitle,
                    'tags': tags
                });
            }
            backboneGlobalObj.trigger('entity:count', {
                'entityType': entityType,
                'relativeValue': relativeValue
            });

            if (expenseCategories) {
                backboneGlobalObj.trigger('expense_category:set', {
                    'expenseCategories': expenseCategories
                });
            }
            this.resetValues();
        },

        renderCreateTemplate: function() {
            var template = _.template(this.upsertTemplate);
            this.$el.html(template(this.getModel().toJSON()));
            this.$el.fadeIn().removeClass('invisible')
                .siblings().fadeOut().addClass('invisible');
        },

        displayEntity: function(entityId, key, entityType) {

            var entity;
            for (var i = 0; i < this.collection.models.length; i++) {
                if (this.collection.models[i].attributes[key] === entityId) {
                    entity = this.collection.models[i];
                    entity.set({
                        'entityType': entityType
                    })
                    break;
                }
            }

            if (!entity) {

                var self = this;
                var model = this.getModel();
                model.set({
                    id: entityId
                });
                var result = model.fetch({
                    success: function(response) {},
                    error: function(error) {}
                });

                result.complete(function(response) {

                    if (response.status == 200) {
                        var entity = JSON.parse(response.responseText);
                        var entity = self.buildModel(entity);
                        entity.set({
                            entityType: entityType
                        });
                        entity.set({
                            id: entityId
                        });

                        if (entity.attributes.tags && !(entity.attributes.tags instanceof Array)) {
                            var tags = [];
                            tags.push(entity.attributes.tags);
                            entity.set({
                                'tags': tags
                            });
                        }
                        self._displayEntity(entity, entityType);
                    }
                });


            } else {
                this._displayEntity(entity, entityType);
            }
        },

        _displayEntity: function(entity, entityType) {

            if (!entity.get('tags')) {
                entity.set({
                    tags: []
                })
            }

            if (entityType == 'expense' && !entity.get('categories')) {
                entity.set({
                    categories: []
                })
            }

            if (entityType == 'todo') {
                if (entity.attributes.tasks.models instanceof Array) {
                    var tasks = [];
                    for (var i = 0; i < entity.attributes.tasks.models.length; i++) {
                        tasks.push(entity.attributes.tasks.models[i].attributes);
                    }
                    entity.set({
                        'tasks': tasks
                    });
                }
            }

            var template = _.template(this.displayTemplate);
            this.$el.html(template(entity.toJSON()));
            this.$el.fadeIn().removeClass('invisible')
                .siblings().fadeOut().addClass('invisible');

            var self = this;
            $('img.delete').confirmation({
                placement: 'bottom',
                onConfirm: function() {
                    self.deleteEntity();
                }
            });

            var self = this;
            $('img.edit').on('click', function() {
                self.editEntity(entity);

                if (entityType == 'todo') {
                    self.buildTaskObj(entity);
                    self.renderTasks();
                }
            });
        },

        findIndex: function(id) {
            for (var i = 0; i < this.collection.models.length; i++) {
                if (this.collection.models[i].attributes.id == id) {
                    break;
                }
            }
            return i;
        },

        findModel: function(id) {

            var index = this.findIndex(id);
            return this.collection.at(index);
        },

        editEntity: function(entity) {

            var entityId = this.$el.find('img.edit').data('id');
            var template = _.template(this.upsertTemplate);
            this.$el.html(template(entity.toJSON()));
            this.$el.fadeIn().removeClass('invisible')
                .siblings().fadeOut().addClass('invisible');
            this.initializeUpdateForm();
            this.displayTagSelection();
        },

        deleteEntity: function() {

            var self = this;
            var entityId = self.$el.find('img.delete').data('id');
            var model = this.getDeletableModel(entityId);
            var result = model.destroy();
            if (result) {
                result.complete(function(response) {
                    if (response.status == 200) {
                        self.$el.empty();

                        var i = self.findIndex(entityId);
                        self.collection.remove(self.collection.at(i));
                        var entityList = self.buildEntityList();
                        backboneGlobalObj.trigger('entity:displaylist', entityList);
                        backboneGlobalObj.trigger('entity:count', {
                            'entityType': self.entityType,
                            'relativeValue': -1
                        });
                    }
                })
            }
        }
    });

})(jQuery, window, document);