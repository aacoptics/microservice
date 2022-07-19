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
        return icon?.trim()
    }
}

// 防抖
export function debounce(func, wait = 100, immediate = false) {
    // 闭包
    let timeout;
    // 不能用箭头函数
    return function () {
        let context = this, args = arguments;
        let later = function () {
            timeout = null;
            if (!immediate) func.apply(context, args);
        };
        let callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func.apply(context, args);
    }
}

export function isInt(n){
    return Number(n) === n && n % 1 === 0;
}

export function isFloat(n){
    return Number(n) === n && n % 1 !== 0;
}

