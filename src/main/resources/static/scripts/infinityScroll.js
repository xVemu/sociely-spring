const infinityScroll = (url) => ({
    items: [],
    init(element) {
        if (element == null) return
        const infScroll = new InfiniteScroll(element, {
            path: () =>
                `${url}?page=${this.loadCount ?? 0}`,
            status: '.page-load-status',
            history: false,
            responseBody: 'json'
        });
        infScroll.on('load', body => {
                this.items = this.items.concat(body.content)
                if (body.last) element.data("infiniteScroll").scrollThreshold = false
            }
        );
        infScroll.loadNextPage();
    }
})