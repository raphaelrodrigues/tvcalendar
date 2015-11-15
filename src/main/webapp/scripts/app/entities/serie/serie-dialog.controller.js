'use strict';

angular.module('tvcalendarApp').controller('SerieDialogController', ['$scope', '$stateParams', '$modalInstance', 'entity', 'Serie', 'SeriePreview',
    function($scope, $stateParams, $modalInstance, entity, Serie, SeriePreview) {

        $scope.serie = entity;
        $scope.load = function(id) {
            Serie.get({
                id: id
            }, function(result) {
                $scope.serie = result;
            });
        };

        var onSaveSuccess = function(result) {
            $scope.$emit('tvcalendarApp:serieUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function(result) {
            $scope.isSaving = false;
        };

        $scope.save = function() {
            $scope.isSaving = true;
            if ($scope.serie.id != null) {
                Serie.update($scope.serie, onSaveSuccess, onSaveError);
            } else {
                Serie.save($scope.serie, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.serie.preview = {};
        $scope.previewSerie = function() {
            console.log($scope.serie.title == undefined || $scope.serie.title.length <= 3);
            if ($scope.serie.title == undefined || $scope.serie.title.length <= 3) {
                return;
            }
            $scope.serie.preview = {};
            SeriePreview.get({
                title: $scope.serie.title
            }, function(result) {
                $scope.serie.preview = result;
                console.log(result)
            });
        }



    }
]);
