(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Finition_extDetailController', Finition_extDetailController);

    Finition_extDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Finition_ext', 'Assortment'];

    function Finition_extDetailController($scope, $rootScope, $stateParams, previousState, entity, Finition_ext, Assortment) {
        var vm = this;

        vm.finition_ext = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:finition_extUpdate', function(event, result) {
            vm.finition_ext = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
