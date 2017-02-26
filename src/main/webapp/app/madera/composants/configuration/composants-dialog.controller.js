(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('_ComposantsDialogController', _ComposantsDialogController);

    _ComposantsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Component', 'Component_product', 'Module_component', 'Provider', 'Component_nature', 'Command_component', 'ImageSaveFile', 'ImageResize','ImageRemoveFile'];

    function _ComposantsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Component, Component_product, Module_component, Provider, Component_nature, Command_component, ImageSaveFile, ImageResize,ImageRemoveFile) {
        var vm = this;

        vm.component = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.component_products = Component_product.query();
        vm.module_components = Module_component.query();
        vm.providers = Provider.query();
        vm.component_natures = Component_nature.query();
        vm.command_components = Command_component.query();

        vm.file =null;


        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.component.id !== null) {

                //Update
                    saveImage(function(data)
                    {                        
                        Component.update(vm.component, onSaveSuccess,onSaveError);
                    },'fileinfo',  vm.component,250,250);

            } else {

                saveImage(function(data)
                    {
                        Component.save(vm.component, onSaveSuccess, onSaveError);
                    },'fileinfo',vm.component,250,250);

            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:componentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        function saveImage(callback, formName,  data, width,height)
        {
            var file = vm.file;
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



        vm.setImage = function(f)
        {
            console.debug(f);
           vm.file = f;
        }
    }
})();
