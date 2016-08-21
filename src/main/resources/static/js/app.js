var gatewayApp = angular.module("gatewayApp", ["ui.router"]);

gatewayApp.config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("home");

    $stateProvider
        .state('home', {
            url: '/home',
            templateUrl: "/html/partialsHome.html",
            controller: 'MessageController'
        });
});


gatewayApp.factory("gatewayFactory", function ($http) {
    var factory = {};

    factory.getMessageResponse = function(message) {
        return $http.post("/message", message);
    }

    return factory;
});


gatewayApp.controller("MessageController", function ($scope, gatewayFactory) {

    function init() {
        $scope.errorMessage = "";
        $scope.responseMessage = "";
        $scope.message = {delay_by: 100};
        $scope.loader = {loading: false};
    }

    $scope.getMessageResponse = function(message) {
        $scope.loader.loading = true;
        $scope.responseMessage = "";
        gatewayFactory.getMessageResponse(message).success(function(data){
            $scope.responseMessage = data;
            $scope.errorMessage = "";
            $scope.loader.loading = false;
        }).error(function(data, status, headers, config) {
            $scope.responseMessage = "";
            $scope.errorMessage = data;
            $scope.loader.loading = false;
        });
    }
    init();
});