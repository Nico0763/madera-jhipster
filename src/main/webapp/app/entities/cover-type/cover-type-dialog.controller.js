(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Cover_typeDialogController', Cover_typeDialogController);

    Cover_typeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cover_type', 'Assortment'];

    function Cover_typeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cover_type, Assortment) {
        var vm = this;

        vm.cover_type = entity;
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
            if (vm.cover_type.id !== null) {
                Cover_type.update(vm.cover_type, onSaveSuccess, onSaveError);
            } else {
                Cover_type.save(vm.cover_type, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:cover_typeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
