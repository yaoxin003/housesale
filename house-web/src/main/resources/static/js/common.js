function nextPage(pageSize,pageNum) {
    var url = $("#searchForm").attr("action");
    var data =  "?pageSize=" + pageSize + "&pageNum=" + pageNum + "&" + $('#searchForm').serialize();
    window.location.href = url + data;
}
