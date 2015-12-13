'use strict';

angular.module('adminToolApp')
    .controller('BrokerStatusDetailController', function ($scope, $rootScope, $stateParams, entity, BrokerStatus) {
        $scope.brokerStatus = entity;
        $scope.load = function (id) {
            BrokerStatus.get({id: id}, function(result) {
                $scope.brokerStatus = result;
            });
        };
        var unsubscribe = $rootScope.$on('adminToolApp:brokerStatusUpdate', function(event, result) {
            $scope.brokerStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
