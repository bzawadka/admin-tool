'use strict';

angular.module('adminToolApp')
    .controller('MessageVersionController', function ($scope, $state, $modal, MessageVersion) {
      
        $scope.messageVersions = [];
        $scope.loadAll = function() {
            MessageVersion.query(function(result) {
               $scope.messageVersions = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.messageVersion = {
                version: null,
                id: null
            };
        };
    });
