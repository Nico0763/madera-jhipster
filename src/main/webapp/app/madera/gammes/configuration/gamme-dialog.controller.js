(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('GammeDialogController', GammeDialogController);

    GammeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Assortment', 'Finition_ext', 'Insulating_type', 'Frame', 'Cover_type', 'Module', 'Pattern', 'Quotation'];

    function GammeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Assortment, Finition_ext, Insulating_type, Frame, Cover_type, Module, Pattern, Quotation) {
        var vm = this;

        vm.assortment = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.finition_exts = Finition_ext.query();
        vm.insulating_types = Insulating_type.query();
        vm.frames = Frame.query();
        vm.cover_types = Cover_type.query();
        vm.modules = Module.query();
        vm.patterns = Pattern.query();
        vm.quotations = Quotation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.assortment.id !== null) {
                Assortment.update(vm.assortment, onSaveSuccess, onSaveError);
            } else {
                Assortment.save(vm.assortment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:assortmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setSkeleton_conception_mode = function ($file, assortment) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        assortment.skeleton_conception_mode = base64Data;
                        assortment.skeleton_conception_modeContentType = $file.type;
                    });
                });
            }
        };

    }
})();
