'use strict';

angular.module('tvcalendarApp')
    .controller('SerieController', function ($scope, $state, $modal, Serie, SerieSearch, ParseLinks) {
      
        $scope.series = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Serie.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.series.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.series = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            SerieSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.series = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.serie = {
                name: null,
                title: null,
                year: null,
                released: null,
                genre: null,
                plot: null,
                poster: null,
                imdbRating: null,
                rating: null,
                id: null
            };
        };
    });
