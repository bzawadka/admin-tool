'use strict';

describe('Broker Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockBroker, MockBrokerStatus, MockMessageVersion;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockBroker = jasmine.createSpy('MockBroker');
        MockBrokerStatus = jasmine.createSpy('MockBrokerStatus');
        MockMessageVersion = jasmine.createSpy('MockMessageVersion');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Broker': MockBroker,
            'BrokerStatus': MockBrokerStatus,
            'MessageVersion': MockMessageVersion
        };
        createController = function() {
            $injector.get('$controller')("BrokerDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'adminToolApp:brokerUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
