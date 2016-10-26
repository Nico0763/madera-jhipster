(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Finition_extDialogController', Finition_extDialogController);

    Finition_extDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Finition_ext', 'Assortment'];

    function Finition_extDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Finition_ext, Assortment) {
        var vm = this;

        vm.finition_ext = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
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
            if (vm.finition_ext.id !== null) {
                Finition_ext.update(vm.finition_ext, onSaveSuccess, onSaveError);
            } else {
                Finition_ext.save(vm.finition_ext, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:finition_extUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, finition_ext) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        finition_ext.image = base64Data;
                        finition_ext.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
