# Configuration Brunch
# ====================

exports.config =
  # See https://github.com/brunch/brunch/blob/stable/docs/config.md for documentation.
  paths:
    public: 'target/public'
    watched: ['src/main/frontend/app','src/test/frontend/app','src/main/frontend/vendor']

  files:
    javascripts:
      joinTo: 'javascript/app.js'
    stylesheets:
      joinTo: 'css/app.css'
    templates:
      joinTo: 'javascript/app.js'

  modules:
    nameCleaner: (path) ->
      path
        # Strip app/ and app/externals/ prefixes
        .replace /^src\/main\/frontend\/app\/(?:externals\/)?/, ''
        # Allow -x.y[.z…] version suffixes in mantisses
        .replace /-\d+(?:\.\d+)+/, ''
        # Allow -fr lang suffixes in mantisses
        .replace '-fr.', '.'

  plugins:
    appcache:
      externalCacheEntries: [
        'http://maps.gstatic.com/mapfiles/place_api/icons/bar-71.png'
        'http://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png'
        'http://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png'
        'http://maps.gstatic.com/mapfiles/place_api/icons/wine-71.png'
      ]
      network: ['*', 'http://*', 'https://*']

    handlebars:
      pathReplace: /0^/ # match nothing, use full file path for module name

    cleancss:
      keepSpecialComments: 0
      removeEmpty: true

    uglify:
      mangle:
        sort: true
        toplevel: true
        eval: true
      compress: true
      beautify: false
      verbose: true

  watcher:
    usePolling: true
