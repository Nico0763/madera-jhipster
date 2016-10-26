(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('component-product', {
            parent: 'entity',
            url: '/component-product?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.component_product.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/component-product/component-products.html',
                    controller: 'Component_productController',
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
                    $translatePartialLoader.addPart('component_product');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('component-product-detail', {
            parent: 'entity',
            url: '/component-product/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.component_product.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/component-product/component-product-detail.html',
                    controller: 'Component_productDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('component_product');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Component_product', function($stateParams, Component_product) {
                    return Component_product.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'component-product',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('component-product-detail.edit', {
            parent: 'component-product-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/component-product/component-product-dialog.html',
                    controller: 'Component_productDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Component_product', function(Component_product) {
                            return Component_product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('component-product.new', {
            parent: 'component-product',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/component-product/component-product-dialog.html',
                    controller: 'Component_productDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                angle: null,
                                length: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('component-product', null, { reload: 'component-product' });
                }, function() {
                    $state.go('component-product');
                });
            }]
        })
        .state('component-product.edit', {
            parent: 'component-product',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/component-product/component-product-dialog.html',
                    controller: 'Component_productDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Component_product', function(Component_product) {
                            return Component_product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('component-product', null, { reload: 'component-product' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('component-product.delete', {
            parent: 'component-product',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/component-product/component-product-delete-dialog.html',
                    controller: 'Component_productDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Component_product', function(Component_product) {
                            return Component_product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('component-product', null, { reload: 'component-product' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
