(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('CommandDetailController', CommandDetailController);

    CommandDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Command', 'Command_component', 'Provider'];

    function CommandDetailController($scope, $rootScope, $stateParams, previousState, entity, Command, Command_component, Provider) {
        var vm = this;

        vm.command = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:commandUpdate', function(event, result) {
            vm.command = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
