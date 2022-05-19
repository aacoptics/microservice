export function getResponseDataMessage(responseData, split = ',') { // 传入响应体
    return (responseData['msg'] === undefined ? '' : split + ' ' + responseData['msg']) +
        (responseData['data'] === undefined ? '' : split + ' ' + responseData['data']) +
        (responseData['message'] === undefined ? '' : split + ' ' + responseData['message']);
}


export function date2str(date) {
    if (date !== null) {
        let d = new Date(date);
        let month = '' + (d.getMonth() + 1);
        let day = '' + d.getDate();
        let year = d.getFullYear();

        if (month.length < 2)
            month = '0' + month;
        if (day.length < 2)
            day = '0' + day;

        return [year, month, day].join('-');
    }
    return '无';
}

export function fontAwesomeIconFormat(icon) {
    try {
        return JSON.parse(icon)
    } catch (e) {
        return icon.trim()
    }
}
