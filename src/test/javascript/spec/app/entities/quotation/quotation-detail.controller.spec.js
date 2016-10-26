'use strict';

describe('Controller Tests', function() {

    describe('Quotation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockQuotation, MockAssortment, MockDeadline, MockProduct, MockCustomer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockQuotation = jasmine.createSpy('MockQuotation');
            MockAssortment = jasmine.createSpy('MockAssortment');
            MockDeadline = jasmine.createSpy('MockDeadline');
            MockProduct = jasmine.createSpy('MockProduct');
            MockCustomer = jasmine.createSpy('MockCustomer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Quotation': MockQuotation,
                'Assortment': MockAssortment,
                'Deadline': MockDeadline,
                'Product': MockProduct,
                'Customer': MockCustomer
            };
            createController = function() {
                $injector.get('$controller')("QuotationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'maderaApp:quotationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
