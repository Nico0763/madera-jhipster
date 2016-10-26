(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Module_componentDetailController', Module_componentDetailController);

    Module_componentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Module_component', 'Module', 'Component'];

    function Module_componentDetailController($scope, $rootScope, $stateParams, previousState, entity, Module_component, Module, Component) {
        var vm = this;

        vm.module_component = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:module_componentUpdate', function(event, result) {
            vm.module_component = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
