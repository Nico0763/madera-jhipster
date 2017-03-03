(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('ImageResize', ImageResize);

    ImageResize.$inject = [];

    function ImageResize () {


      var methods = {};
        methods.resize = function(image, maxWidth, maxHeight, callback)
        {
            var canvas = document.createElement("canvas");
            var img = new Image();
            var ctx = canvas.getContext("2d");
            var canvasCopy = document.createElement("canvas");
            var copyContext = canvasCopy.getContext("2d");

            img.onload = function()
            {
                var ratio = 1;

                if(img.width > maxWidth)
                    ratio = maxWidth / img.width;
                else if(img.height > maxHeight)
                    ratio = maxHeight / img.height;

                canvasCopy.width = img.width;
                canvasCopy.height = img.height;
                copyContext.drawImage(img, 0, 0);

                canvas.width = img.width * ratio;
                canvas.height = img.height * ratio;
                ctx.drawImage(canvasCopy, 0, 0, canvasCopy.width, canvasCopy.height, 0, 0, canvas.width, canvas.height);
                callback(dataURLtoFile(canvas.toDataURL('image/png'),'a.png'));
            };

            img.src = URL.createObjectURL(image);
        }

        var dataURLtoFile = function(dataurl, filename)
        {
            var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
                    bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
                while(n--){
                    u8arr[n] = bstr.charCodeAt(n);
                }
                return new File([u8arr], filename, {type:mime});
        }
        return methods;
    }
})();
