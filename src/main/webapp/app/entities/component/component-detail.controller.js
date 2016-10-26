(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('ComponentDetailController', ComponentDetailController);

    ComponentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Component', 'Component_product', 'Module_component', 'Provider', 'Component_nature', 'Command_component'];

    function ComponentDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Component, Component_product, Module_component, Provider, Component_nature, Command_component) {
        var vm = this;

        vm.component = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('maderaApp:componentUpdate', function(event, result) {
            vm.component = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
