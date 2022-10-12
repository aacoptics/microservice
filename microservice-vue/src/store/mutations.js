export default {
    delTagsItem(state, data) {
        state
            .tagsList
            .splice(data.index, 1);
    },
    setTagsItem(state, data) {
        state
            .tagsList
            .push(data)
    },
    clearTags(state) {
        state.tagsList = []
        state.iframeViews = []
    },
    closeTagsOther(state, data) {
        state.tagsList = data;
        state.iframeViews = state.iframeViews.filter(item => item.path === data.path)
    },
    closeCurrentTag(state, data) {
        for (let i = 0, len = state.tagsList.length; i < len; i++) {
            const item = state.tagsList[i];
            if (item.path === data.$route.fullPath) {
                if (i < len - 1) {
                    data
                        .$router
                        .push(state.tagsList[i + 1].path);
                } else if (i > 0) {
                    data
                        .$router
                        .push(state.tagsList[i - 1].path);
                } else {
                    data
                        .$router
                        .push("/");
                }
                state
                    .tagsList
                    .splice(i, 1);
                break;
            }
        }
    },
    // 侧边栏折叠
    handleCollapse(state, data) {
        state.collapse = data;
    },
    addIframeView(state, data) {
        if (state.iframeViews.some(v => v.path === data.path)) return
        state.iframeViews.push(
            Object.assign({}, data, {
                title: data.meta.title || 'no-name'
            })
        )
    },
    delIframeView(state, data) {
        state.iframeViews = state.iframeViews.filter(item => item.path !== data.path)
    },
}
