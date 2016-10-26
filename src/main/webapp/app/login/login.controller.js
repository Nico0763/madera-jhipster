(function() {
    'use strict';
 
    angular
        .module('maderaApp')
        .controller('LoginMaderaController', LoginMaderaController);

    LoginMaderaController.$inject = ['$rootScope', '$state', '$timeout', 'Auth'];

    function LoginMaderaController ($rootScope, $state, $timeout, Auth, $uibModalInstance) {
        var vm = this;

        vm.authenticationError = false;
        vm.credentials = {};
        vm.login = login;
        vm.password = null;
        vm.rememberMe = true;
        vm.username = null; 
        
        $rootScope.$on('error-login', function() {
            "error";
        })



        function login (event) {
           //event.preventDefault();

            Auth.login({
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe
            }).then(function () {
                vm.authenticationError = false;
                if ($state.current.name === 'register' || $state.current.name === 'activate' ||
                    $state.current.name === 'finishReset' || $state.current.name === 'requestReset') {
                   // $state.go('home');
            
                }

                $rootScope.$broadcast('authenticationSuccess');

                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is succesful, go to stored previousState and clear previousState
                if (Auth.getPreviousState()) {

                    var previousState = Auth.getPreviousState();
                    Auth.resetPreviousState();
                   // $state.go(previousState.name, previousState.params);
                   
                }
            }).catch(function () {
                vm.authenticationError = true;
            });
        }




    }
})();
