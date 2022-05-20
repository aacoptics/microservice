// <projectRoot>/.eslintrc.js
module.exports = {
    root: true,
    parserOptions: {
        parser: 'babel-eslint',
        sourceType: 'module',
    },
    env: {
        browser: true,
        node: true,
        es6: true,
    },
    rules: {
        'vue/multi-word-component-names': 0,
    },
}
