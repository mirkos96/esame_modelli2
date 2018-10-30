(function($, window, document) {

    "use strict"

    var NOTE_URL_ROOT = '/iPersonal/dashboard/notes';

    var Note = Base.extend({
        urlRoot: NOTE_URL_ROOT,

        initialize: function() {
            this.mandatory = {
                    'title': true,
                    'note': true
                },
                this.maxLength = {
                    'title': 50,
                    'note': 1000
                },
                this.formAttributes = ['title', 'note']
        }

    });

    var Notes = Backbone.Collection.extend({
        model: Note
    });

    var NoteView = BaseView.extend({

        el: $('#note-wrapper'),
        entityType: 'NOTE',
        upsertTemplate: $('#note-upsert-template').html(),
        displayTemplate: $('#note-display-template').html(),

        events: {
            'click #note-submit': 'upsertNote',
            'click #note-cancel': 'resetValues',
            'click #note-tag-img': 'displayTagSelection',
        },

        getModel: function(id) {

            return new Note({
                title: '',
                note: '',
                tags: []
            });
        },


        buildModel: function(entity) {
            return new Note(entity);
        },

        prepareVariables: function() {

            this.saveForm = $('#note-form');
            this.tagImage = $('#note-tag-img');
            this.searchTag = $('#note-tag');
            this.noteRTE = $('#note');
        },

        initializeUpdateForm: function() {
            this.prepareVariables();
            Init.initNote();
        },

        resetValues: function(e) {

            if (e) {
                e.preventDefault();
            }
            BaseView.prototype.resetValues.apply(this, arguments);
            this.noteRTE.empty();
        },

        upsertNote: function(e) {

            var self = this;
            e.preventDefault();

            var entityId = self.saveForm.find('.entityId').html();

            if (entityId) {
                self.model = new Note({
                    id: entityId,
                    noteId: entityId
                });
            } else {
                self.model = new Note();
            }

            self.model.set({
                title: self.saveForm.find('[name=title]').val(),
                note: self.saveForm.find('div#note').cleanHtml(),
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
                            self.postCreation(response.responseText, "NOTE", self.model.get('title'), 1, tags);
                            self.model.set({
                                id: response.responseText,
                                'createdOn': Math.floor(Date.now()),
                                'modifiedAt': Math.floor(Date.now()),
                                'summary': self.model.get('note').replace(/<(?:.|\n)*?>/gm, '').trim(),
                                'tags': tags
                            });
                        } else {
                            var summary = self.model.get('note').replace(/<(?:.|\n)*?>/gm, '').trim();
                            self.postCreation(entityId, "NOTE", self.model.get('title'), 0, tags);
                            self.model.set({
                                id: entityId,
                                'modifiedAt': Math.floor(Date.now()),
                                'summary': summary,
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

        fetchNotes: function(e) {

            var self = this;

            if (e) {
                e.preventDefault();
            }

            if (this.collection.length == parseInt(entityCountModel.attributes.notes)) {
                var entityList = this.buildEntityList();
                backboneGlobalObj.trigger('entity:displaylist', entityList);
                return;
            }

            this.model = new Note();
            this.model.fetch({
                data: {
                    offset: this.collection.length,
                    limit: 20
                }
            }).complete(function(response) {
                if (response.status == 200) {
                    var notes = JSON.parse(response.responseText)['note'];
                    if (notes instanceof Array) {
                        for (var index in notes) {
                            var note = new Note(notes[index]);
                            note.set({
                                id: notes[index]['noteId']
                            });
                            if (note.attributes.tags && !(note.attributes.tags instanceof Array)) {
                                var tags = [];
                                tags.push(note.attributes.tags);
                                note.set({
                                    tags: tags
                                });
                            }
                            self.collection.push(note);
                        }
                    } else if (notes) {
                        var note = new Note(notes);
                        note.set({
                            id: notes['noteId']
                        });
                        if (note.attributes.tags && !(note.attributes.tags instanceof Array)) {
                            var tags = [];
                            tags.push(note.attributes.tags);
                            note.set({
                                tags: tags
                            });
                        }
                        self.collection.push(note);
                    }
                    var entityList = self.buildEntityList();
                    backboneGlobalObj.trigger('entity:displaylist', entityList);
                }
            });
        },

        buildEntityList: function() {

            var entityList = [];

            for (var i = 0; i < this.collection.length; i++) {
                var summary = this.collection.models[i].attributes.summary;
                var entity = {
                    'entityId': this.collection.models[i].attributes.id,
                    'entityTitle': this.collection.models[i].attributes.title,
                    'entitySummary': summary ? summary.substring(0, 100) : summary,
                    'entityType': 'note',
                    'modifiedAt': this.collection.models[i].attributes.modifiedAt,
                };
                entityList.push(entity);
            }
            return entityList;
        },

        getDeletableModel: function(id) {

            return new Note({
                id: id
            });
        }
    });

    window.noteView = new NoteView({
        collection: new Notes()
    });

})(jQuery, window, document);