(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('_CommandesController', _CommandesController);

    _CommandesController.$inject = ['$scope', '$state', 'DataUtils', 'Command', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants',  'searchCommand', 'searchCommandByState'];

    function _CommandesController ($scope, $state, DataUtils, Command, ParseLinks, AlertService, pagingParams, paginationConstants,  searchCommand, searchCommandByState) {
        var vm = this;
        
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.searchByState = searchByState;
        vm.resetpage = resetPage;
        vm.toState = toState;

        loadAll();

        function loadAll () {
           Command.query({
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
                vm.commands = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                console.log(error);
                AlertService.error(error.data.message);
            }
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
             searchCommand.query({
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
                vm.commands = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
               console.log(error);
            }
        }


        /*** Recherche pas state ***/
        function searchByState()
        {


           var searchState = $scope.searchState;
           if(searchState!="" && searchState != null)
           {
             searchCommandByState.query({
                state: searchState,
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
                vm.currentState = searchState;
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.commands = data;
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

    function toState(state)
    {
        switch(state)
        {
            case 1:
                return "En commande";
                break;
            case 2:
                return "En attente";
                break;
            case 3:
                return "Livrée";
                break;
            default:
                return "Pas d'état";
                break;

        }
    }

})();
