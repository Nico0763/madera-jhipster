(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('HuisseriesController', HuisseriesController);

    HuisseriesController.$inject = ['$scope', '$state', 'DataUtils', 'Frame', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'ImageSaveFile', 'ImageResize','ImageRemoveFile'];

    function HuisseriesController ($scope, $state, DataUtils, Frame, ParseLinks, AlertService, pagingParams, paginationConstants,ImageSaveFile, ImageResize,ImageRemoveFile) {
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


        vm.editData = {name:null, url:null};
        vm.addData = {name:null, url:null ,id:null};

        vm.select = null;

        loadAll();

        function loadAll () {
            Frame.query({
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


                     saveImage(function(data)
                    {                        
                        Frame.update(data, function(success)
                        {
                            console.debug(success)
                        }, function(error) {
                            console.debug(error);
                        });
                        vm.select = null;
                    },'fileinfo',  vm.select,400,400);    
            }
            else if(type==2)
            {
                saveImage(function(data)
                    {
                        Frame.save(data, function(success)
                        {
                            console.debug(success)
                            $state.reload();
                        }, function(error) {
                            console.debug(error);
                        });
                        vm.select = null;


                    },'fileaddinfo',data,400,400);
            }

        }


        function saveImage(callback, formName,  data, width,height)
        {
            var file = document.forms[formName]['fileUploadImage'].files[0];
            if(file==null)
            {
                callback(data);
                return;
            }
          //  ImageRemoveFile.remove({url:deleteId});
            //Delete de l'ancien
            if(data.url != "" && data.url != null && data.url != undefined)
            {   
                ImageRemoveFile.delete({url:data.url});
            }
           ImageResize.resize(file,width,height,function(cropFile) {
            if(cropFile != null && cropFile != undefined)
            {
                var form = document.forms.namedItem("fileinfo");
                var fd = new FormData(form); 
                fd.delete("fileUploadImage");
                fd.append("fileUploadImage", cropFile);

                 ImageSaveFile.save(fd,function(dataUrl)
                    {
                    
                        data.url = dataUrl.url;
                        callback(data);
                    });  
             }
           });
            
        }


       

    }
})();
