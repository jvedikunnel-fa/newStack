// Point d'entrée de l'appli
// =========================

'use strict';

// Toi aussi tu es sur un vieux browser de m… ?
require('console-helper');

// Setup Backbone + Mediator
// -------------------------
var $ = require('jquery');
var Backbone = require('backbone');
// Backbone ne tente pas un require('jquery'), il exige le global.  Bizarre…
Backbone.$ = $;
// Activer les plugins
require('backbone-mediator'); // publish subscribe : transmet info entre 2 couches sans dépendance directe entre module
require('backbone-stickit'); // gestion de l'actualisation des propriétés bindées des modèles
// Charger les modules Bootstrap 3 utiles à notre appli (module custom)
require('bootstrap');

// Initialiseur
// ------------
//
// Se contente de faire deux choses :
//
// 1. Instancier l'application JS et l'initialiser
// 2. Activer la gestion des routes Backbone (même si on ne s'en
//    sert pas particulièrement ici)

var moment = require('moment');
var application = require('application');

$(function() {
  moment.locale('fr');
  application.initialize();
  Backbone.history.start();
});
