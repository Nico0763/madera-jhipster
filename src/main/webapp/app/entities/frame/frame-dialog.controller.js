(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('FrameDialogController', FrameDialogController);

    FrameDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Frame', 'Assortment'];

    function FrameDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Frame, Assortment) {
        var vm = this;

        vm.frame = entity;
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


        vm.setImage = function ($file, frame) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        frame.image = base64Data;
                        frame.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
