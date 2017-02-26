(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('CoupesPrincipeDialogController', CoupesPrincipeDialogController);

    CoupesPrincipeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Principal_cross_section', 'Module', 'ImageSaveFile', 'ImageResize','ImageRemoveFile'];

    function CoupesPrincipeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Principal_cross_section, Module, ImageSaveFile, ImageResize,ImageRemoveFile) {
        var vm = this;

        vm.principal_cross_section = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.modules = Module.query();

       vm.file =null;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
             vm.isSaving = true;
            if (vm.principal_cross_section.id !== null) {

                //Update
                    saveImage(function(data)
                    {                        
                       Principal_cross_section.update(vm.principal_cross_section, onSaveSuccess, onSaveError);
                    },'fileinfo',  vm.principal_cross_section,400,400);

            } else {

                saveImage(function(data)
                    {
                        Principal_cross_section.save(vm.principal_cross_section, onSaveSuccess, onSaveError);
                    },'fileinfo',vm.principal_cross_section,400,400);

            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:principal_cross_sectionUpdate', result);
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
