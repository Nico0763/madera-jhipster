'use strict';

describe('Controller Tests', function() {

    describe('Command_component Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCommand_component, MockComponent, MockCommand;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCommand_component = jasmine.createSpy('MockCommand_component');
            MockComponent = jasmine.createSpy('MockComponent');
            MockCommand = jasmine.createSpy('MockCommand');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Command_component': MockCommand_component,
                'Component': MockComponent,
                'Command': MockCommand
            };
            createController = function() {
                $injector.get('$controller')("Command_componentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'maderaApp:command_componentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
