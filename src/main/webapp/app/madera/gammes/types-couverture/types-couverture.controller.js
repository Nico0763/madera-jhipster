(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('TypesCouvertureController', TypesCouvertureController);

    TypesCouvertureController.$inject = ['$scope', '$state', 'DataUtils', 'Cover_type', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function TypesCouvertureController ($scope, $state, DataUtils, Cover_type, ParseLinks, AlertService, pagingParams, paginationConstants) {
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


        vm.editData = {name:null, image:null, imageContentType:null};
        vm.addData = {name:null, image:null, imageContentType:null,id:null};

        vm.select = null;

        loadAll();

        function loadAll () {
            Cover_type.query({
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
            data.image = element.image;
            data.imageContentType=element.imageContentType;
        }

        function save(data,type)
        {


            if(type == 1 && vm.select != null)
            {
            
                    vm.select.name = data.name;

      
                    vm.select.image = data.image;
                    vm.select.imageContentType= data.imageContentType;
               

            
            
                Cover_type.update(vm.select, function(success)
                    {
                        console.debug(success)
                    }, function(error) {
                        console.debug(error);
                    });
                vm.select = null;
                
            }
            else if(type==2)
            {
                Cover_type.save({name:data.name, image:data.image, imageContentType:data.imageContentType}, function(success)
                    {
                        console.debug(success)
                        $state.reload();
                    }, function(error) {
                        console.debug(error);
                    });
            }

            data.name = null;
            data.image = null;
            data.imageContentType = null;


        }


        vm.setImage = function ($file, data) {
            
            
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        data.image = base64Data;
                        data.imageContentType = $file.type;
                    });
                });
            }
        };


       

    }
})();
