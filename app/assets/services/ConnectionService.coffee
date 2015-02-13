'use strict'
define(['./module'], (module) ->
  module.factory("ConnectionService", (
    $location,
    $q,
    $timeout,
    $rootScope

    SampleModel
  ) ->
    new class ConnectionService
      socketURL =
        if $location.protocol() is "https"
          "wss://#{ $location.host() }:#{ $location.port() }/ws"
        else
          "ws://#{ $location.host() }:#{ $location.port() }/ws"

      constructor: ->
        @messageBuffer = []
        @id = 0
        ###todo: cleanup required ###
        @requests = []
        do @connect

      connect: =>
        @socket = new WebSocket socketURL
        @socket.onopen = =>
          @unbuffer()
          @socket.onmessage = @onmessage

        @socket.onclose = =>
          setTimeout @connect, 1000

      onmessage: (event) =>
        console.log("got event", event)
        json = JSON.parse event.data
        if (json.requestId)
          defer = r.promise for r in @requests when r.requestId is json.requestId
          if (json.errMsg)
            defer.reject(json.errMsg)
          else
            defer.resolve(json.args)
          @requests = @requests.filter (c) -> c.requestId isnt json.requestId
        else switch json.type
          when "sample-outgoing" then SampleModel.onRequest(json.args)
          when "ping-event" then SampleModel.onPing(json.args)
        $timeout(() ->
          $rootScope.$apply()
          return
        )

      unbuffer: () => if (@messageBuffer.length)
        cachedMessages = @messageBuffer
        @messageBuffer = []
        for message in cachedMessages
          @_send message
        return

      request: (name, json) =>
        @id = @id + 1
        defer = $q.defer()
        @requests.push {requestId: @id.toString(), promise: defer}
        msg = JSON.stringify
                    "type": name
                    "args": json
                    "requestId" : @id.toString()
        if @connected()
          @socket.send msg
        else
          @messageBuffer.push msg
        return defer.promise


      send: (name, json) =>
        msg = JSON.stringify
                    "type": name
                    "args": json
        if @connected()
          @_send msg
        else
          @messageBuffer.push msg

      _send: (msg) =>
        @socket.send msg

      connected: -> @socket.readyState == WebSocket.OPEN
  )
)
