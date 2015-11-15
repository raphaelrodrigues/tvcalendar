'use strict';

angular.module('tvcalendarApp')
    .factory('SerieSearch', function ($resource) {
        return $resource('api/_search/series/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
