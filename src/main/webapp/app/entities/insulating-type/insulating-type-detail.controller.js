(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Insulating_typeDetailController', Insulating_typeDetailController);

    Insulating_typeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Insulating_type', 'Assortment'];

    function Insulating_typeDetailController($scope, $rootScope, $stateParams, previousState, entity, Insulating_type, Assortment) {
        var vm = this;

        vm.insulating_type = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:insulating_typeUpdate', function(event, result) {
            vm.insulating_type = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
