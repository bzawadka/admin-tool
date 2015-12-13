'use strict';

angular.module('adminToolApp')
    .factory('Broker', function ($resource, DateUtils) {
        return $resource('api/brokers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
