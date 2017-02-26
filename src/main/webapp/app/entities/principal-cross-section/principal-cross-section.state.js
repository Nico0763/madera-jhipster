(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('principal-cross-section', {
            parent: 'entity',
            url: '/principal-cross-section?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.principal_cross_section.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/principal-cross-section/principal-cross-sections.html',
                    controller: 'Principal_cross_sectionController',
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
                    $translatePartialLoader.addPart('principal_cross_section');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('principal-cross-section-detail', {
            parent: 'entity',
            url: '/principal-cross-section/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'maderaApp.principal_cross_section.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/principal-cross-section/principal-cross-section-detail.html',
                    controller: 'Principal_cross_sectionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('principal_cross_section');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Principal_cross_section', function($stateParams, Principal_cross_section) {
                    return Principal_cross_section.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'principal-cross-section',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('principal-cross-section-detail.edit', {
            parent: 'principal-cross-section-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/principal-cross-section/principal-cross-section-dialog.html',
                    controller: 'Principal_cross_sectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Principal_cross_section', function(Principal_cross_section) {
                            return Principal_cross_section.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('principal-cross-section.new', {
            parent: 'principal-cross-section',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/principal-cross-section/principal-cross-section-dialog.html',
                    controller: 'Principal_cross_sectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                url: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('principal-cross-section', null, { reload: 'principal-cross-section' });
                }, function() {
                    $state.go('principal-cross-section');
                });
            }]
        })
        .state('principal-cross-section.edit', {
            parent: 'principal-cross-section',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/principal-cross-section/principal-cross-section-dialog.html',
                    controller: 'Principal_cross_sectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Principal_cross_section', function(Principal_cross_section) {
                            return Principal_cross_section.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('principal-cross-section', null, { reload: 'principal-cross-section' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('principal-cross-section.delete', {
            parent: 'principal-cross-section',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/principal-cross-section/principal-cross-section-delete-dialog.html',
                    controller: 'Principal_cross_sectionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Principal_cross_section', function(Principal_cross_section) {
                            return Principal_cross_section.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('principal-cross-section', null, { reload: 'principal-cross-section' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
