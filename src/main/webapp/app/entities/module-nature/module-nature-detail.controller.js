(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Module_natureDetailController', Module_natureDetailController);

    Module_natureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Module_nature', 'Module', 'Unit_used'];

    function Module_natureDetailController($scope, $rootScope, $stateParams, previousState, entity, Module_nature, Module, Unit_used) {
        var vm = this;

        vm.module_nature = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:module_natureUpdate', function(event, result) {
            vm.module_nature = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
