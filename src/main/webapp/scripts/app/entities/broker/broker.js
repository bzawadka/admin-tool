'use strict';

angular.module('adminToolApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('broker', {
                parent: 'entity',
                url: '/brokers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Brokers'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/broker/brokers.html',
                        controller: 'BrokerController'
                    }
                },
                resolve: {
                }
            })
            .state('broker.detail', {
                parent: 'entity',
                url: '/broker/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Broker'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/broker/broker-detail.html',
                        controller: 'BrokerDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Broker', function($stateParams, Broker) {
                        return Broker.get({id : $stateParams.id});
                    }]
                }
            })
            .state('broker.new', {
                parent: 'broker',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/broker/broker-dialog.html',
                        controller: 'BrokerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    comment: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('broker', null, { reload: true });
                    }, function() {
                        $state.go('broker');
                    })
                }]
            })
            .state('broker.edit', {
                parent: 'broker',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/broker/broker-dialog.html',
                        controller: 'BrokerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Broker', function(Broker) {
                                return Broker.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('broker', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('broker.delete', {
                parent: 'broker',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/broker/broker-delete-dialog.html',
                        controller: 'BrokerDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Broker', function(Broker) {
                                return Broker.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('broker', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
