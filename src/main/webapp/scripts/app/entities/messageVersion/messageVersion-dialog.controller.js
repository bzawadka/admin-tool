'use strict';

angular.module('adminToolApp').controller('MessageVersionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'MessageVersion',
        function($scope, $stateParams, $modalInstance, entity, MessageVersion) {

        $scope.messageVersion = entity;
        $scope.load = function(id) {
            MessageVersion.get({id : id}, function(result) {
                $scope.messageVersion = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adminToolApp:messageVersionUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.messageVersion.id != null) {
                MessageVersion.update($scope.messageVersion, onSaveSuccess, onSaveError);
            } else {
                MessageVersion.save($scope.messageVersion, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
