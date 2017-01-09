(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('_CommandesDialogController', _CommandesDialogController);

   _CommandesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Command', 'Command_component', 'Provider', 'getCommandComponents'];

    function _CommandesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Command, Command_component, Provider, getCommandComponents) {
        var vm = this;

        vm.command = entity;
        vm.clear = clear;
        vm.save = save;
        vm.command_components = null;
        vm.providers = Provider.query();
        
        loadComponents();

        function loadComponents()
        {
              getCommandComponents.query({
                id:vm.command.id
            },onSuccess, onError);

              function onSuccess(data)
              {
                vm.command_components = data;
                console.debug(vm.command_components);
              }

              function onError(error)
              {
                console.log(error);
              }

        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.command.id !== null) {
                Command.update(vm.command, onSaveSuccess, onSaveError);
            } else {
                Command.save(vm.command, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:commandUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
