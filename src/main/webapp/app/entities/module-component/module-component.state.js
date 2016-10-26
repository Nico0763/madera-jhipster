(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('module-component', {
            parent: 'entity',
            url: '/module-component?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.module_component.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/module-component/module-components.html',
                    controller: 'Module_componentController',
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
                    $translatePartialLoader.addPart('module_component');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('module-component-detail', {
            parent: 'entity',
            url: '/module-component/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.module_component.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/module-component/module-component-detail.html',
                    controller: 'Module_componentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('module_component');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Module_component', function($stateParams, Module_component) {
                    return Module_component.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'module-component',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('module-component-detail.edit', {
            parent: 'module-component-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/module-component/module-component-dialog.html',
                    controller: 'Module_componentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Module_component', function(Module_component) {
                            return Module_component.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('module-component.new', {
            parent: 'module-component',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/module-component/module-component-dialog.html',
                    controller: 'Module_componentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                quantity: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('module-component', null, { reload: 'module-component' });
                }, function() {
                    $state.go('module-component');
                });
            }]
        })
        .state('module-component.edit', {
            parent: 'module-component',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/module-component/module-component-dialog.html',
                    controller: 'Module_componentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Module_component', function(Module_component) {
                            return Module_component.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('module-component', null, { reload: 'module-component' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('module-component.delete', {
            parent: 'module-component',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/module-component/module-component-delete-dialog.html',
                    controller: 'Module_componentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Module_component', function(Module_component) {
                            return Module_component.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('module-component', null, { reload: 'module-component' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
