(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('ProviderDialogController', ProviderDialogController);

    ProviderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Provider', 'Component', 'Command'];

    function ProviderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Provider, Component, Command) {
        var vm = this;

        vm.provider = entity;
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
            if (vm.provider.id !== null) {
                Provider.update(vm.provider, onSaveSuccess, onSaveError);
            } else {
                Provider.save(vm.provider, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:providerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
