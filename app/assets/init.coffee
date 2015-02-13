'use strict'

requirejs.config({
    paths: {
        "bootbox": "/webjars/bootbox/4.3.0/bootbox"
    },
    shim: {
        "bootbox": ["bootstrap"]
    }
});

require(
  ['angular',
   'angular-route',
   'ui-bootstrap',
   'ui-bootstrap-tpls',
   'angular-ui-select',
   'angular-sanitize',
   'bootbox',
   './app',
   './routes',
   './services/index',
   './sample/index',
  ],

  (angular) ->
    angular.bootstrap(document, ['ui.select','ui.bootstrap', 'app', 'app.services', 'app.sample'])

)