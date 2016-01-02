/**
 * Created by jvedikunnel on 15/12/2015.
 */

var CheckInsCollection = require('models/collection');
var Backbone = require('backbone');
var cnxSvc = require('lib/connectivity');
var _ = require('underscore');

var collection = new CheckInsCollection();
//console.log(collection);
var Lawnchair = require('lawnchair');
require('lawnchair-dom'); // va s'enregistrer sur un Lawnchair existant

var localStore = new Lawnchair(
    {name: 'checkins'},
    function(){} // obligatoire mais pas utilisé
);

function addCheckIn(checkIn) {
    checkIn.key = checkIn.key || Date.now(); // à mettre avant car _.pick(checkIn, 'key', 'userName') retourne {} qui match n'importe quel objet de la collection
    if (collection.findWhere(_.pick(checkIn, 'key', 'userName'))) {  // si le checkin a déjà été mis dans la collection
        return;
    }

    collection['id' in checkIn ? 'add' : 'create'](checkIn); // create = add + save
}

var pendings;

function initialLoad() {
    if (!cnxSvc.isOnline()) {
        return;
    }

    pendings = collection.filter(function(m) {
        return m.isNew();
    });

    if (pendings.length) {
        collection.on('sync', accountForSync); // event sync envoyé par backbone quand envoyé au serveur
        _.invoke(pendings, 'save'); // exécute le save sur chaque élément de pendings
    } else {
        collection.fetch({reset: true}); // reset permet d'envoyer un event qu'on pourra écouter pour render la vue
    }
}

Backbone.Mediator.subscribe('connectivity:online', initialLoad);

function localLoad() {
    localStore.all(function(checkins) {
        collection.reset(checkins, {localLoad: true});
        initialLoad();
    });
}

function accountForSync(model) {
    // suppression de l'élément synchronisé par backbone
    pendings = _.without(pendings, model); // recharge les pendings sans l'élément synchronisé
    if (pendings.length) {
        return;
    }

    collection.off('sync', accountForSync); // suppression du listener pour ne pas refaire le fetch à caque sauvegarde suivante

    // il n'y en a plus : on fetch
    collection.fetch({reset: true});
}

collection.on('reset', function(_, options){  // je n'utilise pas le 1er param
    if (options === undefined || !options.localLoad) {
        localStore.nuke(function(){
            localStore.batch(collection.toJSON());
        }); // supprime et récrée la collection
    }
    Backbone.Mediator.publish('checkins:reset');
});

collection.on('add', function(model){ // vue dans l'api backbone ce que renvoie le add
    localStore.save(model.toJSON()); // ajout du checkin
    Backbone.Mediator.publish('checkins:new', model.toJSON());
});

collection.on('sync', function(model) {
    if (!(model instanceof collection.model)) {
        return;
    }
    localStore.save(model.toJSON());
});

localLoad();

function getCheckIns() {
    return collection.toJSON();
}

// methode dédiée au test
function getLocalStore(cb) {
    localStore.all(cb);
}

exports.addCheckIn = addCheckIn;
exports.getCheckIns = getCheckIns;
exports.getLocalStoreForTests = getLocalStore;