'use strict'
define(['./module', 'bootbox'], (module, bootbox) ->
  module.factory('SampleModel', ($rootScope) ->
    model = this

    this.onRequest = (json) ->
      console.log('onRequest', json)
      model.request = json
      $rootScope.$apply()

    this.onPing = (json) ->
      console.log('onPing', json)

    model
  )
)
