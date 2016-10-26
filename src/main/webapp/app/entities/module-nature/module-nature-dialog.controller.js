(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Module_natureDialogController', Module_natureDialogController);

    Module_natureDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Module_nature', 'Module', 'Unit_used'];

    function Module_natureDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Module_nature, Module, Unit_used) {
        var vm = this;

        vm.module_nature = entity;
        vm.clear = clear;
        vm.save = save;
        vm.modules = Module.query();
        vm.unit_useds = Unit_used.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.module_nature.id !== null) {
                Module_nature.update(vm.module_nature, onSaveSuccess, onSaveError);
            } else {
                Module_nature.save(vm.module_nature, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:module_natureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
