(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Insulating_typeDialogController', Insulating_typeDialogController);

    Insulating_typeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Insulating_type', 'Assortment'];

    function Insulating_typeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Insulating_type, Assortment) {
        var vm = this;

        vm.insulating_type = entity;
        vm.clear = clear;
        vm.save = save;
        vm.assortments = Assortment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.insulating_type.id !== null) {
                Insulating_type.update(vm.insulating_type, onSaveSuccess, onSaveError);
            } else {
                Insulating_type.save(vm.insulating_type, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:insulating_typeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
