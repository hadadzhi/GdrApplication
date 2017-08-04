$(function () {
    // Functions
    function row() {
        return $("<tr></tr>");
    }

    function cell(data) {
        return $("<td></td>").append(data);
    }

    function filterSubent(event) {
        notImplemented(event);
    }

    function notImplemented(event) {
        alert("Not Implemented");
        event.preventDefault();
    }

    // End of functions

    $('[data-toggle="tooltip"]').tooltip(); // Init tooltips

    // $(window).on("beforeunload", function () {
    //     return "Please confirm exiting the application.";
    // });

    $("#subent-btn").click(filterSubent);
    $("#subent-input").change(filterSubent);

    const $paging = $("#paging");
    $paging.click(notImplemented);
    $paging.text("X..Y of Z");

    $("#first").click(notImplemented);
    $("#last").click(notImplemented);
    $("#prev").click(notImplemented);
    $("#next").click(notImplemented);

    for (var i = 0; i < 50; ++i) {
        var $tr = row();
        for (var j = 0; j < 9; ++j) {
            $tr.append(cell(Math.random().toString()));
        }
        $("#records-table-body").append($tr);
    }
});
