'use strict';

angular.module('adminToolApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('messageVersion', {
                parent: 'entity',
                url: '/messageVersions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MessageVersions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/messageVersion/messageVersions.html',
                        controller: 'MessageVersionController'
                    }
                },
                resolve: {
                }
            })
            .state('messageVersion.detail', {
                parent: 'entity',
                url: '/messageVersion/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MessageVersion'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/messageVersion/messageVersion-detail.html',
                        controller: 'MessageVersionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'MessageVersion', function($stateParams, MessageVersion) {
                        return MessageVersion.get({id : $stateParams.id});
                    }]
                }
            })
            .state('messageVersion.new', {
                parent: 'messageVersion',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/messageVersion/messageVersion-dialog.html',
                        controller: 'MessageVersionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    version: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('messageVersion', null, { reload: true });
                    }, function() {
                        $state.go('messageVersion');
                    })
                }]
            })
            .state('messageVersion.edit', {
                parent: 'messageVersion',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/messageVersion/messageVersion-dialog.html',
                        controller: 'MessageVersionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MessageVersion', function(MessageVersion) {
                                return MessageVersion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('messageVersion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('messageVersion.delete', {
                parent: 'messageVersion',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/messageVersion/messageVersion-delete-dialog.html',
                        controller: 'MessageVersionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MessageVersion', function(MessageVersion) {
                                return MessageVersion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('messageVersion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
