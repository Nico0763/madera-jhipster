(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('PatternDetailController', PatternDetailController);

    PatternDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pattern', 'Assortment', 'Product'];

    function PatternDetailController($scope, $rootScope, $stateParams, previousState, entity, Pattern, Assortment, Product) {
        var vm = this;

        vm.pattern = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:patternUpdate', function(event, result) {
            vm.pattern = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
