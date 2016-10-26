(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('unit-used', {
            parent: 'entity',
            url: '/unit-used?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.unit_used.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/unit-used/unit-useds.html',
                    controller: 'Unit_usedController',
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
                    $translatePartialLoader.addPart('unit_used');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('unit-used-detail', {
            parent: 'entity',
            url: '/unit-used/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.unit_used.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/unit-used/unit-used-detail.html',
                    controller: 'Unit_usedDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('unit_used');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Unit_used', function($stateParams, Unit_used) {
                    return Unit_used.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'unit-used',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('unit-used-detail.edit', {
            parent: 'unit-used-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unit-used/unit-used-dialog.html',
                    controller: 'Unit_usedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Unit_used', function(Unit_used) {
                            return Unit_used.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('unit-used.new', {
            parent: 'unit-used',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unit-used/unit-used-dialog.html',
                    controller: 'Unit_usedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                regular_expression: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('unit-used', null, { reload: 'unit-used' });
                }, function() {
                    $state.go('unit-used');
                });
            }]
        })
        .state('unit-used.edit', {
            parent: 'unit-used',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unit-used/unit-used-dialog.html',
                    controller: 'Unit_usedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Unit_used', function(Unit_used) {
                            return Unit_used.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('unit-used', null, { reload: 'unit-used' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('unit-used.delete', {
            parent: 'unit-used',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unit-used/unit-used-delete-dialog.html',
                    controller: 'Unit_usedDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Unit_used', function(Unit_used) {
                            return Unit_used.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('unit-used', null, { reload: 'unit-used' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
