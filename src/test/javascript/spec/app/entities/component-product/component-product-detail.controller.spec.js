'use strict';

describe('Controller Tests', function() {

    describe('Component_product Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockComponent_product, MockProduct, MockComponent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockComponent_product = jasmine.createSpy('MockComponent_product');
            MockProduct = jasmine.createSpy('MockProduct');
            MockComponent = jasmine.createSpy('MockComponent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Component_product': MockComponent_product,
                'Product': MockProduct,
                'Component': MockComponent
            };
            createController = function() {
                $injector.get('$controller')("Component_productDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'maderaApp:component_productUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
