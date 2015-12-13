'use strict';

angular.module('adminToolApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


