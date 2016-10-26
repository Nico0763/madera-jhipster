(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('finition-ext', {
            parent: 'entity',
            url: '/finition-ext?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.finition_ext.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/finition-ext/finition-exts.html',
                    controller: 'Finition_extController',
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
                    $translatePartialLoader.addPart('finition_ext');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('finition-ext-detail', {
            parent: 'entity',
            url: '/finition-ext/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.finition_ext.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/finition-ext/finition-ext-detail.html',
                    controller: 'Finition_extDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('finition_ext');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Finition_ext', function($stateParams, Finition_ext) {
                    return Finition_ext.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'finition-ext',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('finition-ext-detail.edit', {
            parent: 'finition-ext-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/finition-ext/finition-ext-dialog.html',
                    controller: 'Finition_extDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Finition_ext', function(Finition_ext) {
                            return Finition_ext.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('finition-ext.new', {
            parent: 'finition-ext',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/finition-ext/finition-ext-dialog.html',
                    controller: 'Finition_extDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                image: null,
                                imageContentType: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('finition-ext', null, { reload: 'finition-ext' });
                }, function() {
                    $state.go('finition-ext');
                });
            }]
        })
        .state('finition-ext.edit', {
            parent: 'finition-ext',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/finition-ext/finition-ext-dialog.html',
                    controller: 'Finition_extDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Finition_ext', function(Finition_ext) {
                            return Finition_ext.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('finition-ext', null, { reload: 'finition-ext' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('finition-ext.delete', {
            parent: 'finition-ext',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/finition-ext/finition-ext-delete-dialog.html',
                    controller: 'Finition_extDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Finition_ext', function(Finition_ext) {
                            return Finition_ext.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('finition-ext', null, { reload: 'finition-ext' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
