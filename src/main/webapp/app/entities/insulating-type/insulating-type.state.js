(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('insulating-type', {
            parent: 'entity',
            url: '/insulating-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.insulating_type.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insulating-type/insulating-types.html',
                    controller: 'Insulating_typeController',
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
                    $translatePartialLoader.addPart('insulating_type');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('insulating-type-detail', {
            parent: 'entity',
            url: '/insulating-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.insulating_type.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insulating-type/insulating-type-detail.html',
                    controller: 'Insulating_typeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('insulating_type');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Insulating_type', function($stateParams, Insulating_type) {
                    return Insulating_type.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'insulating-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('insulating-type-detail.edit', {
            parent: 'insulating-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insulating-type/insulating-type-dialog.html',
                    controller: 'Insulating_typeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Insulating_type', function(Insulating_type) {
                            return Insulating_type.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insulating-type.new', {
            parent: 'insulating-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insulating-type/insulating-type-dialog.html',
                    controller: 'Insulating_typeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('insulating-type', null, { reload: 'insulating-type' });
                }, function() {
                    $state.go('insulating-type');
                });
            }]
        })
        .state('insulating-type.edit', {
            parent: 'insulating-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insulating-type/insulating-type-dialog.html',
                    controller: 'Insulating_typeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Insulating_type', function(Insulating_type) {
                            return Insulating_type.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insulating-type', null, { reload: 'insulating-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insulating-type.delete', {
            parent: 'insulating-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insulating-type/insulating-type-delete-dialog.html',
                    controller: 'Insulating_typeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Insulating_type', function(Insulating_type) {
                            return Insulating_type.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insulating-type', null, { reload: 'insulating-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
