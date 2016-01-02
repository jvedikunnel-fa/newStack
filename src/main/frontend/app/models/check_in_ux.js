/**
 * Created by jvedikunnel on 15/12/2015.
 */

// Modèle : CheckInUX
// ================

'use strict';

var Backbone = require('backbone');
var _ = require('underscore');
var cnxSvc = require('lib/connectivity');

module.exports = Backbone.Model.extend({
    defaults: {
        lat: 0,
        lng: 0,
        places: [],
        comment: '',
        placeId: undefined,
        checkInForbidden: true,
        fetchPlacesForbidden: false
    },
    initialize: function initCheckInUX() {
        var that = this;
        //super
        Backbone.Model.prototype.initialize.apply(this, arguments);

        // on change
        this.on('change', checkCheckinable);

        checkCheckinable();
        checkFetchable();

        Backbone.Mediator.subscribe('connectivity:online', checkFetchable);
        Backbone.Mediator.subscribe('connectivity:offline', checkFetchable);

        function checkCheckinable () {
            that.set('checkInForbidden', that.get('placeId') === undefined);
        }

        function checkFetchable () {
            that.set('fetchPlacesForbidden', !cnxSvc.isOnline());
        }
    },

    getPlace: function() {
        return _.findWhere(this.get('places'), {id: this.get('placeId')});
        // for(var i = 0, i > this.get('places').length; i++) {
        //   if(this.get('placeId') === this.get('places').at(i).get('id')) {
        //     return this.get('places').at(i);
        //   }
        // }
    }
});
