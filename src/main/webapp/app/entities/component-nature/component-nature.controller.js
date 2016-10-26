(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Component_natureController', Component_natureController);

    Component_natureController.$inject = ['$scope', '$state', 'Component_nature'];

    function Component_natureController ($scope, $state, Component_nature) {
        var vm = this;
        
        vm.component_natures = [];

        loadAll();

        function loadAll() {
            Component_nature.query(function(result) {
                vm.component_natures = result;
            });
        }
    }
})();
