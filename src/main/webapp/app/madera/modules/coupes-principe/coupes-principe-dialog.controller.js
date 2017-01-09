(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('CoupesPrincipeDialogController', CoupesPrincipeDialogController);

    CoupesPrincipeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Principal_cross_section', 'Module'];

    function CoupesPrincipeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Principal_cross_section, Module) {
        var vm = this;

        vm.principal_cross_section = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.modules = Module.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.principal_cross_section.id !== null) {
                Principal_cross_section.update(vm.principal_cross_section, onSaveSuccess, onSaveError);
            } else {
                Principal_cross_section.save(vm.principal_cross_section, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:principal_cross_sectionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, principal_cross_section) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        principal_cross_section.image = base64Data;
                        principal_cross_section.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
