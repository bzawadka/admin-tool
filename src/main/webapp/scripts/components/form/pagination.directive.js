/* globals $ */
'use strict';

angular.module('adminToolApp')
    .directive('adminToolAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
