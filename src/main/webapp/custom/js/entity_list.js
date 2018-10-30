(function($, window, document) {

    "use strict"

    var EntityListView = Backbone.View.extend({

        el: $('#list-wrapper'),
        listTemplate: $('#entity-list-template').html(),

        events: {
            'click li.clickable': 'displayEntity',
            'click a.more': 'fetchMore'
        },

        initialize: function() {

        },

        fetchMore: function(e) {
            e.preventDefault();
            var entity = $(e.target).data('entity');
            backboneGlobalObj.trigger('entity:fetchlist', entity.toUpperCase());
        },

        renderList: function(entities) {

            var count = 0;
            if (entities.length > 0) {
                var entityType = entities[0].entityType + 's';
                count = entityCountModel.get(entityType)
            }
            this.$el.empty();
            var template = _.template(this.listTemplate);
            this.$el.html(template({
                'entities': entities,
                'count': count
            }));
            return this;
        },

        displayEntity: function(e) {
            var self = this;
            var root = $(e.target).closest('li.clickable');
            var entity = root.data('entity');
            var entityId = root.data('id');

            switch (entity.toUpperCase()) {

                case 'BOOKMARK':
                    bookmarkView.displayEntity(entityId, 'id', 'bookmark');
                    break;
                case 'NOTE':
                    noteView.displayEntity(entityId, 'id', 'note');
                    break;
                case 'PIN':
                    pinView.displayEntity(entityId, 'id', 'pin');
                    break;
                case 'TODO':
                    todoView.displayEntity(entityId, 'id', 'todo');
                    break;
                case 'DIARY':
                    diaryView.displayEntity(entityId, 'id', 'diary');
                    break;
                case 'EXPENSE':
                    expenseView.displayEntity(entityId, 'id', 'expense');
                    break;
            }
        }
    });

    window.entityListView = new EntityListView();
})(jQuery, window, document);