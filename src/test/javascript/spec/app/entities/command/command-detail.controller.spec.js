'use strict';

describe('Controller Tests', function() {

    describe('Command Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCommand, MockCommand_component, MockProvider;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCommand = jasmine.createSpy('MockCommand');
            MockCommand_component = jasmine.createSpy('MockCommand_component');
            MockProvider = jasmine.createSpy('MockProvider');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Command': MockCommand,
                'Command_component': MockCommand_component,
                'Provider': MockProvider
            };
            createController = function() {
                $injector.get('$controller')("CommandDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'maderaApp:commandUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
