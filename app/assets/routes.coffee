'use strict'

define(['./app'], (app) ->
  app.config(['$routeProvider', ($routeProvider) ->
    $routeProvider

    .when("/sample",
      templateUrl: "/assets/sample/Sample.html"
      controller: "SampleCtrl"
    )

    .otherwise redirectTo: "/sample"
  ])

)
