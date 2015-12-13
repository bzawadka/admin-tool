'use strict';

angular.module('adminToolApp')
	.controller('MessageVersionDeleteController', function($scope, $modalInstance, entity, MessageVersion) {

        $scope.messageVersion = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MessageVersion.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });