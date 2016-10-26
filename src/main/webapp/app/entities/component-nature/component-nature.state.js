(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('component-nature', {
            parent: 'entity',
            url: '/component-nature',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.component_nature.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/component-nature/component-natures.html',
                    controller: 'Component_natureController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('component_nature');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('component-nature-detail', {
            parent: 'entity',
            url: '/component-nature/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.component_nature.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/component-nature/component-nature-detail.html',
                    controller: 'Component_natureDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('component_nature');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Component_nature', function($stateParams, Component_nature) {
                    return Component_nature.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'component-nature',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('component-nature-detail.edit', {
            parent: 'component-nature-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/component-nature/component-nature-dialog.html',
                    controller: 'Component_natureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Component_nature', function(Component_nature) {
                            return Component_nature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('component-nature.new', {
            parent: 'component-nature',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/component-nature/component-nature-dialog.html',
                    controller: 'Component_natureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nature: null,
                                caracteristics: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('component-nature', null, { reload: 'component-nature' });
                }, function() {
                    $state.go('component-nature');
                });
            }]
        })
        .state('component-nature.edit', {
            parent: 'component-nature',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/component-nature/component-nature-dialog.html',
                    controller: 'Component_natureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Component_nature', function(Component_nature) {
                            return Component_nature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('component-nature', null, { reload: 'component-nature' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('component-nature.delete', {
            parent: 'component-nature',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/component-nature/component-nature-delete-dialog.html',
                    controller: 'Component_natureDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Component_nature', function(Component_nature) {
                            return Component_nature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('component-nature', null, { reload: 'component-nature' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
