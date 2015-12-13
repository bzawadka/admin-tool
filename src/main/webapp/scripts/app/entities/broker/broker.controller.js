'use strict';

angular.module('adminToolApp')
    .controller('BrokerController', function ($scope, $state, $modal, Broker) {
      
        $scope.brokers = [];
        $scope.loadAll = function() {
            Broker.query(function(result) {
               $scope.brokers = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.broker = {
                name: null,
                comment: null,
                id: null
            };
        };
    });
