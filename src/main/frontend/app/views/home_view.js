// Contrôleur principal
// ====================

'use strict';

var View = require('./view');
var moment = require('moment');
var userName = require('lib/notifications').userName;
var cnxSvc = require('lib/connectivity');

var CheckInView = require('./check_in_views');
var CheckIns = require('./history_views');

module.exports = View.extend({
  // Le template principal
  template: require('./templates/home'),

  subscriptions : {
    'connectivity:online' : 'syncMarker',
    'connectivity:offline' : 'syncMarker'
  },

  getRenderData: function homeRenderData () {
    return {
      userName: userName,
      now: moment().format('dddd D MMMM YYYY HH:mm:ss')
    };
  },

  afterRender: function  homeAfterRender() {
    this.startClock();
    this.syncMarker();
    new CheckInView({el : this.$('#checkInUI')}).render();
    new CheckIns({el : this.$('#historyUI')}).render();
  },

  startClock: function startClock() {
    var clock = this.$('#ticker');
    var that = this;
    setInterval(function() {
      clock.text(that.getRenderData().now);
    }, 1000);
    //setInterval(function() {
    //  clock.text(this.getRenderData().now);
    //}.bind(this), 1000); // compatible après IE9
  },

  syncMarker: function syncMarker() {
    this.onlineMarker = this.onlineMarker || this.$('#onlineMarker'); // on cache la recherche de l'élément en l'ajoutant dans l'objet Vue
    this.onlineMarker[cnxSvc.isOnline() ? 'show' : 'hide']('fast'); // appel de la méthode show ou hide de onlineMarker en fonction du critère
  }
});
