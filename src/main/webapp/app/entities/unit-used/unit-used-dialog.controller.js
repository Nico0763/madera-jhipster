(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Unit_usedDialogController', Unit_usedDialogController);

    Unit_usedDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Unit_used', 'Module_nature', 'Component_nature'];

    function Unit_usedDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Unit_used, Module_nature, Component_nature) {
        var vm = this;

        vm.unit_used = entity;
        vm.clear = clear;
        vm.save = save;
        vm.module_natures = Module_nature.query();
        vm.component_natures = Component_nature.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.unit_used.id !== null) {
                Unit_used.update(vm.unit_used, onSaveSuccess, onSaveError);
            } else {
                Unit_used.save(vm.unit_used, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:unit_usedUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
