'use strict';

describe('Controller Tests', function() {

    describe('Provider Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProvider, MockComponent, MockCommand;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProvider = jasmine.createSpy('MockProvider');
            MockComponent = jasmine.createSpy('MockComponent');
            MockCommand = jasmine.createSpy('MockCommand');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Provider': MockProvider,
                'Component': MockComponent,
                'Command': MockCommand
            };
            createController = function() {
                $injector.get('$controller')("ProviderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'maderaApp:providerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
