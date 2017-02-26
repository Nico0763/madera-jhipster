(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('FrameDialogController', FrameDialogController);

    FrameDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Frame', 'Assortment'];

    function FrameDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Frame, Assortment) {
        var vm = this;

        vm.frame = entity;
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
            if (vm.frame.id !== null) {
                Frame.update(vm.frame, onSaveSuccess, onSaveError);
            } else {
                Frame.save(vm.frame, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:frameUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
