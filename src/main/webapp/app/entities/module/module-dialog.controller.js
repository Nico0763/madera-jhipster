(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('ModuleDialogController', ModuleDialogController);

    ModuleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Module', 'Principal_cross_section', 'Module_nature', 'Product', 'Assortment', 'Module_component'];

    function ModuleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Module, Principal_cross_section, Module_nature, Product, Assortment, Module_component) {
        var vm = this;

        vm.module = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.principal_cross_sections = Principal_cross_section.query();
        vm.module_natures = Module_nature.query();
        vm.products = Product.query();
        vm.assortments = Assortment.query();
        vm.module_components = Module_component.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.module.id !== null) {
                Module.update(vm.module, onSaveSuccess, onSaveError);
            } else {
                Module.save(vm.module, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:moduleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setCctp = function ($file, module) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        module.cctp = base64Data;
                        module.cctpContentType = $file.type;
                    });
                });
            }
        };

    }
})();
