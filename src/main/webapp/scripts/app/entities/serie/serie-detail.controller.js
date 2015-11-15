'use strict';

angular.module('tvcalendarApp')
    .controller('SerieDetailController', function ($scope, $rootScope, $stateParams, entity, Serie) {
        $scope.serie = entity;
        $scope.load = function (id) {
            Serie.get({id: id}, function(result) {
                $scope.serie = result;
            });
        };
        var unsubscribe = $rootScope.$on('tvcalendarApp:serieUpdate', function(event, result) {
            $scope.serie = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
