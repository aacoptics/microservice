export function tableFilter(list) { // 传入表格数据
    let filters = {}
    if (list.length) {
        Object.keys(list[0]).forEach(item => { // 拿到第一条数据，将key值组成数组，并将key给filters对象作为键名，值为空数组
            filters[item] = []
        })
        list.forEach(item => { // 遍历表格的数据数组
            for (let key in item) { // 遍历数据数组的每一项(对象)
                if (Object.prototype.hasOwnProperty.call(filters, key) && !filters[key].find(i => i.text === item[key])) {
                    if (item[key] != null) {
                        filters[key].push({text: item[key], value: item[key]})
                    }// filters当前键名对应的值（数组），再push该值组成的对象（el-table筛选条件的格式）
                }
            }
        })
    }
    return filters;
}

export function sortObjByKey(obj) { // 传入表格数据
    const keys = Object.keys(obj).sort();
    const newObj = {};
    for (let i = 0; i < keys.length; i++) {
        const index = keys[i];
        newObj[index] = obj[index];
    }
    return newObj;
}
