$(function () {
    $(window).on("beforeunload", function () {
        return "Please confirm exiting the application.";
    });
    
    var excerpts;
    $.get("/repository/records", function(res) {
        excerpts = res['_embedded']['gdr:recordExcerpts'];
        $("#jsGrid").jsGrid({
            height: "100%",
            width: "100%",
        
            filtering: false,
            editing: false,
            sorting: false,
            paging: false,
            autoload: false,
        
            pageSize: 15,
            pageButtonCount: 5,
        
            deleteConfirm: "",
        
            data: excerpts,
            
            fields: [
                {name: "exforNumber"},
                {name: "reactionString"},
                {name: "energyAtMaxCrossSection"},
                {name: "maxCrossSection"},
                {name: "fullWidthAtHalfMaximum"},
                {name: "chiSquaredReduced"}
            ]
        });
    });
});
