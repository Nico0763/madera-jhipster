(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('ProductComponentsController', ProductComponentsController);

    ProductComponentsController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Component_product', 'Module', 'getModulesAssortment', 'Product', 'getComponentsProduct'];

    function ProductComponentsController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Component_product, Module, getModulesAssortment, Product, getComponentsProduct) {
        var vm = this;

        vm.product = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.modules = null;
        vm.components = null;
        vm.crtModule = null;
        vm.addName = null;


        vm.editData = {angle:null, length:null};
        vm.select = null;
        

        refreshProducts();
        function refreshProducts()
        {
            vm.components = getComponentsProduct.query({id:entity.id});
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        /*** Edition ***/

        vm.selectElement = function(element, data)
        {
            vm.select = element;
            data.angle = element.angle;
            data.length = element.length;
        }

        function save(data)
        {

            
                    vm.select.angle = data.angle;
                    vm.select.length = data.length;
               

            
            
                Component_product.update(vm.select, function(success)
                    {
                    }, function(error) {
                    });
                vm.select = null;
                

            data.angle = null;
            data.length = null;


        }
    }
})();
