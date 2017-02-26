(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('FrameDetailController', FrameDetailController);

    FrameDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Frame', 'Assortment'];

    function FrameDetailController($scope, $rootScope, $stateParams, previousState, entity, Frame, Assortment) {
        var vm = this;

        vm.frame = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:frameUpdate', function(event, result) {
            vm.frame = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
