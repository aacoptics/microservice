export function verifyChinese(rule, value, callback) {
    if (value) {
        if (/[\u4E00-\u9FA5]/g.test(value)) {
            callback(new Error('编码不能输入汉字!'));
        } else {
            callback();
        }
    }
    callback();
}
