'use strict';

angular.module('adminToolApp').controller('BrokerStatusDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'BrokerStatus',
        function($scope, $stateParams, $modalInstance, entity, BrokerStatus) {

        $scope.brokerStatus = entity;
        $scope.load = function(id) {
            BrokerStatus.get({id : id}, function(result) {
                $scope.brokerStatus = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adminToolApp:brokerStatusUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.brokerStatus.id != null) {
                BrokerStatus.update($scope.brokerStatus, onSaveSuccess, onSaveError);
            } else {
                BrokerStatus.save($scope.brokerStatus, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
