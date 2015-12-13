'use strict';

angular.module('adminToolApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('brokerStatus', {
                parent: 'entity',
                url: '/brokerStatuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BrokerStatuss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/brokerStatus/brokerStatuss.html',
                        controller: 'BrokerStatusController'
                    }
                },
                resolve: {
                }
            })
            .state('brokerStatus.detail', {
                parent: 'entity',
                url: '/brokerStatus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BrokerStatus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/brokerStatus/brokerStatus-detail.html',
                        controller: 'BrokerStatusDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'BrokerStatus', function($stateParams, BrokerStatus) {
                        return BrokerStatus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('brokerStatus.new', {
                parent: 'brokerStatus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/brokerStatus/brokerStatus-dialog.html',
                        controller: 'BrokerStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('brokerStatus', null, { reload: true });
                    }, function() {
                        $state.go('brokerStatus');
                    })
                }]
            })
            .state('brokerStatus.edit', {
                parent: 'brokerStatus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/brokerStatus/brokerStatus-dialog.html',
                        controller: 'BrokerStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['BrokerStatus', function(BrokerStatus) {
                                return BrokerStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('brokerStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('brokerStatus.delete', {
                parent: 'brokerStatus',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/brokerStatus/brokerStatus-delete-dialog.html',
                        controller: 'BrokerStatusDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['BrokerStatus', function(BrokerStatus) {
                                return BrokerStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('brokerStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
