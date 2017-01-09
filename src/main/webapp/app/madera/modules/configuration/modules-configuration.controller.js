(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('_ModulesConfigurationController', _ModulesConfigurationController);

    _ModulesConfigurationController.$inject = ['$scope', '$state', 'DataUtils', 'Module', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'DuplicateModule', 'searchModule'];

    function _ModulesConfigurationController ($scope, $state, DataUtils, Module, ParseLinks, AlertService, pagingParams, paginationConstants, DuplicateModule, searchModule) {
        var vm = this;
        
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.duplicate = duplicate;
        vm.search = search;
        vm.resetpage = resetPage;

        loadAll();

        function loadAll () {
           Module.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.modules = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function duplicate(data)
        {
			 DuplicateModule.update({id: data.id}, function onSuccess(data) {
                                           
                                                    $state.reload();
                                                
                                        });
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }


         /*** Recherche ***/
         function search()
        {


           var search = $scope.searchBox;
           if(search!="" && search != null)
           {
             searchModule.query({
                critere: search,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            },onSuccess, onError);

             function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'desc' : 'asc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
           }
           else
           {
            resetPage();
            loadAll();
           }
            

            function onSuccess(data, headers) {
               // console.log(headers('link'));
                vm.currentSearch = search;
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.modules = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
               console.log(error);
            }
        }

        function resetPage()
        {
            $state.params.page=1;
            vm.page = 1;
            pagingParams.page = 1;
        }
    }
})();
