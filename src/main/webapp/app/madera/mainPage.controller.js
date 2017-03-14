(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('MainPageController', MainPageController);

   MainPageController.$inject = ['$scope', '$state', 'DataUtils', 'Provider', 'ParseLinks', 'AlertService',  'paginationConstants', 'Quotation', 'Customer'];

    function MainPageController ($scope, $state, DataUtils, Provider, ParseLinks, AlertService, paginationConstants, Quotation, Customer) {
        var vm = this;

        vm.quotations = Quotation.query({ page: 0,
                size: 5,
                sort:sort()});

        vm.customers = Customer.query({ page: 0,
                size: 5,
                sort:sort()});

        function sort() {
                var result = ['id,desc'];
                return result;
            }
       

        vm.toState = function(num)
        {
            switch(num)
            {
                case 1:
                    return "Brouillon";
                    break;
                case 2:
                    return "Accepté";
                    break;
                case 3:
                    return "En attente";
                    break;
                case 4:
                    return "Refusé";
                    break;
                case 5:
                    return "En commande";
                    break;
                case 6:
                    return "Transfert en facturation";
                    break;
            }
        }


    
    }
})();
