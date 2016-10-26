(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Module_componentDialogController', Module_componentDialogController);

    Module_componentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Module_component', 'Module', 'Component'];

    function Module_componentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Module_component, Module, Component) {
        var vm = this;

        vm.module_component = entity;
        vm.clear = clear;
        vm.save = save;
        vm.modules = Module.query();
        vm.components = Component.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.module_component.id !== null) {
                Module_component.update(vm.module_component, onSaveSuccess, onSaveError);
            } else {
                Module_component.save(vm.module_component, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:module_componentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
