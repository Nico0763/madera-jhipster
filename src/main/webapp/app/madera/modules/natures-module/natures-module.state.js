(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];
 
    function stateConfig($stateProvider) {
        $stateProvider
             .state('madera-admin.natures-module', {
                parent: 'madera-admin',
                url: 'natures-module?page&sort&search',
                data: {
                authorities: ['ROLE_USER']
                },
                views: {
                    'main@madera-admin': {
                        templateUrl: 'app/madera/modules/natures-module/natures-module.html',
                        controller: 'NaturesModuleController',
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
              .state('madera-admin.natures-module.delete', {
            parent: 'madera-admin.natures-module',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/modules/natures-module/natures-module-delete.html',
                    controller: 'NaturesModuleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Module_nature', function(Module_nature) {
                            return Module_nature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.natures-module', null, { reload: 'madera-admin.natures-module' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
