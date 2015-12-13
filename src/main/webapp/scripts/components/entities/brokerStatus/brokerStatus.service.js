'use strict';

angular.module('adminToolApp')
    .factory('BrokerStatus', function ($resource, DateUtils) {
        return $resource('api/brokerStatuss/:id', {}, {
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
