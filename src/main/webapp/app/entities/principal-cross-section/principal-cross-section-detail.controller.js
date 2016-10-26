(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Principal_cross_sectionDetailController', Principal_cross_sectionDetailController);

    Principal_cross_sectionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Principal_cross_section', 'Module'];

    function Principal_cross_sectionDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Principal_cross_section, Module) {
        var vm = this;

        vm.principal_cross_section = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('maderaApp:principal_cross_sectionUpdate', function(event, result) {
            vm.principal_cross_section = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
