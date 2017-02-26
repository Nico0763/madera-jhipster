(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('ComponentDialogController', ComponentDialogController);

    ComponentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Component', 'Component_product', 'Module_component', 'Provider', 'Component_nature', 'Command_component'];

    function ComponentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Component, Component_product, Module_component, Provider, Component_nature, Command_component) {
        var vm = this;

        vm.component = entity;
        vm.clear = clear;
        vm.save = save;
        vm.component_products = Component_product.query();
        vm.module_components = Module_component.query();
        vm.providers = Provider.query();
        vm.component_natures = Component_nature.query();
        vm.command_components = Command_component.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.component.id !== null) {
                Component.update(vm.component, onSaveSuccess, onSaveError);
            } else {
                Component.save(vm.component, onSaveSuccess, onSaveError);
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


    }
})();
