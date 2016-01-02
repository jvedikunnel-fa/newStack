/**
 * Created by jvedikunnel on 15/12/2015.
 */

var View = require('./view');
var store = require('lib/persistence');

module.exports = View.extend({
    // Le template principal
    template: require('./templates/history'),
    checkinsTemplate: require('./templates/check_ins'),

    subscriptions: {
        'checkins:reset': 'render', // render n'existe pas encore, appelera this['render']() après
        'checkins:new': 'insertCheckIn'
    },

    getRenderData: function homeRenderData () {
        return {
            list: this.renderTemplate({checkIns: store.getCheckIns()}, this.checkinsTemplate)
        };
    },

    insertCheckIn: function insertCheckIn(checkIn) {
        checkIn.extra_class = 'new';
        var markup = this.renderTemplate({checkIns: [checkIn]}, this.checkinsTemplate); // récupération du "<li>"

        var list = this.$('#history');
        list.prepend(markup);
        setTimeout(function() {
            list.find('li.new').removeClass('new');
        }, 0); // pour être sûr que l'élément a été ajouté dans le dom
    }
});
