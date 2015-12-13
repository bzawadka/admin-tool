'use strict';

angular.module('adminToolApp')
    .controller('BrokerStatusController', function ($scope, $state, $modal, BrokerStatus) {
      
        $scope.brokerStatuss = [];
        $scope.loadAll = function() {
            BrokerStatus.query(function(result) {
               $scope.brokerStatuss = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.brokerStatus = {
                status: null,
                id: null
            };
        };
    });
