(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('ModuleDetailController', ModuleDetailController);

    ModuleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Module', 'Principal_cross_section', 'Module_nature', 'Product', 'Assortment', 'Module_component'];

    function ModuleDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Module, Principal_cross_section, Module_nature, Product, Assortment, Module_component) {
        var vm = this;

        vm.module = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('maderaApp:moduleUpdate', function(event, result) {
            vm.module = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
