'use strict';

angular.module('adminToolApp')
	.controller('BrokerStatusDeleteController', function($scope, $modalInstance, entity, BrokerStatus) {

        $scope.brokerStatus = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            BrokerStatus.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });