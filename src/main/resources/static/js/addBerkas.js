// Code By Webdevtrick ( https://webdevtrick.com )
function readFile(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function(e) {
            var extension = input.files[0].name.slice(-4).toLowerCase();

            if (extension == ".zip" || extension == ".rar") {
                var htmlPreview =
                    '<img width="100" alt="zip icon" src="https://cdn-icons-png.flaticon.com/512/3979/3979308.png" />' +
                    '<p>' + input.files[0].name + '</p>';
            } else if (extension == ".jpg" || extension == "jpeg" || extension == ".png" || extension == ".gif" || extension == "tiff") {
                var htmlPreview =
                    '<img width="400" src="' + e.target.result + '" />' +
                    '<p>' + input.files[0].name + '</p>';
            } else if (extension == ".doc" || extension == "docx" || extension == ".wps" || extension == "docm") {
                var htmlPreview =
                    '<img width="100" alt="docs icon" src="https://pbs.twimg.com/media/FSJ0RjDaUAAeD3-?format=png&name=small" />' +
                    '<p>' + input.files[0].name + '</p>';
            } else if (extension == "xlsx" || extension == ".xls" || extension == "xlsm" || extension == ".xml") {
                var htmlPreview =
                    '<img width="100" alt="xls icon" src="https://cdn-icons-png.flaticon.com/512/3997/3997638.png" />' +
                    '<p>' + input.files[0].name + '</p>';
            } else if (extension == ".pdf") {
                var htmlPreview =
                    '<img width="100" alt="pdf icon" src="https://cdn-icons-png.flaticon.com/512/3143/3143460.png" />' +
                    '<p>' + input.files[0].name + '</p>';
            } else if (extension == "pptx") {
                var htmlPreview =
                    '<img width="100" alt="ppt icon" src="https://cdn-icons.flaticon.com/png/512/5097/premium/5097542.png?token=exp=1651925838~hmac=e512c5cdff90f496b5c524a05c68ec64" />' +
                    '<p>' + input.files[0].name + '</p>';
            } else if (extension == ".txt") {
                var htmlPreview =
                    '<img width="100" alt="txt icon" src="https://cdn-icons-png.flaticon.com/512/3720/3720207.png" />' +
                    '<p>' + input.files[0].name + '</p>';
            } else if (extension == ".csv") {
                var htmlPreview =
                    '<img width="100" alt="txt icon" src="https://cdn-icons-png.flaticon.com/512/6133/6133884.png" />' +
                    '<p>' + input.files[0].name + '</p>';
            } else if (extension == ".mp4" || extension == ".mkv" || extension == ".3gp" || extension == ".wmv" || extension == "webm" || extension == ".flv" || extension == ".mov") {
                var htmlPreview =
                    '<img width="100" alt="txt icon" src="https://cdn-icons-png.flaticon.com/512/1179/1179069.png" />' +
                    '<p>' + input.files[0].name + '</p>';
            } else {
                var htmlPreview =
                    '<img width="100" alt="file icon" src="https://cdn-icons-png.flaticon.com/512/2965/2965335.png" />' +
                    '<p>' + input.files[0].name + '</p>';
            }
            var wrapperZone = $(input).parent();
            var previewZone = $(input).parent().parent().find('.preview-zone');
            var boxZone = $(input).parent().parent().find('.preview-zone').find('.box').find('.box-body');

            wrapperZone.removeClass('dragover');
            previewZone.removeClass('hidden');
            boxZone.empty();
            boxZone.append(htmlPreview);
        };

        reader.readAsDataURL(input.files[0]);
    }
}

function reset(e) {
    e.wrap('<form>').closest('form').get(0).reset();
    e.unwrap();
}

$(".dropzone").change(function() {
    readFile(this);
});

$('.dropzone-wrapper').on('dragover', function(e) {
    e.preventDefault();
    e.stopPropagation();
    $(this).addClass('dragover');
});

$('.dropzone-wrapper').on('dragleave', function(e) {
    e.preventDefault();
    e.stopPropagation();
    $(this).removeClass('dragover');
});

$('.remove-preview').on('click', function() {
    var boxZone = $(this).parents('.preview-zone').find('.box-body');
    var previewZone = $(this).parents('.preview-zone');
    var dropzone = $(this).parents('.form-group').find('.dropzone');
    boxZone.empty();
    previewZone.addClass('hidden');
    reset(dropzone);
});
