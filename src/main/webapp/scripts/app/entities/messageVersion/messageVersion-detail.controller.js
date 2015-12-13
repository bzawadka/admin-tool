'use strict';

angular.module('adminToolApp')
    .controller('MessageVersionDetailController', function ($scope, $rootScope, $stateParams, entity, MessageVersion) {
        $scope.messageVersion = entity;
        $scope.load = function (id) {
            MessageVersion.get({id: id}, function(result) {
                $scope.messageVersion = result;
            });
        };
        var unsubscribe = $rootScope.$on('adminToolApp:messageVersionUpdate', function(event, result) {
            $scope.messageVersion = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
