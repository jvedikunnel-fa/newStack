/**
 * Created by jvedikunnel on 15/12/2015.
 */
'use strict';

var View = require('./view');
var CheckInUX = require('models/check_in_ux');
var locSvc = require('lib/location');
var poiSvc = require('lib/places');
var cnxSvc = require('lib/connectivity');
var _ = require('underscore');
var userName = require('lib/notifications').userName;
var store = require('lib/persistence');

module.exports = View.extend({
    // Le template principal
    template: require('./templates/check_in'),
    placesTemplate: require('./templates/places'),

    bindings: {
        '#comment': 'comment',
        '#geoloc': {
            observe: ['lat', 'lng'],
            onGet: function(pos) { // en param le contenu d'observe
                if (_.isString(pos[0]) || pos[0] === 0 && pos[1] === 0)
                    return "Je suis...";
                return pos[0] + " / " + pos[1];
            }
        },
        '#places': {
            observe: ['places', 'placeId'],
            onGet: function() {
                return this.renderTemplate(this.model.pick('places', 'placeId'), this.placesTemplate);
            },
            updateMethod: 'html'
        },
        'button[type=submit]': {
            attributes: [{
                name: 'disabled',
                observe: 'checkInForbidden'
            }]
        },
        'header button': {
            attributes: [{
                name: 'disabled',
                observe: 'fetchPlacesForbidden'
            }]
        }
    },

    events: {
        'click header button' : 'fetchPlaces', // this.$el.on('click', 'header button', this.fetchPlaces.bind(this);
        'click #places li' : 'selectPlace',
        'submit': 'checkIn'
    },

    initialize: function() {
        // super
        View.prototype.initialize.apply(this, arguments);

        this.model = new CheckInUX();
    },

    afterRender: function afterCheckInRender() {
        this.fetchPlaces();
    },

    checkIn: function checkIn(event) {
        event.preventDefault();

        var place = this.model.getPlace();

        store.addCheckIn({
            placeId: place.id,
            comment: this.model.get('comment'),
            userName: userName,
            name: place.name,
            icon: place.icon,
            vicinity: place.vicinity
        });

        this.model.set({placeId: undefined, comment: ''});
        //this.model.set(_.pick(this.model.defaults, 'placeId', 'comment')); // avec la lib underscore
    },

    selectPlace: function (event) {
        //var placeId = event.currentTarget.getAttribute('data-place-id');
        var placeId = this.$(event.currentTarget).attr('data-place-id');
        this.model.set('placeId', placeId);
    },

    fetchPlaces: function fetchPlaces () {
        if (!cnxSvc.isOnline()) {
            return;
        }
        var that = this;
        this.model.set(that.model.defaults);
        locSvc.getCurrentLocation(function(lat, lng) {
            //console.log(lat, lng);
            that.model.set({
                lat: lat,
                lng: lng
            });
            //console.log(that.model);

            poiSvc.lookupPlaces(lat, lng, function(places) {
                //console.table(places);
                that.model.set('places', places);
            });
        });
    }
});
