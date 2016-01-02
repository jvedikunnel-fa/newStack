/**
 * Created by jvedikunnel on 15/12/2015.
 */
var $ = require('jquery');
var _ = require('underscore');
//var io = require('socket.io');
//var store = require('lib/persistence');

var userName = sessionStorage.userName || $.trim(prompt("Votre nom d'utilisateur ?"));

if (userName) {
    sessionStorage.userName = userName;
} else {
    userName = "Anonymous_" + _.random(0, 1000);
}

//var socket = io.connect();
//socket.on('checkin', function(checkIn) {
//    store.addCheckIn(checkIn);
//});

exports.userName = userName;