'use strict';

angular.module('adminToolApp').controller('BrokerDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Broker', 'BrokerStatus', 'MessageVersion',
        function($scope, $stateParams, $modalInstance, entity, Broker, BrokerStatus, MessageVersion) {

        $scope.broker = entity;
        $scope.brokerstatuss = BrokerStatus.query();
        $scope.messageversions = MessageVersion.query();
        $scope.load = function(id) {
            Broker.get({id : id}, function(result) {
                $scope.broker = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adminToolApp:brokerUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.broker.id != null) {
                Broker.update($scope.broker, onSaveSuccess, onSaveError);
            } else {
                Broker.save($scope.broker, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
