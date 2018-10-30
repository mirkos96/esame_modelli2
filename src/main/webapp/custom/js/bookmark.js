(function($, window, document) {

    "use strict"

    var BOOKMARK_URL_ROOT = '/iPersonal/dashboard/bookmarks';

    var Bookmark = Base.extend({
        urlRoot: BOOKMARK_URL_ROOT,

        initialize: function() {
            this.mandatory = {
                    'name': true,
                    'url': true,
                    'description': true
                },
                this.maxLength = {
                    'name': 50,
                    'url': 150,
                    'description': 1000
                },
                this.formAttributes = ['name', 'url', 'description']
        }

    });

    var Bookmarks = Backbone.Collection.extend({
        model: Bookmark
    });

    var BookmarkView = BaseView.extend({

        entityType: 'BOOKMARK',
        el: $('#bookmark-wrapper'),
        upsertTemplate: $('#bookmark-upsert-template').html(),
        displayTemplate: $('#bookmark-display-template').html(),

        events: {
            'click #book-submit': 'upsertBookmark',
            'click #book-cancel': 'resetValues',
            'click #book-tag-img': 'displayTagSelection'
        },

        getModel: function() {
            return new Bookmark({
                name: '',
                description: '',
                url: '',
                tags: []
            });
        },

        buildModel: function(entity) {
            return new Bookmark(entity);
        },

        prepareVariables: function() {

            this.saveForm = $('#bookmark-form');
            this.tagImage = $('#book-tag-img');
            this.searchTag = $('#bookmark-tag');
        },

        initializeUpdateForm: function() {
            this.prepareVariables();
            Init.initBookmark();
        },

        upsertBookmark: function(e) {

            var self = this;
            e.preventDefault();

            var entityId = self.saveForm.find('.entityId').html();

            if (entityId) {
                self.model = new Bookmark({
                    id: entityId,
                    bookmarkId: entityId
                });
            } else {
                self.model = new Bookmark();
            }

            self.model.set({
                name: self.saveForm.find('[name=name]').val(),
                url: self.saveForm.find('[name=url]').val(),
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
                            self.postCreation(response.responseText, "BOOKMARK", self.model.get('name'), 1, tags);
                            self.model.set({
                                id: response.responseText,
                                'createdOn': Math.floor(Date.now()),
                                'modifiedAt': Math.floor(Date.now()),
                                'tags': tags
                            });
                        } else {
                            self.postCreation(entityId, "BOOKMARK", self.model.get('name'), 0, tags);
                            self.model.set({
                                'id': entityId,
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

        fetchBookmarks: function(e) {

            var self = this;

            if (e) {
                e.preventDefault();
            }

            if (this.collection.length == parseInt(entityCountModel.attributes.bookmarks)) {
                var entityList = this.buildEntityList();
                backboneGlobalObj.trigger('entity:displaylist', entityList);
                return;
            }

            this.model = new Bookmark();
            this.model.fetch({
                data: {
                    offset: this.collection.length,
                    limit: 20
                }
            }).complete(function(response) {
                if (response.status == 200) {
                    var bookmarks = JSON.parse(response.responseText)['bookmark'];
                    if (bookmarks instanceof Array) {
                        for (var index in bookmarks) {
                            var bookmark = new Bookmark(bookmarks[index]);
                            bookmark.set({
                                id: bookmarks[index]['bookmarkId']
                            });

                            if (bookmark.attributes.tags && !(bookmark.attributes.tags instanceof Array)) {
                                var tags = [];
                                tags.push(bookmark.attributes.tags);
                                bookmark.set({
                                    'tags': tags
                                });
                            }
                            self.collection.push(bookmark);
                        }
                    } else if (bookmarks) {
                        var bookmark = new Bookmark(bookmarks);
                        bookmark.set({
                            id: bookmarks['bookmarkId']
                        });
                        if (bookmark.attributes.tags && !(bookmark.attributes.tags instanceof Array)) {
                            var tags = [];
                            tags.push(bookmark.attributes.tags);
                            bookmark.set({
                                'tags': tags
                            });
                        }
                        self.collection.push(bookmark);
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
                    'url': this.collection.models[i].attributes.url,
                    'entitySummary': description ? description.substring(0, 100) : description,
                    'entityType': 'bookmark',
                    'modifiedAt': this.collection.models[i].attributes.modifiedAt,
                };
                entityList.push(entity);
            }
            return entityList;
        },

        getDeletableModel: function(id) {

            return new Bookmark({
                id: id
            });
        }
    });

    window.bookmarkView = new BookmarkView({
        collection: new Bookmarks()
    });

})(jQuery, window, document);