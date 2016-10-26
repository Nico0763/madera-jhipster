(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('module-nature', {
            parent: 'entity',
            url: '/module-nature?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.module_nature.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/module-nature/module-natures.html',
                    controller: 'Module_natureController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('module_nature');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('module-nature-detail', {
            parent: 'entity',
            url: '/module-nature/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.module_nature.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/module-nature/module-nature-detail.html',
                    controller: 'Module_natureDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('module_nature');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Module_nature', function($stateParams, Module_nature) {
                    return Module_nature.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'module-nature',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('module-nature-detail.edit', {
            parent: 'module-nature-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/module-nature/module-nature-dialog.html',
                    controller: 'Module_natureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Module_nature', function(Module_nature) {
                            return Module_nature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('module-nature.new', {
            parent: 'module-nature',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/module-nature/module-nature-dialog.html',
                    controller: 'Module_natureDialogController',
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
                    $state.go('module-nature', null, { reload: 'module-nature' });
                }, function() {
                    $state.go('module-nature');
                });
            }]
        })
        .state('module-nature.edit', {
            parent: 'module-nature',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/module-nature/module-nature-dialog.html',
                    controller: 'Module_natureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Module_nature', function(Module_nature) {
                            return Module_nature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('module-nature', null, { reload: 'module-nature' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('module-nature.delete', {
            parent: 'module-nature',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/module-nature/module-nature-delete-dialog.html',
                    controller: 'Module_natureDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Module_nature', function(Module_nature) {
                            return Module_nature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('module-nature', null, { reload: 'module-nature' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
