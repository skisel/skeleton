'use strict'
define(['./module'], (module) ->
  module.controller('SampleCtrl', ($scope, ConnectionService, SampleModel) ->
    $scope.$watch(
      -> SampleModel.list
      (list) ->
        $scope.list = list
    )

    $scope.request = () ->
      console.log("request")
      ConnectionService.send('sample-incoming',{
        id : "request"
      })

    $scope.request2 = () ->
      console.log("request2")
      ConnectionService.request('sample-incoming', {
        id: "request2"
      })
      .then((response) ->
        console.log('opa', response)
      )

  )
)
