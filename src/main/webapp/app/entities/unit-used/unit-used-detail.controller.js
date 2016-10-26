(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Unit_usedDetailController', Unit_usedDetailController);

    Unit_usedDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Unit_used', 'Module_nature', 'Component_nature'];

    function Unit_usedDetailController($scope, $rootScope, $stateParams, previousState, entity, Unit_used, Module_nature, Component_nature) {
        var vm = this;

        vm.unit_used = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:unit_usedUpdate', function(event, result) {
            vm.unit_used = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
