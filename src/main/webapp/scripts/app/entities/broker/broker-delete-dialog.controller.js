'use strict';

angular.module('adminToolApp')
	.controller('BrokerDeleteController', function($scope, $modalInstance, entity, Broker) {

        $scope.broker = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Broker.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });