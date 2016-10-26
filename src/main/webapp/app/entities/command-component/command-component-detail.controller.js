(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Command_componentDetailController', Command_componentDetailController);

    Command_componentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Command_component', 'Component', 'Command'];

    function Command_componentDetailController($scope, $rootScope, $stateParams, previousState, entity, Command_component, Component, Command) {
        var vm = this;

        vm.command_component = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:command_componentUpdate', function(event, result) {
            vm.command_component = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
