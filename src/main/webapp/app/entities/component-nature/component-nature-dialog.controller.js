(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Component_natureDialogController', Component_natureDialogController);

    Component_natureDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Component_nature', 'Component', 'Unit_used'];

    function Component_natureDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Component_nature, Component, Unit_used) {
        var vm = this;

        vm.component_nature = entity;
        vm.clear = clear;
        vm.save = save;
        vm.components = Component.query();
        vm.unit_useds = Unit_used.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.component_nature.id !== null) {
                Component_nature.update(vm.component_nature, onSaveSuccess, onSaveError);
            } else {
                Component_nature.save(vm.component_nature, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:component_natureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
