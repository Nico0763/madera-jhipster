(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('PatternDialogController', PatternDialogController);

    PatternDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Pattern', 'Assortment', 'Product'];

    function PatternDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Pattern, Assortment, Product) {
        var vm = this;

        vm.pattern = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.assortments = Assortment.query();
        vm.products = Product.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pattern.id !== null) {
                Pattern.update(vm.pattern, onSaveSuccess, onSaveError);
            } else {
                Pattern.save(vm.pattern, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:patternUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, pattern) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        pattern.image = base64Data;
                        pattern.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
