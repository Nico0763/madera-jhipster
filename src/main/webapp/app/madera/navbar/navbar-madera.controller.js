(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('NavbarMaderaController', NavbarMaderaController);

    NavbarMaderaController.$inject = ['$scope', '$stateParams','$state','LoginService', 'Principal'];

    function NavbarMaderaController($scope, $stateParams, $state, LoginService, Principal) {
        var vm = this;

        vm.account = null; 
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

       getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register'); 
        }
    

        /*** Gestion du menu ***/
        $('[data-toggle]').on('click', function(e){
            e.preventDefault;
            $(this).toggleClass('active').next('ul').slideToggle();
            $(this).parent().siblings().children('ul').slideUp();
            $(this).parent().siblings().children('.active').removeClass('active');
            $('span').not(this).removeClass('glyphicon-triangle-top');
            $(this).find('span').toggleClass('glyphicon-triangle-top');
            return false;
        });

        $('.nav-stats--toggle-menu ul li a').click(function(){
            $('li a').removeClass('active');
            $(this).addClass('active');
        });

        $('.nav-stats--toggle-menu .toggle-menu--btn').click(function(){
            $('.toggle-menu--btn').removeClass("active");
            $('li a').removeClass("active");
            $(this).addClass("active");
        });
    }
})();
