(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('NaturesComposantController', NaturesComposantController);

    NaturesComposantController.$inject = ['$scope', '$state', 'DataUtils', 'Component_nature', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'Unit_used'];

    function NaturesComposantController ($scope, $state, DataUtils, Component_nature, ParseLinks, AlertService, pagingParams, paginationConstants, Unit_used) {
        var vm = this;
        
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        /*** Méthodes ***/
        vm.save = save;


        vm.editData = {nature:null, caracteristics:null, unit_used:null};
        vm.addData = {nature:null, caracteristics:null, unit_used:null};

        vm.select = null;

        loadAll();
        vm.unit_used = Unit_used.query();

       function loadAll () {
            Component_nature.query({
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
              //  vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.results = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
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



        /*** Edition ***/

        vm.selectElement = function(element, data)
        {
            vm.select = element;
            data.name = element.name;
            data.caracteristics= element.caracteristics;
            data.unit_used=element.unit_used;
        }

        function save(data,type)
        {


            if(type == 1 && vm.select != null)
            {
            
                    vm.select.name = data.name;

      
                    vm.select.caracteristics = data.caracteristics;
                    vm.select.unit_used= data.unit_used;
               

            
            
                Component_nature.update(vm.select, function(success)
                    {
                        console.debug(success)
                    }, function(error) {
                        console.debug(error);
                    });
                vm.select = null;
                
            }
            else if(type==2)
            {
                Component_nature.save({name:data.name, caracteristics:data.caracteristics, unit_used:data.unit_used}, function(success)
                    {
                        console.debug(success)
                        $state.reload();
                    }, function(error) {
                        console.debug(error);
                    });
            }

            data.name = null;
            data.caracteristics = null;
            data.unit_used = null;


        }


        


       

    }
})();
