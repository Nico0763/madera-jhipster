(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('ProviderDetailController', ProviderDetailController);

    ProviderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Provider', 'Component', 'Command'];

    function ProviderDetailController($scope, $rootScope, $stateParams, previousState, entity, Provider, Component, Command) {
        var vm = this;

        vm.provider = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:providerUpdate', function(event, result) {
            vm.provider = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
