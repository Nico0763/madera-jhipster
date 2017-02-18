(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('PatternProductsController', PatternProductsController);

    PatternProductsController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Principal_cross_section', 'Module', 'getModulesAssortment', 'Product', 'getProductsPattern', 'AddProductDependencies', 'DeleteProductDependencies'];

    function PatternProductsController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Principal_cross_section, Module, getModulesAssortment, Product, getProductsPattern, AddProductDependencies, DeleteProductDependencies) {
        var vm = this;

        vm.pattern = entity;
        console.debug(vm.pattern);
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.deleteProduct = deleteProduct;
        vm.addModule = addModule;
        vm.modules = null;
        vm.products = null;
        vm.crtModule = null;
        vm.addName = null;

        loadModules();
        function loadModules()
        {
            if(entity.assortment !=null)
            {
                vm.modules = getModulesAssortment.query({id:entity.assortment.id});
            }
        }

        refreshProducts();
        function refreshProducts()
        {
            vm.products = getProductsPattern.query({id:entity.id});
        }

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

        function addModule()
        {
                AddProductDependencies.save({pattern:vm.pattern, module:vm.crtModule, name:vm.addName}, 
                function(data)
                {
                    vm.products.push(data);
                });
        }


        function deleteProduct(product)
        {
            DeleteProductDependencies.delete({id:product.id}, function(data)
            {

                 vm.products.splice(vm.products.indexOf(product, 1));
            });

        }

    }
})();
