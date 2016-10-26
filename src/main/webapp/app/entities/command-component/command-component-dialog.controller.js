(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Command_componentDialogController', Command_componentDialogController);

    Command_componentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Command_component', 'Component', 'Command'];

    function Command_componentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Command_component, Component, Command) {
        var vm = this;

        vm.command_component = entity;
        vm.clear = clear;
        vm.save = save;
        vm.components = Component.query();
        vm.commands = Command.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.command_component.id !== null) {
                Command_component.update(vm.command_component, onSaveSuccess, onSaveError);
            } else {
                Command_component.save(vm.command_component, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:command_componentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
