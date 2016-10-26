(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('assortment', {
            parent: 'entity',
            url: '/assortment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.assortment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/assortment/assortments.html',
                    controller: 'AssortmentController',
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
                    $translatePartialLoader.addPart('assortment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('assortment-detail', {
            parent: 'entity',
            url: '/assortment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.assortment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/assortment/assortment-detail.html',
                    controller: 'AssortmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('assortment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Assortment', function($stateParams, Assortment) {
                    return Assortment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'assortment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('assortment-detail.edit', {
            parent: 'assortment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/assortment/assortment-dialog.html',
                    controller: 'AssortmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Assortment', function(Assortment) {
                            return Assortment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('assortment.new', {
            parent: 'assortment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/assortment/assortment-dialog.html',
                    controller: 'AssortmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                skeleton_conception_mode: null,
                                skeleton_conception_modeContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('assortment', null, { reload: 'assortment' });
                }, function() {
                    $state.go('assortment');
                });
            }]
        })
        .state('assortment.edit', {
            parent: 'assortment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/assortment/assortment-dialog.html',
                    controller: 'AssortmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Assortment', function(Assortment) {
                            return Assortment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('assortment', null, { reload: 'assortment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('assortment.delete', {
            parent: 'assortment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/assortment/assortment-delete-dialog.html',
                    controller: 'AssortmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Assortment', function(Assortment) {
                            return Assortment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('assortment', null, { reload: 'assortment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
