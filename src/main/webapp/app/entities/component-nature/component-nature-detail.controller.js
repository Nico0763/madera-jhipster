(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Component_natureDetailController', Component_natureDetailController);

    Component_natureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Component_nature', 'Component', 'Unit_used'];

    function Component_natureDetailController($scope, $rootScope, $stateParams, previousState, entity, Component_nature, Component, Unit_used) {
        var vm = this;

        vm.component_nature = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:component_natureUpdate', function(event, result) {
            vm.component_nature = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
