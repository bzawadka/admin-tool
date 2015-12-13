'use strict';

angular.module('adminToolApp')
    .controller('BrokerDetailController', function ($scope, $rootScope, $stateParams, entity, Broker, BrokerStatus, MessageVersion) {
        $scope.broker = entity;
        $scope.load = function (id) {
            Broker.get({id: id}, function(result) {
                $scope.broker = result;
            });
        };
        var unsubscribe = $rootScope.$on('adminToolApp:brokerUpdate', function(event, result) {
            $scope.broker = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
